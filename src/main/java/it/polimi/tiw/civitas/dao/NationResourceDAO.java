package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.NationResources;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class NationResourceDAO {

    public void initializeResources(Connection connection, int nationId) throws SQLException {
        String sql = """
                INSERT INTO nation_resources (nation_id, coins, culture_points, energy_points)
                VALUES (?, 100, 0, 0)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nationId);
            statement.executeUpdate();
        }
    }

    public Optional<NationResources> findByNationId(int nationId) throws SQLException {
        String sql = """
                SELECT nation_id, coins, culture_points, energy_points
                FROM nation_resources
                WHERE nation_id = ?
                """;

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapNationResources(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    public void updateResources(Connection connection, NationResources resources) throws SQLException {
        String sql = """
                UPDATE nation_resources
                SET coins = ?, culture_points = ?, energy_points = ?
                WHERE nation_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, resources.getCoins());
            statement.setInt(2, resources.getCulturePoints());
            statement.setInt(3, resources.getEnergyPoints());
            statement.setInt(4, resources.getNationId());
            statement.executeUpdate();
        }
    }

    public void incrementResources(Connection connection, int nationId,
                                   int coinsDelta, int cultureDelta, int energyDelta) throws SQLException {
        String sql = """
                UPDATE nation_resources
                SET coins = GREATEST(0, coins + ?),
                    culture_points = GREATEST(0, culture_points + ?),
                    energy_points = GREATEST(0, energy_points + ?)
                WHERE nation_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, coinsDelta);
            statement.setInt(2, cultureDelta);
            statement.setInt(3, energyDelta);
            statement.setInt(4, nationId);
            statement.executeUpdate();
        }
    }

    private NationResources mapNationResources(ResultSet resultSet) throws SQLException {
        return new NationResources(
                resultSet.getInt("nation_id"),
                resultSet.getInt("coins"),
                resultSet.getInt("culture_points"),
                resultSet.getInt("energy_points")
        );
    }
}