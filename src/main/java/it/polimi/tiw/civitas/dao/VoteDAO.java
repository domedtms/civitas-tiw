package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.Vote;
import it.polimi.tiw.civitas.model.VoteValue;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class VoteDAO {

    public void create(Connection connection, Vote vote) throws SQLException {
        String sql = """
                INSERT INTO votes (law_id, user_id, vote_value)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, vote.getLawId());
            statement.setInt(2, vote.getUserId());
            statement.setString(3, vote.getVoteValue().name());
            statement.executeUpdate();
        }
    }

    public boolean existsByLawAndUser(int lawId, int userId) throws SQLException {
        try (Connection connection = ConnectionHandler.getConnection()) {
            return existsByLawAndUser(connection, lawId, userId);
        }
    }

    public boolean existsByLawAndUser(Connection connection, int lawId, int userId) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS total
                FROM votes
                WHERE law_id = ? AND user_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lawId);
            statement.setInt(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("total") > 0;
            }
        }
    }

    public List<Vote> findByLawId(int lawId) throws SQLException {
        String sql = """
                SELECT id, law_id, user_id, vote_value, created_at
                FROM votes
                WHERE law_id = ?
                ORDER BY created_at ASC
                """;

        List<Vote> votes = new ArrayList<>();

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, lawId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    votes.add(mapVote(resultSet));
                }
            }
        }

        return votes;
    }

    public int countByLawAndValue(int lawId, VoteValue voteValue) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS total
                FROM votes
                WHERE law_id = ? AND vote_value = ?
                """;

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, lawId);
            statement.setString(2, voteValue.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt("total") : 0;
            }
        }
    }

    public Map<VoteValue, Integer> countByLawGrouped(int lawId) throws SQLException {
        String sql = """
                SELECT vote_value, COUNT(*) AS total
                FROM votes
                WHERE law_id = ?
                GROUP BY vote_value
                """;

        Map<VoteValue, Integer> counts = new EnumMap<>(VoteValue.class);

        for (VoteValue value : VoteValue.values()) {
            counts.put(value, 0);
        }

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, lawId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    VoteValue value = VoteValue.valueOf(resultSet.getString("vote_value"));
                    counts.put(value, resultSet.getInt("total"));
                }
            }
        }

        return counts;
    }

    private Vote mapVote(ResultSet resultSet) throws SQLException {
        Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");

        return new Vote(
                resultSet.getInt("id"),
                resultSet.getInt("law_id"),
                resultSet.getInt("user_id"),
                VoteValue.valueOf(resultSet.getString("vote_value")),
                createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null
        );
    }
}