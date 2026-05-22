package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.AnnouncementDAO;
import it.polimi.tiw.civitas.dao.MembershipDAO;
import it.polimi.tiw.civitas.dao.NationDAO;
import it.polimi.tiw.civitas.model.Announcement;
import it.polimi.tiw.civitas.model.MembershipRole;
import it.polimi.tiw.civitas.dao.DecisionLogDAO;
import it.polimi.tiw.civitas.dao.NationResourceDAO;
import it.polimi.tiw.civitas.model.DecisionLog;
import it.polimi.tiw.civitas.model.DecisionLogAction;
import it.polimi.tiw.civitas.util.ConnectionHandler;

import java.sql.Connection;

import java.sql.SQLException;

public class AnnouncementService {

    private static final int MIN_TITLE_LENGTH = 3;
    private static final int MAX_TITLE_LENGTH = 120;
    private static final int MIN_CONTENT_LENGTH = 10;
    private static final int MAX_CONTENT_LENGTH = 5000;

    private final AnnouncementDAO announcementDAO;
    private final MembershipDAO membershipDAO;
    private final NationDAO nationDAO;
    private final NationResourceDAO nationResourceDAO;
    private final DecisionLogDAO decisionLogDAO;

    public AnnouncementService() {
        this.announcementDAO = new AnnouncementDAO();
        this.membershipDAO = new MembershipDAO();
        this.nationDAO = new NationDAO();
        this.nationResourceDAO = new NationResourceDAO();
        this.decisionLogDAO = new DecisionLogDAO();
    }

    public int createAnnouncement(int nationId, int authorId, String title, String content)
            throws SQLException, AnnouncementException {

        String normalizedTitle = normalize(title);
        String normalizedContent = normalize(content);

        validateInput(nationId, authorId, normalizedTitle, normalizedContent);

        if (nationDAO.findById(nationId).isEmpty()) {
            throw new AnnouncementException("Nation not found.");
        }

        boolean authorized = canCreateAnnouncement(authorId, nationId);

        if (!authorized) {
            throw new AnnouncementException("You are not authorized to publish official announcements.");
        }

        Announcement announcement = new Announcement();
        announcement.setNationId(nationId);
        announcement.setAuthorId(authorId);
        announcement.setTitle(normalizedTitle);
        announcement.setContent(normalizedContent);

        try (Connection connection = ConnectionHandler.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                int announcementId = announcementDAO.create(connection, announcement);

                nationResourceDAO.incrementResources(connection, nationId, 3, 2, 0);

                DecisionLog resourceLog = new DecisionLog();
                resourceLog.setNationId(nationId);
                resourceLog.setLawId(null);
                resourceLog.setActorId(authorId);
                resourceLog.setAction(DecisionLogAction.RESOURCE_UPDATED);
                resourceLog.setDescription("Resources updated after official announcement: coins +3, culture +2.");

                decisionLogDAO.create(connection, resourceLog);

                connection.commit();
                return announcementId;

            } catch (SQLException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    public boolean canCreateAnnouncement(int userId, int nationId) throws SQLException {
        if (userId <= 0 || nationId <= 0) {
            return false;
        }

        return membershipDAO.hasAnyRole(
                userId,
                nationId,
                MembershipRole.FOUNDER,
                MembershipRole.MINISTER
        );
    }

    private void validateInput(int nationId, int authorId, String title, String content)
            throws AnnouncementException {

        if (nationId <= 0) {
            throw new AnnouncementException("Invalid nation.");
        }

        if (authorId <= 0) {
            throw new AnnouncementException("Invalid author.");
        }

        if (isBlank(title) || title.length() < MIN_TITLE_LENGTH || title.length() > MAX_TITLE_LENGTH) {
            throw new AnnouncementException("Title must contain between 3 and 120 characters.");
        }

        if (isBlank(content) || content.length() < MIN_CONTENT_LENGTH || content.length() > MAX_CONTENT_LENGTH) {
            throw new AnnouncementException("Content must contain between 10 and 5000 characters.");
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}