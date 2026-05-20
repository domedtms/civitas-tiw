package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.DecisionLog;
import it.polimi.tiw.civitas.model.DecisionLogAction;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DecisionLogDAO {

    public int create(Connection connection, DecisionLog decisionLog) throws SQLException {
        String sql = """
                INSERT INTO decision_logs (nation_id, law_id, actor_id, action, description)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, decisionLog.getNationId());

            if (decisionLog.getLawId() != null) {
                statement.setInt(2, decisionLog.getLawId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }

            if (decisionLog.getActorId() != null) {
                statement.setInt(3, decisionLog.getActorId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }

            statement.setString(4, decisionLog.getAction().name());
            statement.setString(5, decisionLog.getDescription());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            throw new SQLException("Creating decision log failed, no ID obtained.");
        }
    }

    public List<DecisionLog> findByNationId(int nationId) throws SQLException {
        String sql = """
                SELECT id, nation_id, law_id, actor_id, action, description, created_at
                FROM decision_logs
                WHERE nation_id = ?
                ORDER BY created_at DESC
                """;

        List<DecisionLog> logs = new ArrayList<>();

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    logs.add(mapDecisionLog(resultSet));
                }
            }
        }

        return logs;
    }

    private DecisionLog mapDecisionLog(ResultSet resultSet) throws SQLException {
        Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");

        Integer lawId = resultSet.getObject("law_id") != null
                ? resultSet.getInt("law_id")
                : null;

        Integer actorId = resultSet.getObject("actor_id") != null
                ? resultSet.getInt("actor_id")
                : null;

        return new DecisionLog(
                resultSet.getInt("id"),
                resultSet.getInt("nation_id"),
                lawId,
                actorId,
                DecisionLogAction.valueOf(resultSet.getString("action")),
                resultSet.getString("description"),
                createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null
        );
    }
}