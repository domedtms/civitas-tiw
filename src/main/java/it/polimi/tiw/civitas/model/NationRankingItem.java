package it.polimi.tiw.civitas.model;

public class NationRankingItem {

    private int nationId;
    private String nationName;
    private String flagSymbol;
    private int citizensCount;
    private int approvedLawsCount;
    private int coins;
    private int culturePoints;
    private int energyPoints;
    private int score;

    public NationRankingItem() {
    }

    public NationRankingItem(int nationId, String nationName, String flagSymbol,
                             int citizensCount, int approvedLawsCount,
                             int coins, int culturePoints, int energyPoints, int score) {
        this.nationId = nationId;
        this.nationName = nationName;
        this.flagSymbol = flagSymbol;
        this.citizensCount = citizensCount;
        this.approvedLawsCount = approvedLawsCount;
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


    public int getApprovedLawsCount() {
        return approvedLawsCount;
    }

    public void setApprovedLawsCount(int approvedLawsCount) {
        this.approvedLawsCount = approvedLawsCount;
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