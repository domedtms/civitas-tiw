package it.polimi.tiw.civitas.model;

import java.time.LocalDateTime;

public class Citizen {

    private int userId;
    private String username;
    private MembershipRole role;
    private LocalDateTime joinedAt;

    public Citizen() {
    }

    public Citizen(int userId, String username, MembershipRole role, LocalDateTime joinedAt) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MembershipRole getRole() {
        return role;
    }

    public void setRole(MembershipRole role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}