package it.polimi.tiw.civitas.controller.nation;

import it.polimi.tiw.civitas.model.User;
import it.polimi.tiw.civitas.service.NationException;
import it.polimi.tiw.civitas.service.NationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/nations/create")
public class CreateNationServlet extends HttpServlet {

    private static final String CREATE_NATION_VIEW = "/WEB-INF/views/nation/create-nation.jsp";
    private static final String SESSION_USER = "loggedUser";

    private NationService nationService;

    @Override
    public void init() {
        this.nationService = new NationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = getLoggedUser(request);

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher(CREATE_NATION_VIEW).forward(request, response);
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

        String name = request.getParameter("name");
        String motto = request.getParameter("motto");
        String description = request.getParameter("description");
        String flagSymbol = request.getParameter("flagSymbol");

        try {
            int nationId = nationService.createNation(
                    name,
                    motto,
                    description,
                    flagSymbol,
                    loggedUser.getId()
            );

            response.sendRedirect(request.getContextPath() + "/nation?id=" + nationId);

        } catch (NationException e) {
            preserveFormValues(request, name, motto, description, flagSymbol);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(CREATE_NATION_VIEW).forward(request, response);

        } catch (SQLException e) {
            preserveFormValues(request, name, motto, description, flagSymbol);
            request.setAttribute("error", "Errore inatteso. Riprova più tardi.");
            request.getRequestDispatcher(CREATE_NATION_VIEW).forward(request, response);
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

    private void preserveFormValues(HttpServletRequest request, String name, String motto,
                                    String description, String flagSymbol) {
        request.setAttribute("name", name);
        request.setAttribute("motto", motto);
        request.setAttribute("description", description);
        request.setAttribute("flagSymbol", flagSymbol);
    }
}
