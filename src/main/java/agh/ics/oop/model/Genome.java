package agh.ics.oop.model;

import java.util.List;
import java.util.Random;

public class Genome {
    // TODO
    private final float randomGenomeChangeChance;
    private final List<MoveDirection> geneList;
    private int activeGene;

    public Genome(float randomGenomeChangeChance, List<MoveDirection> geneList) {
        this.randomGenomeChangeChance = randomGenomeChangeChance;
        this.geneList = geneList;
        this.activeGene = Genome.random.nextInt(this.geneList.size());
    }

    public MoveDirection getGeneAt(int index) { return this.geneList.get(index); }

    public MoveDirection getActiveGene() {
        return this.geneList.get(this.activeGene);
    }

    public int getActiveGeneIndex() {
        return this.activeGene;
    }

    public void updateActiveGene() {
        if (Genome.random.nextDouble() < this.randomGenomeChangeChance) {
            this.activeGene = Genome.random.nextInt(this.geneList.size());
        } else {
            this.activeGene = (this.activeGene + 1) % this.geneList.size();
        }
    }

    public String toString() {
        return this.geneList.toString();
    }

    private static final Random random = new Random();
}
