package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.Announcement;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAO {

    public int create(Announcement announcement) throws SQLException {
        String sql = """
                INSERT INTO announcements (nation_id, author_id, title, content)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, announcement.getNationId());
            statement.setInt(2, announcement.getAuthorId());
            statement.setString(3, announcement.getTitle());
            statement.setString(4, announcement.getContent());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            throw new SQLException("Creating announcement failed, no ID obtained.");
        }
    }

    public List<Announcement> findByNationId(int nationId) throws SQLException {
        String sql = """
                SELECT id, nation_id, author_id, title, content, created_at
                FROM announcements
                WHERE nation_id = ?
                ORDER BY created_at DESC
                """;

        List<Announcement> announcements = new ArrayList<>();

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    announcements.add(mapAnnouncement(resultSet));
                }
            }
        }

        return announcements;
    }

    private Announcement mapAnnouncement(ResultSet resultSet) throws SQLException {
        Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");

        return new Announcement(
                resultSet.getInt("id"),
                resultSet.getInt("nation_id"),
                resultSet.getInt("author_id"),
                resultSet.getString("title"),
                resultSet.getString("content"),
                createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null
        );
    }
}