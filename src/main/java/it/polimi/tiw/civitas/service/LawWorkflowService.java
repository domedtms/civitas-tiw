package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.DecisionLogDAO;
import it.polimi.tiw.civitas.dao.LawDAO;
import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.VoteDAO;
import it.polimi.tiw.civitas.model.DecisionLog;
import it.polimi.tiw.civitas.model.DecisionLogAction;
import it.polimi.tiw.civitas.model.Law;
import it.polimi.tiw.civitas.model.LawStatus;
import it.polimi.tiw.civitas.model.MembershipRole;
import it.polimi.tiw.civitas.model.VoteValue;
import it.polimi.tiw.civitas.util.ConnectionHandler;
import it.polimi.tiw.civitas.dao.NationResourceDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class LawWorkflowService {

    private final LawDAO lawDAO;
    private final VoteDAO voteDAO;
    private final MembershipDAO membershipDAO;
    private final DecisionLogDAO decisionLogDAO;
    private final NationResourceDAO nationResourceDAO;

    public LawWorkflowService() {
        this.lawDAO = new LawDAO();
        this.voteDAO = new VoteDAO();
        this.membershipDAO = new MembershipDAO();
        this.decisionLogDAO = new DecisionLogDAO();
        this.nationResourceDAO = new NationResourceDAO();
    }

    public boolean canCloseLaw(int userId, int lawId) throws SQLException {
        if (userId <= 0 || lawId <= 0) {
            return false;
        }

        Optional<Law> lawOptional = lawDAO.findById(lawId);

        if (lawOptional.isEmpty()) {
            return false;
        }

        Law law = lawOptional.get();

        if (law.getStatus() != LawStatus.PROPOSED) {
            return false;
        }

        return membershipDAO.hasAnyRole(
                userId,
                law.getNationId(),
                MembershipRole.FOUNDER,
                MembershipRole.MINISTER
        );
    }

    public boolean canRepealLaw(int userId, int lawId) throws SQLException {
        if (userId <= 0 || lawId <= 0) {
            return false;
        }

        Optional<Law> lawOptional = lawDAO.findById(lawId);

        if (lawOptional.isEmpty()) {
            return false;
        }

        Law law = lawOptional.get();

        if (law.getStatus() != LawStatus.APPROVED) {
            return false;
        }

        return membershipDAO.hasAnyRole(
                userId,
                law.getNationId(),
                MembershipRole.FOUNDER,
                MembershipRole.MINISTER
        );
    }

    public LawStatus closeLaw(int lawId, int actorId) throws SQLException, LawWorkflowException {
        if (lawId <= 0) {
            throw new LawWorkflowException("Invalid law.");
        }

        if (actorId <= 0) {
            throw new LawWorkflowException("Invalid actor.");
        }

        try (Connection connection = ConnectionHandler.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                Optional<Law> lawOptional = lawDAO.findById(connection, lawId);

                if (lawOptional.isEmpty()) {
                    throw new LawWorkflowException("Law not found.");
                }

                Law law = lawOptional.get();

                if (law.getStatus() != LawStatus.PROPOSED) {
                    throw new LawWorkflowException("Only proposed laws can be closed.");
                }

                boolean authorized = membershipDAO.hasAnyRole(
                        actorId,
                        law.getNationId(),
                        MembershipRole.FOUNDER,
                        MembershipRole.MINISTER
                );

                if (!authorized) {
                    throw new LawWorkflowException("You are not authorized to close this vote.");
                }

                Map<VoteValue, Integer> voteCounts = voteDAO.countByLawGrouped(connection, lawId);

                int yesVotes = voteCounts.getOrDefault(VoteValue.YES, 0);
                int noVotes = voteCounts.getOrDefault(VoteValue.NO, 0);

                LawStatus resultStatus = yesVotes > noVotes
                        ? LawStatus.APPROVED
                        : LawStatus.REJECTED;

                lawDAO.updateStatusAndClose(connection, lawId, resultStatus);
                applyResourceUpdateForClosedLaw(connection, law, resultStatus);

                DecisionLog decisionLog = new DecisionLog();
                decisionLog.setNationId(law.getNationId());
                decisionLog.setLawId(lawId);
                decisionLog.setActorId(actorId);
                decisionLog.setAction(
                        resultStatus == LawStatus.APPROVED
                                ? DecisionLogAction.LAW_APPROVED
                                : DecisionLogAction.LAW_REJECTED
                );
                decisionLog.setDescription(buildDecisionDescription(law, resultStatus, yesVotes, noVotes));

                decisionLogDAO.create(connection, decisionLog);

                connection.commit();
                return resultStatus;

            } catch (SQLException | LawWorkflowException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    public void repealLaw(int lawId, int actorId) throws SQLException, LawWorkflowException {
        if (lawId <= 0) {
            throw new LawWorkflowException("Invalid law.");
        }

        if (actorId <= 0) {
            throw new LawWorkflowException("Invalid actor.");
        }

        try (Connection connection = ConnectionHandler.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                Optional<Law> lawOptional = lawDAO.findById(connection, lawId);

                if (lawOptional.isEmpty()) {
                    throw new LawWorkflowException("Law not found.");
                }

                Law law = lawOptional.get();

                if (law.getStatus() != LawStatus.APPROVED) {
                    throw new LawWorkflowException("Only approved laws can be repealed.");
                }

                boolean authorized = membershipDAO.hasAnyRole(
                        actorId,
                        law.getNationId(),
                        MembershipRole.FOUNDER,
                        MembershipRole.MINISTER
                );

                if (!authorized) {
                    throw new LawWorkflowException("You are not authorized to repeal this law.");
                }

                lawDAO.updateStatus(connection, lawId, LawStatus.REPEALED);
                nationResourceDAO.incrementResources(connection, law.getNationId(), -10, -5, 0);

                DecisionLog decisionLog = new DecisionLog();
                decisionLog.setNationId(law.getNationId());
                decisionLog.setLawId(lawId);
                decisionLog.setActorId(actorId);
                decisionLog.setAction(DecisionLogAction.LAW_REPEALED);
                decisionLog.setDescription("Law \"" + law.getTitle() + "\" was repealed.");

                DecisionLog resourceLog = new DecisionLog();
                resourceLog.setNationId(law.getNationId());
                resourceLog.setLawId(lawId);
                resourceLog.setActorId(null);
                resourceLog.setAction(DecisionLogAction.RESOURCE_UPDATED);
                resourceLog.setDescription("Resources updated after law repeal: coins -10, culture -5.");

                decisionLogDAO.create(connection, resourceLog);

                decisionLogDAO.create(connection, decisionLog);

                connection.commit();

            } catch (SQLException | LawWorkflowException e) {
                connection.rollback();
                throw e;

            } finally {
                    connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    private void applyResourceUpdateForClosedLaw(Connection connection, Law law, LawStatus resultStatus)
            throws SQLException {

        DecisionLog resourceLog = new DecisionLog();
        resourceLog.setNationId(law.getNationId());
        resourceLog.setLawId(law.getId());
        resourceLog.setActorId(null);
        resourceLog.setAction(DecisionLogAction.RESOURCE_UPDATED);

        if (resultStatus == LawStatus.APPROVED) {
            nationResourceDAO.incrementResources(connection, law.getNationId(), 20, 10, 5);
            resourceLog.setDescription("Resources updated after law approval: coins +20,culture +10, energy +5.");
        } else {
            nationResourceDAO.incrementResources(connection, law.getNationId(), -5, 0, -2);
            resourceLog.setDescription("Resources updated after law rejection: coins -5, energy -2.");
        }

        decisionLogDAO.create(connection, resourceLog);
    }

    private String buildDecisionDescription(Law law, LawStatus resultStatus, int yesVotes, int noVotes) {
        return "Law \"" + law.getTitle() + "\" was "
                + resultStatus.name()
                + " with YES=" + yesVotes
                + " and NO=" + noVotes + ".";
    }
}