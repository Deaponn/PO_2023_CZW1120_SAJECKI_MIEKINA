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
        this.activeGene = 0;
    }

    public MoveDirection getGeneAt(int index) { return this.geneList.get(index); }

    public MoveDirection getActiveGene() {
        return this.geneList.get(this.activeGene);
    }

    public void updateActiveGene() {
        if (Genome.random.nextDouble() < this.randomGenomeChangeChance) {
            this.activeGene = Genome.random.nextInt(this.geneList.size());
        } else {
            this.activeGene = (this.activeGene + 1) % this.geneList.size();
        }
    }

    private static final Random random = new Random();
}
