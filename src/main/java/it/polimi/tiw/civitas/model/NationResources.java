package it.polimi.tiw.civitas.model;

public class NationResources {

    private int nationId;
    private int coins;
    private int culturePoints;
    private int energyPoints;

    public NationResources() {
    }

    public NationResources(int nationId, int coins, int culturePoints, int energyPoints) {
        this.nationId = nationId;
        this.coins = coins;
        this.culturePoints = culturePoints;
        this.energyPoints = energyPoints;
    }

    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
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
}