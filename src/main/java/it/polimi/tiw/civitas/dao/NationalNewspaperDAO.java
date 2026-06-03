package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.NationalNewspaper;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class NationalNewspaperDAO {

    public int create(NationalNewspaper newspaper) throws SQLException {
        String sql = """
                INSERT INTO national_newspapers (
                    nation_id,
                    generated_by,
                    period,
                    title,
                    editorial,
                    political_summary,
                    resources_summary,
                    legislative_summary,
                    announcements_summary,
                    historical_summary
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, newspaper.getNationId());
            statement.setInt(2, newspaper.getGeneratedBy());
            statement.setString(3, newspaper.getPeriod());
            statement.setString(4, newspaper.getTitle());
            statement.setString(5, newspaper.getEditorial());
            statement.setString(6, newspaper.getPoliticalSummary());
            statement.setString(7, newspaper.getResourcesSummary());
            statement.setString(8, newspaper.getLegislativeSummary());
            statement.setString(9, newspaper.getAnnouncementsSummary());
            statement.setString(10, newspaper.getHistoricalSummary());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            throw new SQLException("Creating national newspaper failed, no ID obtained.");
        }
    }

    public boolean existsByNationAndPeriod(int nationId, String period) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS total
                FROM national_newspapers
                WHERE nation_id = ? AND period = ?
                """;

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nationId);
            statement.setString(2, period);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("total") > 0;
            }
        }
    }

    private NationalNewspaper mapNationalNewspaper(ResultSet resultSet) throws SQLException {
        Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");

        return new NationalNewspaper(
                resultSet.getInt("id"),
                resultSet.getInt("nation_id"),
                resultSet.getInt("generated_by"),
                resultSet.getString("period"),
                resultSet.getString("title"),
                resultSet.getString("editorial"),
                resultSet.getString("political_summary"),
                resultSet.getString("resources_summary"),
                resultSet.getString("legislative_summary"),
                resultSet.getString("announcements_summary"),
                resultSet.getString("historical_summary"),
                createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null
        );
    }
}