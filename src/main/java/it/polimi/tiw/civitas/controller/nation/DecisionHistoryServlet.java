package it.polimi.tiw.civitas.controller.nation;

import it.polimi.tiw.civitas.dao.DecisionLogDAO;
import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.model.DecisionLog;
import it.polimi.tiw.civitas.model.Nation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/nation/history")
public class DecisionHistoryServlet extends HttpServlet {

    private static final String DECISION_HISTORY_VIEW = "/WEB-INF/views/nation/decision-history.jsp";

    private NationDAO nationDAO;
    private DecisionLogDAO decisionLogDAO;

    @Override
    public void init() {
        this.nationDAO = new NationDAO();
        this.decisionLogDAO = new DecisionLogDAO();
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
            Optional<Nation> nationOptional = nationDAO.findById(nationId);

            if (nationOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            List<DecisionLog> decisionLogs = decisionLogDAO.findByNationId(nationId);

            request.setAttribute("nation", nationOptional.get());
            request.setAttribute("decisionLogs", decisionLogs);

            request.getRequestDispatcher(DECISION_HISTORY_VIEW).forward(request, response);

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
}