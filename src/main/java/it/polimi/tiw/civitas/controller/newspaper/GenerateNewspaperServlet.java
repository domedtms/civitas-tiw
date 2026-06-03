package it.polimi.tiw.civitas.controller.newspaper;

import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.model.Nation;
import it.polimi.tiw.civitas.model.User;
import it.polimi.tiw.civitas.service.NationalNewspaperService;
import it.polimi.tiw.civitas.service.NewspaperGenerationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Optional;

@WebServlet("/nation/newspapers/generate")
public class GenerateNewspaperServlet extends HttpServlet {

    private static final String GENERATE_NEWSPAPER_VIEW = "/WEB-INF/views/newspaper/generate-newspaper.jsp";
    private static final String SESSION_USER = "loggedUser";

    private NationalNewspaperService nationalNewspaperService;
    private NationDAO nationDAO;

    @Override
    public void init() {
        this.nationalNewspaperService = new NationalNewspaperService();
        this.nationDAO = new NationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = getLoggedUser(request);

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

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

            if (!nationalNewspaperService.canGenerateNewspaper(loggedUser.getId(), nationId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            request.setAttribute("nation", nationOptional.get());
            request.setAttribute("period", YearMonth.now().toString());

            request.getRequestDispatcher(GENERATE_NEWSPAPER_VIEW).forward(request, response);

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        User loggedUser = getLoggedUser(request);

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer nationId = parseId(request.getParameter("nationId"));

        if (nationId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String period = request.getParameter("period");

        try {
            int newspaperId = nationalNewspaperService.generateNewspaper(nationId, loggedUser.getId(), period);
            response.sendRedirect(request.getContextPath() + "/nation/newspaper?id=" + newspaperId);

        } catch (NewspaperGenerationException e) {
            preserveFormValues(request, nationId, period);
            request.setAttribute("error", e.getMessage());
            forwardWithNation(request, response, nationId);

        } catch (SQLException e) {
            preserveFormValues(request, nationId, period);
            request.setAttribute("error", "Unexpected error. Please try again later.");
            forwardWithNation(request, response, nationId);
        }
    }

    private void forwardWithNation(HttpServletRequest request, HttpServletResponse response, int nationId)
            throws ServletException, IOException {

        try {
            Optional<Nation> nationOptional = nationDAO.findById(nationId);

            if (nationOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("nation", nationOptional.get());
            request.getRequestDispatcher(GENERATE_NEWSPAPER_VIEW).forward(request, response);

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void preserveFormValues(HttpServletRequest request, int nationId, String period) {
        request.setAttribute("nationId", nationId);
        request.setAttribute("period", period);
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