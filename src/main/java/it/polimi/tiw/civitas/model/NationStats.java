package it.polimi.tiw.civitas.model;

public class NationStats {

    private int nationId;
    private String nationName;
    private String flagSymbol;

    private int citizensCount;
    private int foundersCount;
    private int ministersCount;
    private int regularCitizensCount;

    private int lawsCount;
    private int proposedLawsCount;
    private int approvedLawsCount;
    private int rejectedLawsCount;
    private int repealedLawsCount;

    private int announcementsCount;
    private int decisionEventsCount;

    private int coins;
    private int culturePoints;
    private int energyPoints;

    private int score;

    public NationStats() {
    }

    public NationStats(int nationId, String nationName, String flagSymbol,
                       int citizensCount, int foundersCount, int ministersCount, int regularCitizensCount,
                       int lawsCount, int proposedLawsCount, int approvedLawsCount,
                       int rejectedLawsCount, int repealedLawsCount,
                       int announcementsCount, int decisionEventsCount,
                       int coins, int culturePoints, int energyPoints, int score) {
        this.nationId = nationId;
        this.nationName = nationName;
        this.flagSymbol = flagSymbol;
        this.citizensCount = citizensCount;
        this.foundersCount = foundersCount;
        this.ministersCount = ministersCount;
        this.regularCitizensCount = regularCitizensCount;
        this.lawsCount = lawsCount;
        this.proposedLawsCount = proposedLawsCount;
        this.approvedLawsCount = approvedLawsCount;
        this.rejectedLawsCount = rejectedLawsCount;
        this.repealedLawsCount = repealedLawsCount;
        this.announcementsCount = announcementsCount;
        this.decisionEventsCount = decisionEventsCount;
        this.coins = coins;
        this.culturePoints = culturePoints;
        this.energyPoints = energyPoints;
        this.score = score;
    }

    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getFlagSymbol() {
        return flagSymbol;
    }

    public void setFlagSymbol(String flagSymbol) {
        this.flagSymbol = flagSymbol;
    }

    public int getCitizensCount() {
        return citizensCount;
    }

    public void setCitizensCount(int citizensCount) {
        this.citizensCount = citizensCount;
    }

    public int getFoundersCount() {
        return foundersCount;
    }

    public void setFoundersCount(int foundersCount) {
        this.foundersCount = foundersCount;
    }

    public int getMinistersCount() {
        return ministersCount;
    }

    public void setMinistersCount(int ministersCount) {
        this.ministersCount = ministersCount;
    }

    public int getRegularCitizensCount() {
        return regularCitizensCount;
    }

    public void setRegularCitizensCount(int regularCitizensCount) {
        this.regularCitizensCount = regularCitizensCount;
    }

    public int getLawsCount() {
        return lawsCount;
    }

    public void setLawsCount(int lawsCount) {
        this.lawsCount = lawsCount;
    }

    public int getProposedLawsCount() {
        return proposedLawsCount;
    }

    public void setProposedLawsCount(int proposedLawsCount) {
        this.proposedLawsCount = proposedLawsCount;
    }

    public int getApprovedLawsCount() {
        return approvedLawsCount;
    }

    public void setApprovedLawsCount(int approvedLawsCount) {
        this.approvedLawsCount = approvedLawsCount;
    }

    public int getRejectedLawsCount() {
        return rejectedLawsCount;
    }

    public void setRejectedLawsCount(int rejectedLawsCount) {
        this.rejectedLawsCount = rejectedLawsCount;
    }

    public int getRepealedLawsCount() {
        return repealedLawsCount;
    }

    public void setRepealedLawsCount(int repealedLawsCount) {
        this.repealedLawsCount = repealedLawsCount;
    }

    public int getAnnouncementsCount() {
        return announcementsCount;
    }

    public void setAnnouncementsCount(int announcementsCount) {
        this.announcementsCount = announcementsCount;
    }

    public int getDecisionEventsCount() {
        return decisionEventsCount;
    }

    public void setDecisionEventsCount(int decisionEventsCount) {
        this.decisionEventsCount = decisionEventsCount;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getCulturePoints() {
        return culturePoints;
    }

    public void setCulturePoints(int culturePoints) {
        this.culturePoints = culturePoints;
    }

    public int getEnergyPoints() {
        return energyPoints;
    }

    public void setEnergyPoints(int energyPoints) {
        this.energyPoints = energyPoints;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}