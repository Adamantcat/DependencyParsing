package com.sdp.ml;

import com.sdp.parsing.Oracle;
import com.sdp.parsing.Parser;
import com.sdp.parsing.TransitionParser;
import com.sdp.util.Corpus;
import com.sdp.util.Tree;

import java.util.*;

/**
 * Created by Julia on 05.07.2018.
 */
public class MulticlassPerceptron {

    private Map<String, Perceptron> perceptrons;
    private Oracle oracle;

    public MulticlassPerceptron() {

        this.perceptrons = new HashMap<String, Perceptron>();
        this.oracle = new Oracle();

        for (String transition : Parser.transitions) {
            perceptrons.put(transition, new Perceptron());
        }
    }

    //return the scores for the transitions
    public Map<String, Double> getScores(List<String> features) {
        //System.out.println("LOGGING: getScores");
        Map<String, Double> scores = new HashMap<String, Double>();

        for (String transition : perceptrons.keySet()) {
            Perceptron perceptron = perceptrons.get(transition);
            scores.put(transition, perceptron.getScore(features));
        }

        return scores;
    }

    private String predictTransition(List<String> features) {
        //System.out.println("LOGGING: predictTransition");
        List<Map.Entry<String, Double>> orderedScores = new ArrayList(getScores(features).entrySet());
        Collections.sort(orderedScores, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        return orderedScores.get(0).getKey();
    }

    public void predictTree(Tree tree) {
        //System.out.println("LOGGING: predictTree");
        tree.getTokens().forEach(t -> System.out.print(t.getForm() + " "));
        System.out.println();
        List<String> features;
        oracle.init(tree);
        while (!oracle.isTerminal()) {
            features = FeatureExtraction.extractFeatures(oracle);
            String predicted = predictTransition(features);
            String correctTransition = oracle.nextTransition(tree); //return which transition should be done and do the transition
            if (!predicted.equals(correctTransition)) {
                //update perceptrons
                perceptrons.get(predicted).decrease(features);
                perceptrons.get(correctTransition).increase(features);
            }
        }
    }

    public void train (Corpus corpus) {
        for(Tree tree : corpus.getTrees()) {
            tree.clear();
            predictTree(tree);
        }
    }




}
