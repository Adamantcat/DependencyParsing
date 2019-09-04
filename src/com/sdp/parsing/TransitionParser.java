package com.sdp.parsing;

import com.sdp.ml.FeatureExtraction;
import com.sdp.ml.MulticlassPerceptron;
import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.util.*;

/**
 * Course: Statistical Dependency Parsing, SS 2018
 * Author: Julia Koch
 * Class Description: Arc-Standard parser
 */
public class TransitionParser extends Parser {

    private MulticlassPerceptron model; //model to predict arcs and labels

    public TransitionParser(MulticlassPerceptron model) {
        super();
        this.model = model;
    }

    //parse a tree
    @Override
    public List<String> parse(Tree tree) {
        List<String> transitions = new ArrayList<String>();
        Map<String, Double> scores;
        init(tree); //prepare stack and buffer for this tree
        //predict arcs for this tree
        while (!isTerminal()) {
            //extract features from current configuration
            List<String> features = FeatureExtraction.extractFeatures(this, tree);
            scores = model.getScores(features); //get scores from model for the distinct transitions

            List<Map.Entry<String, Double>> orderedScores = new ArrayList(scores.entrySet());
            Collections.sort(orderedScores, (o1, o2) -> o2.getValue().compareTo(o1.getValue())); //sort scores

            //try to do highest scoring transition
            String suggestedTransition = orderedScores.get(0).getKey();
            boolean isValid = false;
            int i = 0;
            //check if suggested transition is Valid.
            // When the transition was executed, it must have been valid,
            //since transition method in superclass check for preconditions
            while ((!isValid) && (i < orderedScores.size() - 1)) {
                switch (suggestedTransition) {
                    case Parser.leftArc: {
                        isValid = leftArc(); //try to do left arc
                        break;
                    }
                    case Parser.rightArc:
                        isValid = rightArc(); //try to do right arc
                        break;

                    case Parser.shift:
                        isValid = shift(); //try to do shift
                        break;
                }
                if(isValid)
                    break;
                //if suggested transition was not valid, try next best transition
                i++;
                suggestedTransition = orderedScores.get(i).getKey();
            }
            transitions.add(suggestedTransition); //add executed transition to transition sequence
        }

        //after the arcs are created, assign labels
       for(Token token : tree.getTokens()) {
            List<String> labelFeatures = FeatureExtraction.extractLabelFeatures(token, tree);
            token.setRel(model.getLabel(labelFeatures)); //assign the label predicted by the model
       }
        return transitions;
    }
}
