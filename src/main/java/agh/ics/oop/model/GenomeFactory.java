package agh.ics.oop.model;

import agh.ics.oop.Configuration;
import agh.ics.oop.util.RandomNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static agh.ics.oop.Configuration.Fields.*;

public class GenomeFactory {
    private final int genomeLength;
    private final int minMutations;
    private final int maxMutations;
    private final float randomGenomeChangeChance;
    private final RandomNumber randomNoRepeats;

    public GenomeFactory(Configuration configuration) {
        this.genomeLength = configuration.get(GENOME_LENGTH);
        this.minMutations = configuration.get(MIN_MUTATIONS);
        this.maxMutations = configuration.get(MAX_MUTATIONS);
        this.randomGenomeChangeChance = configuration.get(RANDOM_GENOME_CHANGE_CHANCE);
        this.randomNoRepeats = new RandomNumber(0, genomeLength);
    }

    public Genome genomeFrom(Genome stronger, Genome weaker, float proportion) {
        int strongerContribution = (int) (this.genomeLength * proportion);
        int weakerContribution = this.genomeLength - strongerContribution;

        int strongerOffset = random.nextInt(2) * weakerContribution;
        int weakerOffset = strongerOffset == 0 ? strongerContribution : 0;

        List<MoveDirection> geneList = new ArrayList<>(this.genomeLength);
        for (int i = 0; i < this.genomeLength; i++) geneList.add(null);
        for (int i = strongerOffset; i < strongerOffset + strongerContribution; i++) {
            geneList.set(i, stronger.getGeneAt(i));
        }
        for (int i = weakerOffset; i < weakerOffset + weakerContribution; i++) {
            geneList.set(i, weaker.getGeneAt(i));
        }

        int mutations = random.nextInt(minMutations, maxMutations + 1);
        randomNoRepeats.refreshRange();
        for (int i = 0; i < mutations; i++) {
            geneList.set(randomNoRepeats.next(), MoveDirection.randomDirection());
        }

        return new Genome(this.randomGenomeChangeChance, geneList);
    }

    public Genome randomGenome() {
        List<MoveDirection> geneList = new ArrayList<>(this.genomeLength);
        for (int i = 0; i < this.genomeLength; i++) {
            geneList.add(MoveDirection.randomDirection());
        }
        return new Genome(this.randomGenomeChangeChance, geneList);
    }

    private static final Random random = new Random();
}
