package it.polimi.tiw.civitas.controller.nation;

import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.model.Nation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/nation/dashboard")
public class NationDashboardServlet extends HttpServlet {

    private static final String DASHBOARD_VIEW = "/WEB-INF/views/nation/nation-dashboard.jsp";

    private NationDAO nationDAO;

    @Override
    public void init() {
        this.nationDAO = new NationDAO();
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

            request.setAttribute("nation", nationOptional.get());
            request.getRequestDispatcher(DASHBOARD_VIEW).forward(request, response);

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