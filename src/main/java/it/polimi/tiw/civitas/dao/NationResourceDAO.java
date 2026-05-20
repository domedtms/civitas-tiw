package it.polimi.tiw.civitas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NationResourceDAO {

    public void initializeResources(Connection connection, int nationId) throws SQLException {
        String sql = """
                INSERT INTO nation_resources (nation_id, coins, culture_points, energy_points)
                VALUES (?, 0, 0, 0)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nationId);
            statement.executeUpdate();
        }
    }
}