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
    private final GenomeFactory genomeFactory;

    public AnimalFactory(Configuration configuration, GenomeFactory genomeFactory) {
        this.minReproduceEnergy = configuration.get(MIN_REPRODUCE_ENERGY);
        this.energyPassed = configuration.get(ENERGY_PASSED);

        this.genomeFactory = genomeFactory;
    }

    public Animal create(Vector2D position, int energy) {
        return this.create(position, energy, genomeFactory.randomGenome());
    }

    public Animal create(Vector2D position, int energy, Genome genome) {
        return new Animal(position, energy, genome);
    }

    // assumes that both Animal objects are at the same position
    public boolean canBreed(Animal firstParent, Animal secondParent) {
        return firstParent.getEnergy() >= this.minReproduceEnergy && secondParent.getEnergy() >= this.minReproduceEnergy;
    }

    // assumes that both Animal objects are at the same position
    public Animal breedAnimals(Animal firstParent, Animal secondParent) {
        Animal strongerParent;
        Animal weakerParent;
        if (Animal.compare(firstParent, secondParent)) {
            strongerParent = firstParent;
            weakerParent = secondParent;
        } else {
            strongerParent = secondParent;
            weakerParent = firstParent;
        }
        float proportion = strongerParent.getEnergy() / (float)(strongerParent.getEnergy() + weakerParent.getEnergy());
        Genome kidGenome = this.genomeFactory.genomeFrom(strongerParent.getGenome(), weakerParent.getGenome(), proportion);
        return this.create(strongerParent.getPosition(), 2 * this.energyPassed, kidGenome);
    }

    private static final Random random = new Random();
}
