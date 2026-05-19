package it.polimi.tiw.civitas.model;

import java.time.LocalDateTime;

public class Nation {

    private int id;
    private String name;
    private String motto;
    private String description;
    private String flagSymbol;
    private int founderId;
    private LocalDateTime createdAt;

    public Nation() {
    }

    public Nation(int id, String name, String motto, String description, String flagSymbol,
                  int founderId, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.motto = motto;
        this.description = description;
        this.flagSymbol = flagSymbol;
        this.founderId = founderId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public String getFlagSymbol() {
        return flagSymbol;
    }

    public void setFlagSymbol(String flagSymbol) {
        this.flagSymbol = flagSymbol;
    }

    
    public int getFounderId() {
        return founderId;
    }

    public void setFounderId(int founderId) {
        this.founderId = founderId;
    }

    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}