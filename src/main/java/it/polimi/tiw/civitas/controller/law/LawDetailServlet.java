package it.polimi.tiw.civitas.controller.law;

import it.polimi.tiw.civitas.dao.LawDAO;
import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.model.Law;
import it.polimi.tiw.civitas.model.Nation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/law")
public class LawDetailServlet extends HttpServlet {

    private static final String LAW_DETAIL_VIEW = "/WEB-INF/views/law/law-detail.jsp";

    private LawDAO lawDAO;
    private NationDAO nationDAO;

    @Override
    public void init() {
        this.lawDAO = new LawDAO();
        this.nationDAO = new NationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer lawId = parseId(request.getParameter("id"));

        if (lawId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Optional<Law> lawOptional = lawDAO.findById(lawId);

            if (lawOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Law law = lawOptional.get();
            Optional<Nation> nationOptional = nationDAO.findById(law.getNationId());

            if (nationOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("law", law);
            request.setAttribute("nation", nationOptional.get());

            request.getRequestDispatcher(LAW_DETAIL_VIEW).forward(request, response);

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