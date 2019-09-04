package com.sdp.ml;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Course: Statistical Dependency Parsing, SS 2018
 * Author: Julia Koch
 * Class Description: Perceptron for one possible outcome
 */
public class Perceptron implements Serializable {
    private Map<String, Double> weights; //map of features and their weights

    public Perceptron() {
        weights = new HashMap<String, Double>();
    }

    //return weight for a give feature
    private Double getWeight(String feature) {
        if(weights.containsKey(feature))
            return weights.get(feature);
        else
            return 0.0; //return 0, if the feature is not in map
    }

    //calculate score of this outcome for a given list of features
    public double getScore(List<String> features) {
        double score = 0.0;
        for(String feature : features) {
            score += getWeight(feature); //sum of scores for all features in list of features
        }
        return score;
    }

    //increase weights by +1 for all features in a given list of features
    public void increase(List<String> features) {
        for(String feature: features) {
            if(weights.containsKey(feature))
                weights.put(feature, weights.get(feature) + 1.0);
            else
                weights.put(feature, 1.0);
        }
    }

    //decrease weights by -1 for all features in a given list of features
    public void decrease(List<String> features) {
        for(String feature: features) {
            if(weights.containsKey(feature))
                weights.put(feature, weights.get(feature) - 1.0);
            else
                weights.put(feature, -1.0);
        }
    }
}
