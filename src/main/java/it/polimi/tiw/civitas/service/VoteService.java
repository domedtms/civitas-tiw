package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.LawDAO;
import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.VoteDAO;
import it.polimi.tiw.civitas.model.Law;
import it.polimi.tiw.civitas.model.LawStatus;
import it.polimi.tiw.civitas.model.Vote;
import it.polimi.tiw.civitas.model.VoteValue;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

public class VoteService {

    private final LawDAO lawDAO;
    private final VoteDAO voteDAO;
    private final MembershipDAO membershipDAO;

    public VoteService() {
        this.lawDAO = new LawDAO();
        this.voteDAO = new VoteDAO();
        this.membershipDAO = new MembershipDAO();
    }

    public boolean canVote(int userId, int lawId) throws SQLException {
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

        if (!membershipDAO.existsByUserAndNation(userId, law.getNationId())) {
            return false;
        }

        return !voteDAO.existsByLawAndUser(lawId, userId);
    }

    public void voteLaw(int lawId, int userId, String voteValueRaw)
            throws SQLException, VoteException {

        if (lawId <= 0) {
            throw new VoteException("Legge non valida.");
        }

        if (userId <= 0) {
            throw new VoteException("Utente non valido.");
        }

        VoteValue voteValue = parseVoteValue(voteValueRaw);

        Optional<Law> lawOptional = lawDAO.findById(lawId);

        if (lawOptional.isEmpty()) {
            throw new VoteException("Legge non trovata.");
        }

        Law law = lawOptional.get();

        if (law.getStatus() != LawStatus.PROPOSED) {
            throw new VoteException("Questa legge non è aperta al voto.");
        }

        if (!membershipDAO.existsByUserAndNation(userId, law.getNationId())) {
            throw new VoteException("Solo i cittadini di questa micro-nazione possono votare questa legge.");
        }

        try (Connection connection = ConnectionHandler.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                if (voteDAO.existsByLawAndUser(connection, lawId, userId)) {
                    throw new VoteException("Hai già votato questa legge.");
                }

                Vote vote = new Vote();
                vote.setLawId(lawId);
                vote.setUserId(userId);
                vote.setVoteValue(voteValue);

                voteDAO.create(connection, vote);

                connection.commit();

            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                throw new VoteException("Hai già votato questa legge.");

            } catch (SQLException | VoteException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    private VoteValue parseVoteValue(String value) throws VoteException {
        if (value == null || value.trim().isEmpty()) {
            throw new VoteException("Valore di voto non valido.");
        }

        try {
            return VoteValue.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new VoteException("Valore di voto non valido.");
        }
    }
}
