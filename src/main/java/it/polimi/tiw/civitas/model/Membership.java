package it.polimi.tiw.civitas.model;

import java.time.LocalDateTime;

public class Membership {

    private int id;
    private int userId;
    private int nationId;
    private MembershipRole role;
    private LocalDateTime joinedAt;

    public Membership() {
    }

    public Membership(int id, int userId, int nationId, MembershipRole role, LocalDateTime joinedAt) {
        this.id = id;
        this.userId = userId;
        this.nationId = nationId;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    
    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
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