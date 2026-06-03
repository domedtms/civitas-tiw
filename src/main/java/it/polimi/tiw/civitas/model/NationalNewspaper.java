package it.polimi.tiw.civitas.model;

import java.time.LocalDateTime;

public class NationalNewspaper {

    private int id;
    private int nationId;
    private int generatedBy;
    private String period;
    private String title;
    private String editorial;
    private String politicalSummary;
    private String resourcesSummary;
    private String legislativeSummary;
    private String announcementsSummary;
    private String historicalSummary;
    private LocalDateTime createdAt;

    public NationalNewspaper() {
    }

    public NationalNewspaper(int id, int nationId, int generatedBy, String period,
                             String title, String editorial, String politicalSummary,
                             String resourcesSummary, String legislativeSummary,
                             String announcementsSummary, String historicalSummary,
                             LocalDateTime createdAt) {
        this.id = id;
        this.nationId = nationId;
        this.generatedBy = generatedBy;
        this.period = period;
        this.title = title;
        this.editorial = editorial;
        this.politicalSummary = politicalSummary;
        this.resourcesSummary = resourcesSummary;
        this.legislativeSummary = legislativeSummary;
        this.announcementsSummary = announcementsSummary;
        this.historicalSummary = historicalSummary;
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


    public int getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(int generatedBy) {
        this.generatedBy = generatedBy;
    }


    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }


    public String getPoliticalSummary() {
        return politicalSummary;
    }

    public void setPoliticalSummary(String politicalSummary) {
        this.politicalSummary = politicalSummary;
    }


    public String getResourcesSummary() {
        return resourcesSummary;
    }

    public void setResourcesSummary(String resourcesSummary) {
        this.resourcesSummary = resourcesSummary;
    }


    public String getLegislativeSummary() {
        return legislativeSummary;
    }

    public void setLegislativeSummary(String legislativeSummary) {
        this.legislativeSummary = legislativeSummary;
    }


    public String getAnnouncementsSummary() {
        return announcementsSummary;
    }

    public void setAnnouncementsSummary(String announcementsSummary) {
        this.announcementsSummary = announcementsSummary;
    }


    public String getHistoricalSummary() {
        return historicalSummary;
    }

    public void setHistoricalSummary(String historicalSummary) {
        this.historicalSummary = historicalSummary;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}