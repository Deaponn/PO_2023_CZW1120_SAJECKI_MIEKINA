package agh.ics.oop;

import agh.ics.oop.model.MapType;

public record Configuration(int mapWidth,
                            int mapHeight,
                            // type of the map, project requirement F
                            MapType mapType,
                            int genomeLength,
                            // if set to 0, simulation is standard,
                            // if set to 0.2, it's "a bit of craziness", project requirement 3
                            float randomGenomeChangeChance,
                            int startingPlantsNumber,
                            int plantEnergy,
                            int numberOfGrowingPlants,
                            int startingAnimalsNumber,
                            int requiredReproductionEnergy,
                            // TODO: percentage or integer amount?
                            //      if percentage, should the max be 200%?
                            //      (for both parents to give out all the energy)
                            int energyPassedToChild,
                            int minMutationsNumber,
                            int maxMutationsNumber,
                            // default is 0.2, according to Pareto rule
                            float equatorSize,
                            // default is 0.8, according to Pareto rule
                            float plantGrowAtEquatorChance,
                            // saving to external file
                            boolean saveSteps
                            ) {}
