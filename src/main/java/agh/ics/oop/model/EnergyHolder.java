package agh.ics.oop.model;

public interface EnergyHolder {
    int drainEnergy(int energy);
    int supplyEnergy(EnergyHolder energyHolder, int energy);
    int getEnergy();
}
