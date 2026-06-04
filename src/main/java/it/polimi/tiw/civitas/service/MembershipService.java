package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.DecisionLogDAO;
import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.model.DecisionLog;
import it.polimi.tiw.civitas.model.DecisionLogAction;
import it.polimi.tiw.civitas.model.MembershipRole;
import it.polimi.tiw.civitas.util.ConnectionHandler;
import it.polimi.tiw.civitas.util.HtmlUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class MembershipService {

    public static final String ACTION_PROMOTE_MINISTER = "PROMOTE_MINISTER";
    public static final String ACTION_DEMOTE_CITIZEN = "DEMOTE_CITIZEN";

    private final MembershipDAO membershipDAO;
    private final NationDAO nationDAO;
    private final DecisionLogDAO decisionLogDAO;

    public MembershipService() {
        this.membershipDAO = new MembershipDAO();
        this.nationDAO = new NationDAO();
        this.decisionLogDAO = new DecisionLogDAO();
    }

    public void joinNation(int userId, int nationId) throws SQLException, MembershipException {
        validateInput(userId, nationId);

        if (nationDAO.findById(nationId).isEmpty()) {
            throw new MembershipException("Micro-nazione non trovata.");
        }

        if (membershipDAO.existsByUserAndNation(userId, nationId)) {
            throw new MembershipException("Sei già cittadino di questa micro-nazione.");
        }

        membershipDAO.createCitizenMembership(userId, nationId);
    }

    public void updateMemberRole(int actorId, int nationId, int targetUserId, String action)
            throws SQLException, MembershipException {

        validateInput(actorId, nationId);

        if (targetUserId <= 0) {
            throw new MembershipException("Utente destinatario non valido.");
        }

        if (action == null || action.trim().isEmpty()) {
            throw new MembershipException("Azione sul ruolo non valida.");
        }

        if (nationDAO.findById(nationId).isEmpty()) {
            throw new MembershipException("Micro-nazione non trovata.");
        }

        try (Connection connection = ConnectionHandler.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                Optional<MembershipRole> actorRoleOptional =
                        membershipDAO.findRoleByUserAndNation(connection, actorId, nationId);

                if (actorRoleOptional.isEmpty() || actorRoleOptional.get() != MembershipRole.FOUNDER) {
                    throw new MembershipException("Solo il fondatore può gestire i ruoli dei membri.");
                }

                Optional<MembershipRole> targetRoleOptional =
                        membershipDAO.findRoleByUserAndNation(connection, targetUserId, nationId);

                if (targetRoleOptional.isEmpty()) {
                    throw new MembershipException("L'utente destinatario non fa parte di questa micro-nazione.");
                }

                MembershipRole currentRole = targetRoleOptional.get();

                if (currentRole == MembershipRole.FOUNDER) {
                    throw new MembershipException("Il ruolo di fondatore non può essere modificato.");
                }

                MembershipRole newRole = resolveNewRole(currentRole, action);

                membershipDAO.updateRole(connection, targetUserId, nationId, newRole);

                DecisionLog decisionLog = new DecisionLog();
                decisionLog.setNationId(nationId);
                decisionLog.setLawId(null);
                decisionLog.setActorId(actorId);
                decisionLog.setAction(DecisionLogAction.ROLE_UPDATED);
                decisionLog.setDescription(
                        "Ruolo dell'utente ID " + targetUserId + " modificato da "
                                + HtmlUtil.label(currentRole).toLowerCase() + " a "
                                + HtmlUtil.label(newRole).toLowerCase() + "."
                );

                decisionLogDAO.create(connection, decisionLog);

                connection.commit();

            } catch (SQLException | MembershipException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    private MembershipRole resolveNewRole(MembershipRole currentRole, String action)
            throws MembershipException {

        String normalizedAction = action.trim().toUpperCase();

        if (ACTION_PROMOTE_MINISTER.equals(normalizedAction)) {
            if (currentRole != MembershipRole.CITIZEN) {
                throw new MembershipException("Solo i cittadini possono essere promossi a ministro.");
            }

            return MembershipRole.MINISTER;
        }

        if (ACTION_DEMOTE_CITIZEN.equals(normalizedAction)) {
            if (currentRole != MembershipRole.MINISTER) {
                throw new MembershipException("Solo i ministri possono essere riportati al ruolo di cittadino.");
            }

            return MembershipRole.CITIZEN;
        }

        throw new MembershipException("Azione sul ruolo non supportata.");
    }

    private void validateInput(int userId, int nationId) throws MembershipException {
        if (userId <= 0) {
            throw new MembershipException("Utente non valido.");
        }

        if (nationId <= 0) {
            throw new MembershipException("Micro-nazione non valida.");
        }
    }
}
