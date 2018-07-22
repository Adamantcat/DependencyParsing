package com.sdp.ml;

import com.sdp.evaluation.Evaluation;
import com.sdp.parsing.Oracle;
import com.sdp.parsing.Parser;
import com.sdp.parsing.TransitionParser;
import com.sdp.util.Corpus;
import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.util.*;

/**
 * Created by Julia on 05.07.2018.
 */
public class MulticlassPerceptron {

    private Map<String, Perceptron> perceptrons;
    private Map<String, Perceptron> labelPerceptrons;
    private Oracle oracle;

    public MulticlassPerceptron() {

        this.perceptrons = new HashMap<String, Perceptron>();
        this.oracle = new Oracle();

        for (String transition : Parser.transitions) {
            perceptrons.put(transition, new Perceptron());
        }
    }

    public MulticlassPerceptron(Set<String> tagset) {

        this.perceptrons = new HashMap<String, Perceptron>();
        this.labelPerceptrons = new HashMap<String, Perceptron>();
        this.oracle = new Oracle();

        for (String transition : Parser.transitions) {
            perceptrons.put(transition, new Perceptron());
        }
        for (String label : tagset) {
            labelPerceptrons.put(label, new Perceptron());
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

    public String getLabel(List<String> labelFeatures) {
        double max = 0;
        String predicted = "_";
        for (String label : labelPerceptrons.keySet()) {
            if (labelPerceptrons.get(label).getScore(labelFeatures) > max)
                predicted = label;
        }
        return predicted;
    }

    private String predictTransition(List<String> features) {
        //System.out.println("LOGGING: predictTransition");
        List<Map.Entry<String, Double>> orderedScores = new ArrayList(getScores(features).entrySet());
        Collections.sort(orderedScores, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        return orderedScores.get(0).getKey();
    }

    private void predictTree(Tree tree) {
        //System.out.println("LOGGING: predictTree");
        tree.clear();
        List<String> features;
        oracle.init(tree);
        while (!oracle.isTerminal()) {
            features = FeatureExtraction.extractFeatures(oracle, tree);
            String predicted = predictTransition(features);
            String correctTransition = oracle.nextTransition(tree); //return which transition should be done and do the transition
            if (!predicted.equals(correctTransition)) {
                //update perceptrons
                perceptrons.get(predicted).decrease(features);
                perceptrons.get(correctTransition).increase(features);
            }
        }
    }

    private void predictLabel(Token token, Tree tree) {
        List<String> labelFeatures = FeatureExtraction.extractLabelFeatures(token, tree);
        String predicted = getLabel(labelFeatures);

        token.setRel(predicted);

        if (!predicted.equals(token.getGoldRel())) {
            labelPerceptrons.get(predicted).decrease(labelFeatures);
            labelPerceptrons.get(token.getGoldRel()).increase(labelFeatures);
        }

    }

    public void predictLabels(Tree tree) {
        for (Token token : tree.getTokens()) {
            predictLabel(token, tree);
        }
    }

    public void train(Corpus corpus, int iterations) {
        for (int i = 1; i <= iterations; i++) {
            System.out.println("Iteration " + i);
            List<Tree> trees = corpus.getTrees();
            Collections.shuffle(trees);
            for (Tree tree : trees) {
                predictTree(tree);
                if (labelPerceptrons != null) {
                    predictLabels(tree);
                }
            }
        }
    }

}
