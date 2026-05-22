package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.Citizen;
import it.polimi.tiw.civitas.model.MembershipRole;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MembershipDAO {

    public void createFounderMembership(Connection connection, int userId, int nationId) throws SQLException {
        String sql = """
                INSERT INTO memberships (user_id, nation_id, role)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, nationId);
            statement.setString(3, MembershipRole.FOUNDER.name());
            statement.executeUpdate();
        }
    }

    public void createCitizenMembership(int userId, int nationId) throws SQLException {
        String sql = """
                INSERT INTO memberships (user_id, nation_id, role)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, nationId);
            statement.setString(3, MembershipRole.CITIZEN.name());
            statement.executeUpdate();
        }
    }

    public boolean existsByUserAndNation(int userId, int nationId) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS total
                FROM memberships
                WHERE user_id = ? AND nation_id = ?
                """;

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, nationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("total") > 0;
            }
        }
    }

    public List<Citizen> findCitizensByNationId(int nationId) throws SQLException {
        String sql = """
                SELECT u.id AS user_id,
                       u.username,
                       m.role,
                       m.joined_at
                FROM memberships m
                INNER JOIN users u ON m.user_id = u.id
                WHERE m.nation_id = ?
                ORDER BY
                    CASE m.role
                        WHEN 'FOUNDER' THEN 1
                        WHEN 'MINISTER' THEN 2
                        ELSE 3
                    END,
                    m.joined_at ASC
                """;

        List<Citizen> citizens = new ArrayList<>();

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    citizens.add(mapCitizen(resultSet));
                }
            }
        }

        return citizens;
    }

    private Citizen mapCitizen(ResultSet resultSet) throws SQLException {
        Timestamp joinedAtTimestamp = resultSet.getTimestamp("joined_at");

        return new Citizen(
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                MembershipRole.valueOf(resultSet.getString("role")),
                joinedAtTimestamp != null ? joinedAtTimestamp.toLocalDateTime() : null
        );
    }
    
    public Optional<MembershipRole> findRoleByUserAndNation(int userId, int nationId) throws SQLException {
        try (Connection connection = ConnectionHandler.getConnection()) {
            return findRoleByUserAndNation(connection, userId, nationId);
        }
    }

    public Optional<MembershipRole> findRoleByUserAndNation(Connection connection, int userId, int nationId)
            throws SQLException {

        String sql = """
                SELECT role
                FROM memberships
                WHERE user_id = ? AND nation_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, nationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(MembershipRole.valueOf(resultSet.getString("role")));
                }
            }
        }

        return Optional.empty();
    }
    public boolean hasAnyRole(int userId, int nationId, MembershipRole... roles) throws SQLException {
        Optional<MembershipRole> currentRole = findRoleByUserAndNation(userId, nationId);

        if (currentRole.isEmpty()) {
            return false;
        }

        for (MembershipRole role : roles) {
            if (currentRole.get() == role) {
                return true;
            }
        }

        return false;
    }
    public void updateRole(Connection connection, int userId, int nationId, MembershipRole role)
            throws SQLException {

        String sql = """
                UPDATE memberships
                SET role = ?
                WHERE user_id = ? AND nation_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, role.name());
            statement.setInt(2, userId);
            statement.setInt(3, nationId);
            statement.executeUpdate();
        }
    }
}