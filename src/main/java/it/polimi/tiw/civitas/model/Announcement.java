package it.polimi.tiw.civitas.model;

import java.time.LocalDateTime;

public class Announcement {

    private int id;
    private int nationId;
    private int authorId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public Announcement() {
    }

    public Announcement(int id, int nationId, int authorId, String title,
                        String content, LocalDateTime createdAt) {
        this.id = id;
        this.nationId = nationId;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
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

    
    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}