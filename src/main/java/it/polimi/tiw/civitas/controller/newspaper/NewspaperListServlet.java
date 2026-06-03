package it.polimi.tiw.civitas.controller.newspaper;

import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.dao.NationalNewspaperDAO;
import it.polimi.tiw.civitas.model.Nation;
import it.polimi.tiw.civitas.model.NationalNewspaper;
import it.polimi.tiw.civitas.model.User;
import it.polimi.tiw.civitas.service.NationalNewspaperService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/nation/newspapers")
public class NewspaperListServlet extends HttpServlet {

    private static final String NEWSPAPERS_VIEW = "/WEB-INF/views/newspaper/newspapers.jsp";
    private static final String SESSION_USER = "loggedUser";

    private NationDAO nationDAO;
    private NationalNewspaperDAO nationalNewspaperDAO;
    private NationalNewspaperService nationalNewspaperService;

    @Override
    public void init() {
        this.nationDAO = new NationDAO();
        this.nationalNewspaperDAO = new NationalNewspaperDAO();
        this.nationalNewspaperService = new NationalNewspaperService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer nationId = parseId(request.getParameter("nationId"));

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

            List<NationalNewspaper> newspapers = nationalNewspaperDAO.findByNationId(nationId);

            User loggedUser = getLoggedUser(request);
            boolean canGenerateNewspaper = loggedUser != null
                    && nationalNewspaperService.canGenerateNewspaper(loggedUser.getId(), nationId);

            request.setAttribute("nation", nationOptional.get());
            request.setAttribute("newspapers", newspapers);
            request.setAttribute("canGenerateNewspaper", canGenerateNewspaper);

            request.getRequestDispatcher(NEWSPAPERS_VIEW).forward(request, response);

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object user = session.getAttribute(SESSION_USER);

        if (user instanceof User) {
            return (User) user;
        }

        return null;
    }

    private Integer parseId(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}