package it.polimi.tiw.civitas.controller.law;

import it.polimi.tiw.civitas.model.User;
import it.polimi.tiw.civitas.service.VoteException;
import it.polimi.tiw.civitas.service.VoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/law/vote")
public class VoteLawServlet extends HttpServlet {

    private static final String SESSION_USER = "loggedUser";

    private VoteService voteService;

    @Override
    public void init() {
        this.voteService = new VoteService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = getLoggedUser(request);

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer lawId = parseId(request.getParameter("lawId"));

        if (lawId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String voteValue = request.getParameter("voteValue");

        try {
            voteService.voteLaw(lawId, loggedUser.getId(), voteValue);
            response.sendRedirect(request.getContextPath() + "/law?id=" + lawId);

        } catch (VoteException e) {
            String encodedError = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/law?id=" + lawId + "&voteError=" + encodedError);

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/nations");
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