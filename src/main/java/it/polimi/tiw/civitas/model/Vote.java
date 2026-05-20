package it.polimi.tiw.civitas.model;

import java.time.LocalDateTime;

public class Vote {

    private int id;
    private int lawId;
    private int userId;
    private VoteValue voteValue;
    private LocalDateTime createdAt;

    public Vote() {
    }

    public Vote(int id, int lawId, int userId, VoteValue voteValue, LocalDateTime createdAt) {
        this.id = id;
        this.lawId = lawId;
        this.userId = userId;
        this.voteValue = voteValue;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getLawId() {
        return lawId;
    }

    public void setLawId(int lawId) {
        this.lawId = lawId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public VoteValue getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(VoteValue voteValue) {
        this.voteValue = voteValue;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}