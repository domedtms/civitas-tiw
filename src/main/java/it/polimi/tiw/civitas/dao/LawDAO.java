package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.Law;
import it.polimi.tiw.civitas.model.LawStatus;
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

public class LawDAO {

    public int create(Connection connection, Law law) throws SQLException {
        String sql = """
                INSERT INTO laws (nation_id, proposer_id, title, description, status)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, law.getNationId());
            statement.setInt(2, law.getProposerId());
            statement.setString(3, law.getTitle());
            statement.setString(4, law.getDescription());
            statement.setString(5, law.getStatus().name());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            throw new SQLException("Creating law failed, no ID obtained.");
        }
    }

    public Optional<Law> findById(int id) throws SQLException {
        String sql = """
                SELECT id, nation_id, proposer_id, title, description, status, created_at, closed_at
                FROM laws
                WHERE id = ?
                """;

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapLaw(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    public List<Law> findByNationId(int nationId) throws SQLException {
        String sql = """
                SELECT id, nation_id, proposer_id, title, description, status, created_at, closed_at
                FROM laws
                WHERE nation_id = ?
                ORDER BY created_at DESC
                """;

        List<Law> laws = new ArrayList<>();

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    laws.add(mapLaw(resultSet));
                }
            }
        }

        return laws;
    }

    public List<Law> findByNationIdAndStatus(int nationId, LawStatus status) throws SQLException {
        String sql = """
                SELECT id, nation_id, proposer_id, title, description, status, created_at, closed_at
                FROM laws
                WHERE nation_id = ? AND status = ?
                ORDER BY created_at DESC
                """;

        List<Law> laws = new ArrayList<>();

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nationId);
            statement.setString(2, status.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    laws.add(mapLaw(resultSet));
                }
            }
        }

        return laws;
    }

    public void updateStatusAndClose(Connection connection, int lawId, LawStatus status) throws SQLException {
        String sql = """
                UPDATE laws
                SET status = ?, closed_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, lawId);
            statement.executeUpdate();
        }
    }

    public void updateStatus(Connection connection, int lawId, LawStatus status) throws SQLException {
        String sql = """
                UPDATE laws
                SET status = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, lawId);
            statement.executeUpdate();
        }
    }

    private Law mapLaw(ResultSet resultSet) throws SQLException {
        Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");
        Timestamp closedAtTimestamp = resultSet.getTimestamp("closed_at");

        return new Law(
                resultSet.getInt("id"),
                resultSet.getInt("nation_id"),
                resultSet.getInt("proposer_id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                LawStatus.valueOf(resultSet.getString("status")),
                createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null,
                closedAtTimestamp != null ? closedAtTimestamp.toLocalDateTime() : null
        );
    }
}