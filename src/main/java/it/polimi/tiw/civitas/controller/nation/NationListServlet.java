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
import java.util.List;

@WebServlet("/nations")
public class NationListServlet extends HttpServlet {

    private static final String NATIONS_VIEW = "/WEB-INF/views/nation/nations.jsp";

    private NationDAO nationDAO;

    @Override
    public void init() {
        this.nationDAO = new NationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Nation> nations = nationDAO.findAll();
            request.setAttribute("nations", nations);
            request.getRequestDispatcher(NATIONS_VIEW).forward(request, response);

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}