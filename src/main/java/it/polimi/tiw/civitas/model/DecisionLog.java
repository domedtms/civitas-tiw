package it.polimi.tiw.civitas.model;

import java.time.LocalDateTime;

public class DecisionLog {

    private int id;
    private int nationId;
    private Integer lawId;
    private Integer actorId;
    private DecisionLogAction action;
    private String description;
    private LocalDateTime createdAt;

    public DecisionLog() {
    }

    public DecisionLog(int id, int nationId, Integer lawId, Integer actorId,
                       DecisionLogAction action, String description, LocalDateTime createdAt) {
        this.id = id;
        this.nationId = nationId;
        this.lawId = lawId;
        this.actorId = actorId;
        this.action = action;
        this.description = description;
        this.createdAt = createdAt;
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


    public Integer getLawId() {
        return lawId;
    }

    public void setLawId(Integer lawId) {
        this.lawId = lawId;
    }


    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }


    public DecisionLogAction getAction() {
        return action;
    }

    public void setAction(DecisionLogAction action) {
        this.action = action;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}