package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.NationStats;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class NationStatsDAO {

    public Optional<NationStats> findByNationId(int nationId) throws SQLException {
        String sql = """
                SELECT
                    n.id AS nation_id,
                    n.name AS nation_name,
                    n.flag_symbol,

                    COALESCE(m.citizens_count, 0) AS citizens_count,
                    COALESCE(m.founders_count, 0) AS founders_count,
                    COALESCE(m.ministers_count, 0) AS ministers_count,
                    COALESCE(m.regular_citizens_count, 0) AS regular_citizens_count,

                    COALESCE(l.laws_count, 0) AS laws_count,
                    COALESCE(l.proposed_laws_count, 0) AS proposed_laws_count,
                    COALESCE(l.approved_laws_count, 0) AS approved_laws_count,
                    COALESCE(l.rejected_laws_count, 0) AS rejected_laws_count,
                    COALESCE(l.repealed_laws_count, 0) AS repealed_laws_count,

                    COALESCE(a.announcements_count, 0) AS announcements_count,
                    COALESCE(d.decision_events_count, 0) AS decision_events_count,

                    COALESCE(r.coins, 0) AS coins,
                    COALESCE(r.culture_points, 0) AS culture_points,
                    COALESCE(r.energy_points, 0) AS energy_points,

                    (
                        COALESCE(r.coins, 0)
                        + COALESCE(r.culture_points, 0)
                        + COALESCE(r.energy_points, 0)
                        + COALESCE(l.approved_laws_count, 0) * 5
                        + COALESCE(m.citizens_count, 0) * 2
                    ) AS score

                FROM nations n

                LEFT JOIN nation_resources r
                    ON n.id = r.nation_id

                LEFT JOIN (
                    SELECT
                        nation_id,
                        COUNT(*) AS citizens_count,
                        SUM(CASE WHEN role = 'FOUNDER' THEN 1 ELSE 0 END) AS founders_count,
                        SUM(CASE WHEN role = 'MINISTER' THEN 1 ELSE 0 END) AS ministers_count,
                        SUM(CASE WHEN role = 'CITIZEN' THEN 1 ELSE 0 END) AS regular_citizens_count
                    FROM memberships
                    GROUP BY nation_id
                ) m ON n.id = m.nation_id

                LEFT JOIN (
                    SELECT
                        nation_id,
                        COUNT(*) AS laws_count,
                        SUM(CASE WHEN status = 'PROPOSED' THEN 1 ELSE 0 END) AS proposed_laws_count,
                        SUM(CASE WHEN status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_laws_count,
                        SUM(CASE WHEN status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_laws_count,
                        SUM(CASE WHEN status = 'REPEALED' THEN 1 ELSE 0 END) AS repealed_laws_count
                    FROM laws
                    GROUP BY nation_id
                ) l ON n.id = l.nation_id

                LEFT JOIN (
                    SELECT nation_id, COUNT(*) AS announcements_count
                    FROM announcements
                    GROUP BY nation_id
                ) a ON n.id = a.nation_id

                LEFT JOIN (
                    SELECT nation_id, COUNT(*) AS decision_events_count
                    FROM decision_logs
                    GROUP BY nation_id
                ) d ON n.id = d.nation_id

                WHERE n.id = ?
                """;

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapNationStats(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    private NationStats mapNationStats(ResultSet resultSet) throws SQLException {
        return new NationStats(
                resultSet.getInt("nation_id"),
                resultSet.getString("nation_name"),
                resultSet.getString("flag_symbol"),

                resultSet.getInt("citizens_count"),
                resultSet.getInt("founders_count"),
                resultSet.getInt("ministers_count"),
                resultSet.getInt("regular_citizens_count"),

                resultSet.getInt("laws_count"),
                resultSet.getInt("proposed_laws_count"),
                resultSet.getInt("approved_laws_count"),
                resultSet.getInt("rejected_laws_count"),
                resultSet.getInt("repealed_laws_count"),

                resultSet.getInt("announcements_count"),
                resultSet.getInt("decision_events_count"),

                resultSet.getInt("coins"),
                resultSet.getInt("culture_points"),
                resultSet.getInt("energy_points"),

                resultSet.getInt("score")
        );
    }
}