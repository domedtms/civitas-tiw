package it.polimi.tiw.civitas.controller.newspaper;

import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.dao.NationalNewspaperDAO;
import it.polimi.tiw.civitas.model.Nation;
import it.polimi.tiw.civitas.model.NationalNewspaper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/nation/newspaper")
public class NewspaperDetailServlet extends HttpServlet {

    private static final String NEWSPAPER_DETAIL_VIEW = "/WEB-INF/views/newspaper/newspaper-detail.jsp";

    private NationalNewspaperDAO nationalNewspaperDAO;
    private NationDAO nationDAO;

    @Override
    public void init() {
        this.nationalNewspaperDAO = new NationalNewspaperDAO();
        this.nationDAO = new NationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer newspaperId = parseId(request.getParameter("id"));

        if (newspaperId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Optional<NationalNewspaper> newspaperOptional =
                    nationalNewspaperDAO.findById(newspaperId);

            if (newspaperOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            NationalNewspaper newspaper = newspaperOptional.get();

            Optional<Nation> nationOptional = nationDAO.findById(newspaper.getNationId());

            if (nationOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("newspaper", newspaper);
            request.setAttribute("nation", nationOptional.get());

            request.getRequestDispatcher(NEWSPAPER_DETAIL_VIEW).forward(request, response);

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