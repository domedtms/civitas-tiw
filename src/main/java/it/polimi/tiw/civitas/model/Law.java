package it.polimi.tiw.civitas.model;

import java.time.LocalDateTime;

public class Law {

    private int id;
    private int nationId;
    private int proposerId;
    private String title;
    private String description;
    private LawStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public Law() {
    }

    public Law(int id, int nationId, int proposerId, String title, String description,
               LawStatus status, LocalDateTime createdAt, LocalDateTime closedAt) {
        this.id = id;
        this.nationId = nationId;
        this.proposerId = proposerId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.closedAt = closedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
    }


    public int getProposerId() {
        return proposerId;
    }

    public void setProposerId(int proposerId) {
        this.proposerId = proposerId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public LawStatus getStatus() {
        return status;
    }

    public void setStatus(LawStatus status) {
        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }
}