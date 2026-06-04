package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.DecisionLogDAO;
import it.polimi.tiw.civitas.dao.LawDAO;
import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.model.DecisionLog;
import it.polimi.tiw.civitas.model.DecisionLogAction;
import it.polimi.tiw.civitas.model.Law;
import it.polimi.tiw.civitas.model.LawStatus;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class LawService {

    private static final int MIN_TITLE_LENGTH = 3;
    private static final int MAX_TITLE_LENGTH = 120;
    private static final int MIN_DESCRIPTION_LENGTH = 10;
    private static final int MAX_DESCRIPTION_LENGTH = 5000;

    private final LawDAO lawDAO;
    private final NationDAO nationDAO;
    private final MembershipDAO membershipDAO;
    private final DecisionLogDAO decisionLogDAO;

    public LawService() {
        this.lawDAO = new LawDAO();
        this.nationDAO = new NationDAO();
        this.membershipDAO = new MembershipDAO();
        this.decisionLogDAO = new DecisionLogDAO();
    }

    public boolean canProposeLaw(int userId, int nationId) throws SQLException {
        if (userId <= 0 || nationId <= 0) {
            return false;
        }

        if (nationDAO.findById(nationId).isEmpty()) {
            return false;
        }

        return membershipDAO.existsByUserAndNation(userId, nationId);
    }

    public int proposeLaw(int nationId, int proposerId, String title, String description)
            throws SQLException, LawException {

        String normalizedTitle = normalize(title);
        String normalizedDescription = normalize(description);

        validateInput(nationId, proposerId, normalizedTitle, normalizedDescription);

        if (nationDAO.findById(nationId).isEmpty()) {
            throw new LawException("Micro-nazione non trovata.");
        }

        if (!membershipDAO.existsByUserAndNation(proposerId, nationId)) {
            throw new LawException("Solo i cittadini di questa micro-nazione possono proporre leggi.");
        }

        try (Connection connection = ConnectionHandler.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                Law law = new Law();
                law.setNationId(nationId);
                law.setProposerId(proposerId);
                law.setTitle(normalizedTitle);
                law.setDescription(normalizedDescription);
                law.setStatus(LawStatus.PROPOSED);

                int lawId = lawDAO.create(connection, law);

                DecisionLog decisionLog = new DecisionLog();
                decisionLog.setNationId(nationId);
                decisionLog.setLawId(lawId);
                decisionLog.setActorId(proposerId);
                decisionLog.setAction(DecisionLogAction.LAW_PROPOSED);
                decisionLog.setDescription("Nuova legge proposta: " + normalizedTitle);

                decisionLogDAO.create(connection, decisionLog);

                connection.commit();
                return lawId;

            } catch (SQLException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    private void validateInput(int nationId, int proposerId, String title, String description)
            throws LawException {

        if (nationId <= 0) {
            throw new LawException("Micro-nazione non valida.");
        }

        if (proposerId <= 0) {
            throw new LawException("Proponente non valido.");
        }

        if (isBlank(title) || title.length() < MIN_TITLE_LENGTH || title.length() > MAX_TITLE_LENGTH) {
            throw new LawException("Il titolo deve contenere tra 3 e 120 caratteri.");
        }

        if (isBlank(description) || description.length() < MIN_DESCRIPTION_LENGTH || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new LawException("La descrizione deve contenere tra 10 e 5000 caratteri.");
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
