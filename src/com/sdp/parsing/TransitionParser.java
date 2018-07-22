package com.sdp.parsing;

import com.sdp.ml.FeatureExtraction;
import com.sdp.ml.MulticlassPerceptron;
import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.util.*;

/**
 * Created by Julia on 28.06.2018.
 */
public class TransitionParser extends Parser {

    private MulticlassPerceptron model;

    public TransitionParser(MulticlassPerceptron model) {
        super();
        this.model = model;
    }

    @Override
    public List<String> parse(Tree tree) {
        List<String> transitions = new ArrayList<String>();
        Map<String, Double> scores;
        init(tree);
        while (!isTerminal()) {
            List<String> features = FeatureExtraction.extractFeatures(this, tree);
            scores = model.getScores(features);

            List<Map.Entry<String, Double>> orderedScores = new ArrayList(scores.entrySet());
            Collections.sort(orderedScores, (o1, o2) -> o2.getValue().compareTo(o1.getValue())); //sort scores
            //orderedScores.forEach(e -> System.out.println(e));

            String suggestedTransition = orderedScores.get(0).getKey();
            // System.out.println("suggested: " + suggestedTransition);
            boolean isValid = false;
            int i = 0;
            while ((!isValid) && (i < orderedScores.size() - 1)) {
                switch (suggestedTransition) {
                    case Parser.leftArc: {
                        isValid = leftArc();
                        break;
                    }
                    case Parser.rightArc:
                        isValid = rightArc();
                        break;

                    case Parser.shift:
                        isValid = shift();
                        break;
                }
                if(isValid)
                    break;
                i++;
                suggestedTransition = orderedScores.get(i).getKey();
            }
            transitions.add(suggestedTransition);
        }

        model.predictLabels(tree);

        return transitions;
    }
}
