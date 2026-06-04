package it.polimi.tiw.civitas.controller.law;

import it.polimi.tiw.civitas.model.User;
import it.polimi.tiw.civitas.service.LawException;
import it.polimi.tiw.civitas.service.LawService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/laws/create")
public class CreateLawServlet extends HttpServlet {

    private static final String CREATE_LAW_VIEW = "/WEB-INF/views/law/create-law.jsp";
    private static final String SESSION_USER = "loggedUser";

    private LawService lawService;

    @Override
    public void init() {
        this.lawService = new LawService();
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
            if (!lawService.canProposeLaw(loggedUser.getId(), nationId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            request.setAttribute("nationId", nationId);
            request.getRequestDispatcher(CREATE_LAW_VIEW).forward(request, response);

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

        String title = request.getParameter("title");
        String description = request.getParameter("description");

        try {
            int lawId = lawService.proposeLaw(nationId, loggedUser.getId(), title, description);
            response.sendRedirect(request.getContextPath() + "/law?id=" + lawId);

        } catch (LawException e) {
            preserveFormValues(request, nationId, title, description);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(CREATE_LAW_VIEW).forward(request, response);

        } catch (SQLException e) {
            preserveFormValues(request, nationId, title, description);
            request.setAttribute("error", "Errore inatteso. Riprova più tardi.");
            request.getRequestDispatcher(CREATE_LAW_VIEW).forward(request, response);
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

    private void preserveFormValues(HttpServletRequest request, int nationId, String title, String description) {
        request.setAttribute("nationId", nationId);
        request.setAttribute("title", title);
        request.setAttribute("description", description);
    }
}
