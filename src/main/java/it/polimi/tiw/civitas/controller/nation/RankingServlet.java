package it.polimi.tiw.civitas.controller.nation;

import it.polimi.tiw.civitas.dao.NationRankingDAO;
import it.polimi.tiw.civitas.model.NationRankingItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/ranking")
public class RankingServlet extends HttpServlet {

    private static final String RANKING_VIEW = "/WEB-INF/views/nation/ranking.jsp";

    private NationRankingDAO nationRankingDAO;

    @Override
    public void init() {
        this.nationRankingDAO = new NationRankingDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<NationRankingItem> ranking = nationRankingDAO.findRanking();

            request.setAttribute("ranking", ranking);
            request.getRequestDispatcher(RANKING_VIEW).forward(request, response);

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}