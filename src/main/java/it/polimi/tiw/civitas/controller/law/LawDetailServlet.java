package it.polimi.tiw.civitas.controller.law;

import it.polimi.tiw.civitas.dao.LawDAO;
import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.dao.VoteDAO;
import it.polimi.tiw.civitas.model.Law;
import it.polimi.tiw.civitas.model.LawStatus;
import it.polimi.tiw.civitas.model.Nation;
import it.polimi.tiw.civitas.model.User;
import it.polimi.tiw.civitas.model.VoteValue;
import it.polimi.tiw.civitas.service.LawWorkflowService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@WebServlet("/law")
public class LawDetailServlet extends HttpServlet {

    private static final String LAW_DETAIL_VIEW = "/WEB-INF/views/law/law-detail.jsp";
    private static final String SESSION_USER = "loggedUser";

    private LawDAO lawDAO;
    private NationDAO nationDAO;
    private VoteDAO voteDAO;
    private MembershipDAO membershipDAO;
    private LawWorkflowService lawWorkflowService;

    @Override
    public void init() {
        this.lawDAO = new LawDAO();
        this.nationDAO = new NationDAO();
        this.voteDAO = new VoteDAO();
        this.membershipDAO = new MembershipDAO();
        this.lawWorkflowService = new LawWorkflowService();
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

            Map<VoteValue, Integer> voteCounts = voteDAO.countByLawGrouped(lawId);

            User loggedUser = getLoggedUser(request);

            boolean currentUserMember = false;
            boolean alreadyVoted = false;
            boolean canVote = false;

            boolean canCloseLaw = false;

            if (loggedUser != null) {
                canCloseLaw = lawWorkflowService.canCloseLaw(loggedUser.getId(), lawId);
            }

            if (loggedUser != null) {
                currentUserMember = membershipDAO.existsByUserAndNation(loggedUser.getId(), law.getNationId());
                alreadyVoted = voteDAO.existsByLawAndUser(lawId, loggedUser.getId());
                canVote = currentUserMember && !alreadyVoted && law.getStatus() == LawStatus.PROPOSED;
            }

            request.setAttribute("law", law);
            request.setAttribute("nation", nationOptional.get());
            request.setAttribute("voteCounts", voteCounts);
            request.setAttribute("currentUserMember", currentUserMember);
            request.setAttribute("alreadyVoted", alreadyVoted);
            request.setAttribute("canVote", canVote);
            request.setAttribute("voteError", request.getParameter("voteError"));
            request.setAttribute("canCloseLaw", canCloseLaw);
            request.setAttribute("workflowError", request.getParameter("workflowError"));

            request.getRequestDispatcher(LAW_DETAIL_VIEW).forward(request, response);

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