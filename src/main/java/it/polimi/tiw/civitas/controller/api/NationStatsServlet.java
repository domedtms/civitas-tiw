package it.polimi.tiw.civitas.controller.api;

import it.polimi.tiw.civitas.model.NationStats;
import it.polimi.tiw.civitas.service.NationStatsService;
import it.polimi.tiw.civitas.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/api/nation/stats")
public class NationStatsServlet extends HttpServlet {

    private NationStatsService nationStatsService;

    @Override
    public void init() {
        this.nationStatsService = new NationStatsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer nationId = parseId(request.getParameter("id"));

        if (nationId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Optional<NationStats> statsOptional = nationStatsService.findStatsByNationId(nationId);

            if (statsOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            response.getWriter().write(toJson(statsOptional.get()));

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Integer parseId(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String toJson(NationStats stats) {
        return "{"
                + "\"nationId\":" + stats.getNationId() + ","
                + "\"nationName\":" + JsonUtil.quote(stats.getNationName()) + ","
                + "\"flagSymbol\":" + JsonUtil.quote(stats.getFlagSymbol()) + ","

                + "\"citizensCount\":" + stats.getCitizensCount() + ","
                + "\"foundersCount\":" + stats.getFoundersCount() + ","
                + "\"ministersCount\":" + stats.getMinistersCount() + ","
                + "\"regularCitizensCount\":" + stats.getRegularCitizensCount() + ","

                + "\"lawsCount\":" + stats.getLawsCount() + ","
                + "\"proposedLawsCount\":" + stats.getProposedLawsCount() + ","
                + "\"approvedLawsCount\":" + stats.getApprovedLawsCount() + ","
                + "\"rejectedLawsCount\":" + stats.getRejectedLawsCount() + ","
                + "\"repealedLawsCount\":" + stats.getRepealedLawsCount() + ","

                + "\"announcementsCount\":" + stats.getAnnouncementsCount() + ","
                + "\"decisionEventsCount\":" + stats.getDecisionEventsCount() + ","

                + "\"coins\":" + stats.getCoins() + ","
                + "\"culturePoints\":" + stats.getCulturePoints() + ","
                + "\"energyPoints\":" + stats.getEnergyPoints() + ","

                + "\"score\":" + stats.getScore()
                + "}";
    }
}