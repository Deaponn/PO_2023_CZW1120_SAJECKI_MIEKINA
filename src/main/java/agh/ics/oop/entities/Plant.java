package agh.ics.oop.entities;

import agh.ics.oop.model.EnergyHolder;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.render.AssignRenderer;
import agh.ics.oop.render.renderer.PlantRenderer;

@AssignRenderer(renderer = PlantRenderer.class)
public class Plant extends WorldElement implements EnergyHolder {
    private int energy;

    public Plant(Vector2D position, int energy) {
        super(position);
        this.energy = energy;
    }

    @Override
    public int drainEnergy(int energy) {
        int drainedEnergy = Math.min(energy, this.energy);
        this.energy -= drainedEnergy;
        return drainedEnergy;
    }

    @Override
    public int supplyEnergy(EnergyHolder energyHolder, int energy) {
        int gainedEnergy = energyHolder.drainEnergy(energy);
        this.energy += gainedEnergy;
        return gainedEnergy;
    }

    @Override
    public int getEnergy() { return this.energy; }
}
