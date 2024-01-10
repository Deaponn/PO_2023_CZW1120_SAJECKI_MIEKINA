package agh.ics.oop.entities;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.Genome;
import agh.ics.oop.model.GenomeFactory;
import agh.ics.oop.model.Vector2D;

import java.util.Random;

import static agh.ics.oop.Configuration.Fields.*;

public class AnimalFactory {
    private final int minReproduceEnergy;
    private final int energyPassed;
    private final int minMutations;
    private final int maxMutations;

    private final GenomeFactory genomeFactory;

    public AnimalFactory(Configuration configuration, GenomeFactory genomeFactory) {
        this.minReproduceEnergy = configuration.get(MIN_REPRODUCE_ENERGY);
        this.energyPassed = configuration.get(ENERGY_PASSED);
        this.minMutations = configuration.get(MIN_MUTATIONS);
        this.maxMutations = configuration.get(MAX_MUTATIONS);

        this.genomeFactory = genomeFactory;
    }

    public Animal create(Vector2D position, int energy) {
        int mutations = AnimalFactory.random.nextInt(
                this.minMutations,
                this.maxMutations);
        Genome genome = this.genomeFactory.create(mutations);
        return new Animal(position, this.minReproduceEnergy, this.energyPassed, energy, genome);
    }

    private static final Random random = new Random();
}
