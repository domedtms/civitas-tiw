package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.dao.NationResourceDAO;
import it.polimi.tiw.civitas.model.Nation;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class NationService {

    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_MOTTO_LENGTH = 150;
    private static final int MAX_FLAG_LENGTH = 20;
    private static final int MAX_DESCRIPTION_LENGTH = 3000;

    private final NationDAO nationDAO;
    private final MembershipDAO membershipDAO;
    private final NationResourceDAO nationResourceDAO;

    public NationService() {
        this.nationDAO = new NationDAO();
        this.membershipDAO = new MembershipDAO();
        this.nationResourceDAO = new NationResourceDAO();
    }

    public int createNation(String name, String motto, String description, String flagSymbol, int founderId)
            throws SQLException, NationException {

        String normalizedName = normalize(name);
        String normalizedMotto = normalizeOptional(motto);
        String normalizedDescription = normalizeOptional(description);
        String normalizedFlagSymbol = normalizeOptional(flagSymbol);

        validateInput(normalizedName, normalizedMotto, normalizedDescription, normalizedFlagSymbol, founderId);

        try (Connection connection = ConnectionHandler.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                if (nationDAO.existsByName(connection, normalizedName)) {
                    throw new NationException("A nation with this name already exists.");
                }

                Nation nation = new Nation();
                nation.setName(normalizedName);
                nation.setMotto(normalizedMotto);
                nation.setDescription(normalizedDescription);
                nation.setFlagSymbol(normalizedFlagSymbol);
                nation.setFounderId(founderId);

                int nationId = nationDAO.create(connection, nation);

                membershipDAO.createFounderMembership(connection, founderId, nationId);
                nationResourceDAO.initializeResources(connection, nationId);

                connection.commit();
                return nationId;

            } catch (SQLException | NationException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    private void validateInput(String name, String motto, String description, String flagSymbol, int founderId)
            throws NationException {

        if (founderId <= 0) {
            throw new NationException("Invalid founder.");
        }

        if (isBlank(name) || name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new NationException("Nation name must contain between 3 and 100 characters.");
        }

        if (motto != null && motto.length() > MAX_MOTTO_LENGTH) {
            throw new NationException("Motto must contain at most 150 characters.");
        }

        if (flagSymbol != null && flagSymbol.length() > MAX_FLAG_LENGTH) {
            throw new NationException("Flag symbol must contain at most 20 characters.");
        }

        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new NationException("Description must contain at most 3000 characters.");
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeOptional(String value) {
        String normalized = normalize(value);
        return normalized.isEmpty() ? null : normalized;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}