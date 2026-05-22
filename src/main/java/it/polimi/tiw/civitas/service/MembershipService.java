package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.DecisionLogDAO;
import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.model.DecisionLog;
import it.polimi.tiw.civitas.model.DecisionLogAction;
import it.polimi.tiw.civitas.model.MembershipRole;
import it.polimi.tiw.civitas.util.ConnectionHandler;

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
            throw new MembershipException("Nation not found.");
        }

        if (membershipDAO.existsByUserAndNation(userId, nationId)) {
            throw new MembershipException("You are already a citizen of this nation.");
        }

        membershipDAO.createCitizenMembership(userId, nationId);
    }

    public void updateMemberRole(int actorId, int nationId, int targetUserId, String action)
            throws SQLException, MembershipException {

        validateInput(actorId, nationId);

        if (targetUserId <= 0) {
            throw new MembershipException("Invalid target user.");
        }

        if (action == null || action.trim().isEmpty()) {
            throw new MembershipException("Invalid role action.");
        }

        if (nationDAO.findById(nationId).isEmpty()) {
            throw new MembershipException("Nation not found.");
        }

        try (Connection connection = ConnectionHandler.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                Optional<MembershipRole> actorRoleOptional =
                        membershipDAO.findRoleByUserAndNation(connection, actorId, nationId);

                if (actorRoleOptional.isEmpty() || actorRoleOptional.get() != MembershipRole.FOUNDER) {
                    throw new MembershipException("Only the founder can manage member roles.");
                }

                Optional<MembershipRole> targetRoleOptional =
                        membershipDAO.findRoleByUserAndNation(connection, targetUserId, nationId);

                if (targetRoleOptional.isEmpty()) {
                    throw new MembershipException("Target user is not a member of this nation.");
                }

                MembershipRole currentRole = targetRoleOptional.get();

                if (currentRole == MembershipRole.FOUNDER) {
                    throw new MembershipException("The founder role cannot be changed.");
                }

                MembershipRole newRole = resolveNewRole(currentRole, action);

                membershipDAO.updateRole(connection, targetUserId, nationId, newRole);

                DecisionLog decisionLog = new DecisionLog();
                decisionLog.setNationId(nationId);
                decisionLog.setLawId(null);
                decisionLog.setActorId(actorId);
                decisionLog.setAction(DecisionLogAction.ROLE_UPDATED);
                decisionLog.setDescription(
                        "User ID " + targetUserId + " role changed from "
                                + currentRole.name() + " to " + newRole.name() + "."
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
                throw new MembershipException("Only citizens can be promoted to minister.");
            }

            return MembershipRole.MINISTER;
        }

        if (ACTION_DEMOTE_CITIZEN.equals(normalizedAction)) {
            if (currentRole != MembershipRole.MINISTER) {
                throw new MembershipException("Only ministers can be demoted to citizen.");
            }

            return MembershipRole.CITIZEN;
        }

        throw new MembershipException("Unsupported role action.");
    }

    private void validateInput(int userId, int nationId) throws MembershipException {
        if (userId <= 0) {
            throw new MembershipException("Invalid user.");
        }

        if (nationId <= 0) {
            throw new MembershipException("Invalid nation.");
        }
    }
}