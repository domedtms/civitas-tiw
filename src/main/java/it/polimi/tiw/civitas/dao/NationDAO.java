package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.Nation;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NationDAO {

    public List<Nation> findAll() throws SQLException {
        String sql = """
                SELECT id, name, motto, description, flag_symbol, founder_id, created_at
                FROM nations
                ORDER BY created_at DESC
                """;

        List<Nation> nations = new ArrayList<>();

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                nations.add(mapNation(resultSet));
            }
        }

        return nations;
    }

    public Optional<Nation> findById(int id) throws SQLException {
        String sql = """
                SELECT id, name, motto, description, flag_symbol, founder_id, created_at
                FROM nations
                WHERE id = ?
                """;

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapNation(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    public boolean existsByName(Connection connection, String name) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS total
                FROM nations
                WHERE name = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("total") > 0;
            }
        }
    }

    public int create(Connection connection, Nation nation) throws SQLException {
        String sql = """
                INSERT INTO nations (name, motto, description, flag_symbol, founder_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, nation.getName());
            statement.setString(2, nation.getMotto());
            statement.setString(3, nation.getDescription());
            statement.setString(4, nation.getFlagSymbol());
            statement.setInt(5, nation.getFounderId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            throw new SQLException("Creating nation failed, no ID obtained.");
        }
    }

    private Nation mapNation(ResultSet resultSet) throws SQLException {
        Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");

        return new Nation(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("motto"),
                resultSet.getString("description"),
                resultSet.getString("flag_symbol"),
                resultSet.getInt("founder_id"),
                createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null
        );
    }
}