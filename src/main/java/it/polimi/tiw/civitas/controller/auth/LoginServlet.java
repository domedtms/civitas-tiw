package it.polimi.tiw.civitas.controller.auth;

import it.polimi.tiw.civitas.model.User;
import it.polimi.tiw.civitas.service.AuthException;
import it.polimi.tiw.civitas.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String LOGIN_VIEW = "/WEB-INF/views/auth/login.jsp";
    private static final String SESSION_USER = "loggedUser";

    private AuthService authService;

    @Override
    public void init() {
        this.authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = authService.login(email, password);
            removeSensitiveData(user);

            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_USER, user);

            response.sendRedirect(request.getContextPath() + "/index.jsp");

        } catch (AuthException e) {
            preserveFormValues(request, email);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);

        } catch (SQLException e) {
            preserveFormValues(request, email);
            request.setAttribute("error", "Unexpected error. Please try again later.");
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
        }
    }

    private void removeSensitiveData(User user) {
        if (user != null) {
            user.setPasswordHash(null);
        }
    }

    private void preserveFormValues(HttpServletRequest request, String email) {
        request.setAttribute("email", email);
    }
}