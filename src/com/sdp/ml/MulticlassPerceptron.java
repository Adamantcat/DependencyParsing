package com.sdp.ml;

import com.sdp.evaluation.Evaluation;
import com.sdp.parsing.Oracle;
import com.sdp.parsing.Parser;
import com.sdp.parsing.TransitionParser;
import com.sdp.util.Corpus;
import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Course: Statistical Dependency Parsing, SS 2018
 * Author: Julia Koch
 * Class Description: Statistical model for dependency parsing. Implementation of a multiclass perceptron
 */
public class MulticlassPerceptron implements Serializable{

    private Map<String, Perceptron> perceptrons; //perceptrons to predict transitions, one perceptron for each transition
    private Map<String, Perceptron> labelPerceptrons;//perceprtons to predict labels, one perceptron for each label
    private Oracle oracle; //Oracle to give correct transitions from gold trees

    //constructor for unlabeled parsing
    public MulticlassPerceptron() {

        this.perceptrons = new HashMap<String, Perceptron>();
        this.oracle = new Oracle();

        for (String transition : Parser.transitions) {
            perceptrons.put(transition, new Perceptron());
        }
    }

    //constructor for labeled parsing
    //possible labels are the labels in the tagset
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
        Map<String, Double> scores = new HashMap<String, Double>();

        for (String transition : perceptrons.keySet()) {
            Perceptron perceptron = perceptrons.get(transition);
            scores.put(transition, perceptron.getScore(features));
        }
        return scores;
    }

    //predict a label for an arc, given a set of features
    //return highest scoring label
    public String getLabel(List<String> labelFeatures) {
        double max = 0;
        String predicted = "_";
        for (String label : labelPerceptrons.keySet()) {
            if (labelPerceptrons.get(label).getScore(labelFeatures) > max)
                predicted = label;
        }
        return predicted;
    }

    //predict a transition given the list of features of a configuration
    //return only highest scoring transition
    private String predictTransition(List<String> features) {
        List<Map.Entry<String, Double>> orderedScores = new ArrayList(getScores(features).entrySet());
        Collections.sort(orderedScores, (o1, o2) -> o2.getValue().compareTo(o1.getValue())); //sort by score
        return orderedScores.get(0).getKey();
    }

    //train transitions on one tree
    private void predictTree(Tree tree) {
        tree.clear(); //reset tree
        List<String> features;
        oracle.init(tree);
        while (!oracle.isTerminal()) {
            features = FeatureExtraction.extractFeatures(oracle, tree); //extract features for current configuration
            String predicted = predictTransition(features); //predict a transition
            //ask oracle which transition should be done and do the transition right away
            String correctTransition = oracle.nextTransition(tree);
            //if prediction was wrong, update weigths of involved perceptrons
            if (!predicted.equals(correctTransition)) {
                //update perceptrons
                perceptrons.get(predicted).decrease(features);
                perceptrons.get(correctTransition).increase(features);
            }
        }
    }

    //train label for one token
    private void predictLabel(Token token, Tree tree) {
        List<String> labelFeatures = FeatureExtraction.extractLabelFeatures(token, tree);
        String predicted = getLabel(labelFeatures); //predict a label
        token.setRel(predicted); //set rel of token to predicted label

        //if prediction was wrong, update involved perceptrons
        if (!predicted.equals(token.getGoldRel())) {
            labelPerceptrons.get(predicted).decrease(labelFeatures);
            labelPerceptrons.get(token.getGoldRel()).increase(labelFeatures);
        }

    }

    //train labels on one tree
    private void predictLabels(Tree tree) {
        for (Token token : tree.getTokens()) {
            predictLabel(token, tree);
        }
    }

    //train transition and labels on whole corpus with given number of iterations over corpus
    public void train(Corpus corpus, int iterations) {
        for (int i = 1; i <= iterations; i++) {
            System.out.println("Iteration " + i);
            List<Tree> trees = corpus.getTrees();
            Collections.shuffle(trees); //random shuffle the trees before each iteration
            for (Tree tree : trees) {
                predictTree(tree);
                if (labelPerceptrons != null) { //only predict labels if label classifiers are initialized
                    predictLabels(tree);
                }
            }
        }
    }

    //save this model as object to file
    //should be done after training
    public void save(String filename) throws IOException{
        File output = new File(filename); //output file
        output.getParentFile().mkdirs();
        ObjectOutputStream oos = new ObjectOutputStream(
                new GZIPOutputStream(new FileOutputStream(output)));
        oos.writeObject(this);
        oos.close();
        System.out.println("saved model to " + filename);
    }

    //read previously trainied model from file
    public MulticlassPerceptron read(String filename) throws IOException, ClassNotFoundException{
        File input = new File(filename);
        ObjectInputStream ois = new ObjectInputStream(new
                GZIPInputStream(new FileInputStream(input)));
        MulticlassPerceptron model = (MulticlassPerceptron) ois.readObject();
        ois.close();
        return model;
    }

}
