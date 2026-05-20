package it.polimi.tiw.civitas.controller.nation;

import it.polimi.tiw.civitas.dao.AnnouncementDAO;
import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.model.Announcement;
import it.polimi.tiw.civitas.model.Citizen;
import it.polimi.tiw.civitas.model.MembershipRole;
import it.polimi.tiw.civitas.model.Nation;
import it.polimi.tiw.civitas.model.User;

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

@WebServlet("/nation")
public class NationDetailServlet extends HttpServlet {

    private static final String NATION_DETAIL_VIEW = "/WEB-INF/views/nation/nation-detail.jsp";
    private static final String SESSION_USER = "loggedUser";

    private NationDAO nationDAO;
    private MembershipDAO membershipDAO;
    private AnnouncementDAO announcementDAO;

    @Override
    public void init() {
        this.nationDAO = new NationDAO();
        this.membershipDAO = new MembershipDAO();
        this.announcementDAO = new AnnouncementDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer nationId = parseId(request.getParameter("id"));

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

            List<Citizen> citizens = membershipDAO.findCitizensByNationId(nationId);
            List<Announcement> announcements = announcementDAO.findByNationId(nationId);

            User loggedUser = getLoggedUser(request);

            boolean currentUserMember = false;
            boolean canCreateAnnouncement = false;

            if (loggedUser != null) {
                Optional<MembershipRole> roleOptional =
                        membershipDAO.findRoleByUserAndNation(loggedUser.getId(), nationId);

                currentUserMember = roleOptional.isPresent();

                canCreateAnnouncement = roleOptional
                        .map(role -> role == MembershipRole.FOUNDER || role == MembershipRole.MINISTER)
                        .orElse(false);
            }

            request.setAttribute("nation", nationOptional.get());
            request.setAttribute("citizens", citizens);
            request.setAttribute("announcements", announcements);
            request.setAttribute("currentUserMember", currentUserMember);
            request.setAttribute("canCreateAnnouncement", canCreateAnnouncement);
            request.setAttribute("joinError", request.getParameter("joinError"));

            request.getRequestDispatcher(NATION_DETAIL_VIEW).forward(request, response);

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