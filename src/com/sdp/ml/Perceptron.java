package com.sdp.ml;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Julia on 05.07.2018.
 */
public class Perceptron implements Serializable {
    private Map<String, Double> weights;

    public Perceptron() {
        weights = new HashMap<String, Double>();
    }

    private Double getWeight(String feature) {
        if(weights.containsKey(feature))
            return weights.get(feature);
        else
            return 0.0;
    }

    public double getScore(List<String> features) {
        double score = 0.0;
        for(String feature : features) {
            score += getWeight(feature);
        }
        return score;
    }

    public void increase(List<String> features) {
        for(String feature: features) {
            if(weights.containsKey(feature))
                weights.put(feature, weights.get(feature) + 1.0);
            else
                weights.put(feature, 1.0);
        }
    }

    public void decrease(List<String> features) {
        for(String feature: features) {
            if(weights.containsKey(feature))
                weights.put(feature, weights.get(feature) - 1.0);
            else
                weights.put(feature, -1.0);
        }
    }
}
