package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.NationDAO;

import java.sql.SQLException;

public class MembershipService {

    private final MembershipDAO membershipDAO;
    private final NationDAO nationDAO;

    public MembershipService() {
        this.membershipDAO = new MembershipDAO();
        this.nationDAO = new NationDAO();
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

    private void validateInput(int userId, int nationId) throws MembershipException {
        if (userId <= 0) {
            throw new MembershipException("Invalid user.");
        }

        if (nationId <= 0) {
            throw new MembershipException("Invalid nation.");
        }
    }
}