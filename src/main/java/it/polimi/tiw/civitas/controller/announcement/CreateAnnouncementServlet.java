package it.polimi.tiw.civitas.controller.announcement;

import it.polimi.tiw.civitas.model.User;
import it.polimi.tiw.civitas.service.AnnouncementException;
import it.polimi.tiw.civitas.service.AnnouncementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/announcements/create")
public class CreateAnnouncementServlet extends HttpServlet {

    private static final String CREATE_ANNOUNCEMENT_VIEW = "/WEB-INF/views/announcement/create-announcement.jsp";
    private static final String SESSION_USER = "loggedUser";

    private AnnouncementService announcementService;

    @Override
    public void init() {
        this.announcementService = new AnnouncementService();
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
            boolean authorized = announcementService.canCreateAnnouncement(loggedUser.getId(), nationId);

            if (!authorized) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            request.setAttribute("nationId", nationId);
            request.getRequestDispatcher(CREATE_ANNOUNCEMENT_VIEW).forward(request, response);

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
        String content = request.getParameter("content");

        try {
            announcementService.createAnnouncement(nationId, loggedUser.getId(), title, content);
            response.sendRedirect(request.getContextPath() + "/nation?id=" + nationId);

        } catch (AnnouncementException e) {
            preserveFormValues(request, nationId, title, content);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(CREATE_ANNOUNCEMENT_VIEW).forward(request, response);

        } catch (SQLException e) {
            preserveFormValues(request, nationId, title, content);
            request.setAttribute("error", "Errore inatteso. Riprova più tardi.");
            request.getRequestDispatcher(CREATE_ANNOUNCEMENT_VIEW).forward(request, response);
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

    private void preserveFormValues(HttpServletRequest request, int nationId, String title, String content) {
        request.setAttribute("nationId", nationId);
        request.setAttribute("title", title);
        request.setAttribute("content", content);
    }
}
