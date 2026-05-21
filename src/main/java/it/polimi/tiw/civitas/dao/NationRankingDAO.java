package it.polimi.tiw.civitas.dao;

import it.polimi.tiw.civitas.model.NationRankingItem;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NationRankingDAO {

    public List<NationRankingItem> findRanking() throws SQLException {
        String sql = """
                SELECT
                    n.id AS nation_id,
                    n.name AS nation_name,
                    n.flag_symbol,
                    COALESCE(m.citizens_count, 0) AS citizens_count,
                    COALESCE(l.approved_laws_count, 0) AS approved_laws_count,
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
                    SELECT nation_id, COUNT(*) AS citizens_count
                    FROM memberships
                    GROUP BY nation_id
                ) m ON n.id = m.nation_id
                LEFT JOIN (
                    SELECT nation_id, COUNT(*) AS approved_laws_count
                    FROM laws
                    WHERE status = 'APPROVED'
                    GROUP BY nation_id
                ) l ON n.id = l.nation_id
                ORDER BY score DESC, citizens_count DESC, n.name ASC
                """;

        List<NationRankingItem> ranking = new ArrayList<>();

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ranking.add(mapRankingItem(resultSet));
            }
        }

        return ranking;
    }

    private NationRankingItem mapRankingItem(ResultSet resultSet) throws SQLException {
        return new NationRankingItem(
                resultSet.getInt("nation_id"),
                resultSet.getString("nation_name"),
                resultSet.getString("flag_symbol"),
                resultSet.getInt("citizens_count"),
                resultSet.getInt("approved_laws_count"),
                resultSet.getInt("coins"),
                resultSet.getInt("culture_points"),
                resultSet.getInt("energy_points"),
                resultSet.getInt("score")
        );
    }
}