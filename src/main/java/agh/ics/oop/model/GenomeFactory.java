package agh.ics.oop.model;

import agh.ics.oop.Configuration;

import java.util.List;
import java.util.Random;

import static agh.ics.oop.Configuration.Fields.GENOME_LENGTH;
import static agh.ics.oop.Configuration.Fields.RANDOM_GENOME_CHANGE_CHANCE;

public class GenomeFactory {
    private final int genomeLength;
    private final float randomGenomeChangeChance;

    public GenomeFactory(Configuration configuration) {
        this.genomeLength = configuration.get(GENOME_LENGTH);
        this.randomGenomeChangeChance = configuration.get(RANDOM_GENOME_CHANGE_CHANCE);
    }

    public Genome create(int mutations) {
        // TODO
        return new Genome(this.randomGenomeChangeChance, List.of());
    }

    private static final Random random = new Random();
}
