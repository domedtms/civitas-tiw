package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.NationStatsDAO;
import it.polimi.tiw.civitas.model.NationStats;

import java.sql.SQLException;
import java.util.Optional;

public class NationStatsService {

    private final NationStatsDAO nationStatsDAO;

    public NationStatsService() {
        this.nationStatsDAO = new NationStatsDAO();
    }

    public Optional<NationStats> findStatsByNationId(int nationId) throws SQLException {
        if (nationId <= 0) {
            return Optional.empty();
        }

        return nationStatsDAO.findByNationId(nationId);
    }
}