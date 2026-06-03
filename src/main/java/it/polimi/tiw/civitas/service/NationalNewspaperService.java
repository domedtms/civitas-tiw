package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.AnnouncementDAO;
import it.polimi.tiw.civitas.dao.DecisionLogDAO;
import it.polimi.tiw.civitas.dao.LawDAO;
import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.dao.NationalNewspaperDAO;
import it.polimi.tiw.civitas.model.Announcement;
import it.polimi.tiw.civitas.model.DecisionLog;
import it.polimi.tiw.civitas.model.Law;
import it.polimi.tiw.civitas.model.MembershipRole;
import it.polimi.tiw.civitas.model.Nation;
import it.polimi.tiw.civitas.model.NationStats;
import it.polimi.tiw.civitas.model.NationalNewspaper;

import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class NationalNewspaperService {

    private static final int MAX_ITEMS_IN_SUMMARY = 5;

    private final NationalNewspaperDAO nationalNewspaperDAO;
    private final NationDAO nationDAO;
    private final MembershipDAO membershipDAO;
    private final NationStatsService nationStatsService;
    private final LawDAO lawDAO;
    private final AnnouncementDAO announcementDAO;
    private final DecisionLogDAO decisionLogDAO;

    public NationalNewspaperService() {
        this.nationalNewspaperDAO = new NationalNewspaperDAO();
        this.nationDAO = new NationDAO();
        this.membershipDAO = new MembershipDAO();
        this.nationStatsService = new NationStatsService();
        this.lawDAO = new LawDAO();
        this.announcementDAO = new AnnouncementDAO();
        this.decisionLogDAO = new DecisionLogDAO();
    }

    public boolean canGenerateNewspaper(int userId, int nationId) throws SQLException {
        if (userId <= 0 || nationId <= 0) {
            return false;
        }

        return membershipDAO.hasAnyRole(
                userId,
                nationId,
                MembershipRole.FOUNDER,
                MembershipRole.MINISTER
        );
    }

    public int generateNewspaper(int nationId, int generatedBy, String period)
            throws SQLException, NewspaperGenerationException {

        validateInput(nationId, generatedBy, period);

        String normalizedPeriod = normalizePeriod(period);

        Optional<Nation> nationOptional = nationDAO.findById(nationId);

        if (nationOptional.isEmpty()) {
            throw new NewspaperGenerationException("Nation not found.");
        }

        if (!canGenerateNewspaper(generatedBy, nationId)) {
            throw new NewspaperGenerationException("You are not authorized to generate this newspaper.");
        }

        if (nationalNewspaperDAO.existsByNationAndPeriod(nationId, normalizedPeriod)) {
            throw new NewspaperGenerationException("A national newspaper already exists for this period.");
        }

        Nation nation = nationOptional.get();

        NationStats stats = nationStatsService.findStatsByNationId(nationId)
                .orElseThrow(() -> new NewspaperGenerationException("Nation statistics not available."));

        List<Law> laws = lawDAO.findByNationId(nationId);
        List<Announcement> announcements = announcementDAO.findByNationId(nationId);
        List<DecisionLog> decisionLogs = decisionLogDAO.findByNationId(nationId);

        NationalNewspaper newspaper = buildNewspaper(
                nation,
                stats,
                laws,
                announcements,
                decisionLogs,
                generatedBy,
                normalizedPeriod
        );

        return nationalNewspaperDAO.create(newspaper);
    }

    private void validateInput(int nationId, int generatedBy, String period)
            throws NewspaperGenerationException {

        if (nationId <= 0) {
            throw new NewspaperGenerationException("Invalid nation.");
        }

        if (generatedBy <= 0) {
            throw new NewspaperGenerationException("Invalid generator.");
        }

        if (period == null || period.trim().isEmpty()) {
            throw new NewspaperGenerationException("Period is required.");
        }
    }

    private String normalizePeriod(String period) throws NewspaperGenerationException {
        String normalizedPeriod = period.trim();

        try {
            YearMonth.parse(normalizedPeriod);
            return normalizedPeriod;
        } catch (DateTimeParseException e) {
            throw new NewspaperGenerationException("Period must use YYYY-MM format.");
        }
    }

    private NationalNewspaper buildNewspaper(Nation nation,
                                             NationStats stats,
                                             List<Law> laws,
                                             List<Announcement> announcements,
                                             List<DecisionLog> decisionLogs,
                                             int generatedBy,
                                             String period) {

        NationalNewspaper newspaper = new NationalNewspaper();

        newspaper.setNationId(nation.getId());
        newspaper.setGeneratedBy(generatedBy);
        newspaper.setPeriod(period);
        newspaper.setTitle("Giornale Nazionale di " + nation.getName() + " — " + period);
        newspaper.setEditorial(buildEditorial(nation, stats, period));
        newspaper.setPoliticalSummary(buildPoliticalSummary(stats));
        newspaper.setResourcesSummary(buildResourcesSummary(stats));
        newspaper.setLegislativeSummary(buildLegislativeSummary(stats, laws));
        newspaper.setAnnouncementsSummary(buildAnnouncementsSummary(announcements));
        newspaper.setHistoricalSummary(buildHistoricalSummary(decisionLogs));

        return newspaper;
    }

    private String buildEditorial(Nation nation, NationStats stats, String period) {
        StringBuilder builder = new StringBuilder();

        builder.append("Edizione nazionale del periodo ")
                .append(period)
                .append(" per la micro-nazione ")
                .append(nation.getName())
                .append(". ");

        builder.append("La comunità conta ")
                .append(stats.getCitizensCount())
                .append(" cittadini, con ")
                .append(stats.getMinistersCount())
                .append(" ministri attivi. ");

        if (stats.getCitizensCount() <= 1) {
            builder.append("La nazione si trova ancora in una fase iniziale di costruzione istituzionale.");
        } else {
            builder.append("La nazione mostra una struttura civica già avviata e una base politica osservabile.");
        }

        return builder.toString();
    }

    private String buildPoliticalSummary(NationStats stats) {
        if (stats.getLawsCount() == 0) {
            return "Non risultano ancora leggi proposte. La vita politica formale della micro-nazione è in fase iniziale.";
        }

        if (stats.getApprovedLawsCount() > stats.getRejectedLawsCount()) {
            return "Il quadro politico appare positivo: le leggi approvate superano quelle respinte, indicando una capacità decisionale stabile.";
        }

        if (stats.getRejectedLawsCount() > stats.getApprovedLawsCount()) {
            return "Il quadro politico mostra una fase di confronto intenso: le leggi respinte superano quelle approvate.";
        }

        return "Il quadro politico appare equilibrato: leggi approvate e respinte sono in rapporto bilanciato.";
    }

    private String buildResourcesSummary(NationStats stats) {
        StringBuilder builder = new StringBuilder();

        builder.append("Le risorse simboliche registrano ")
                .append(stats.getCoins())
                .append(" coins, ")
                .append(stats.getCulturePoints())
                .append(" punti cultura e ")
                .append(stats.getEnergyPoints())
                .append(" punti energia. ");

        if (stats.getEnergyPoints() < 10) {
            builder.append("Il livello energetico è basso e potrebbe richiedere nuove decisioni favorevoli.");
        } else {
            builder.append("Il livello energetico risulta stabile.");
        }

        if (stats.getCulturePoints() >= 30) {
            builder.append(" La cultura nazionale appare particolarmente sviluppata.");
        }

        return builder.toString();
    }

    private String buildLegislativeSummary(NationStats stats, List<Law> laws) {
        StringBuilder builder = new StringBuilder();

        builder.append("L'attività legislativa comprende ")
                .append(stats.getLawsCount())
                .append(" leggi totali: ")
                .append(stats.getProposedLawsCount())
                .append(" proposte, ")
                .append(stats.getApprovedLawsCount())
                .append(" approvate, ")
                .append(stats.getRejectedLawsCount())
                .append(" respinte e ")
                .append(stats.getRepealedLawsCount())
                .append(" abrogate.");

        if (!laws.isEmpty()) {
            builder.append(" Ultime leggi registrate: ");

            int limit = Math.min(MAX_ITEMS_IN_SUMMARY, laws.size());

            for (int i = 0; i < limit; i++) {
                Law law = laws.get(i);

                if (i > 0) {
                    builder.append("; ");
                }

                builder.append("\"")
                        .append(law.getTitle())
                        .append("\"")
                        .append(" (")
                        .append(law.getStatus().name())
                        .append(")");
            }

            builder.append(".");
        }

        return builder.toString();
    }

    private String buildAnnouncementsSummary(List<Announcement> announcements) {
        if (announcements.isEmpty()) {
            return "Non sono presenti comunicati ufficiali nel periodo osservato.";
        }

        StringBuilder builder = new StringBuilder();

        builder.append("La comunicazione istituzionale comprende ")
                .append(announcements.size())
                .append(" comunicati complessivi. Ultimi comunicati: ");

        int limit = Math.min(MAX_ITEMS_IN_SUMMARY, announcements.size());

        for (int i = 0; i < limit; i++) {
            Announcement announcement = announcements.get(i);

            if (i > 0) {
                builder.append("; ");
            }

            builder.append("\"")
                    .append(announcement.getTitle())
                    .append("\"");
        }

        builder.append(".");

        return builder.toString();
    }

    private String buildHistoricalSummary(List<DecisionLog> decisionLogs) {
        if (decisionLogs.isEmpty()) {
            return "Lo storico decisionale non contiene ancora eventi rilevanti.";
        }

        StringBuilder builder = new StringBuilder();

        builder.append("Lo storico decisionale contiene ")
                .append(decisionLogs.size())
                .append(" eventi. Eventi più recenti: ");

        int limit = Math.min(MAX_ITEMS_IN_SUMMARY, decisionLogs.size());

        for (int i = 0; i < limit; i++) {
            DecisionLog log = decisionLogs.get(i);

            if (i > 0) {
                builder.append("; ");
            }

            builder.append(log.getAction().name());
        }

        builder.append(".");

        return builder.toString();
    }
}