package org.subterra.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DistributedRNG {

    private Map<Integer, Double> distribution;
    private double distSum;
    private Random randgen;

    public DistributedRNG() {
        distribution = new HashMap<>();
        randgen = new Random(System.currentTimeMillis() / 1000L);
    }

    public void addNumber(int value, double distribution) {
        if (this.distribution.get(value) != null) {
            distSum -= this.distribution.get(value);
        }
        this.distribution.put(value, distribution);
        distSum += distribution;
    }

    public int getNumber() {
        double rand = randgen.nextDouble();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (Integer i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return 0;
    }

}
