package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.MembershipRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}