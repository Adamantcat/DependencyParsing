package com.sdp.evaluation;

import com.sdp.util.Corpus;
import com.sdp.util.Token;
import com.sdp.util.Tree;

/**
 * Created by Julia on 16.05.2018.
 */
public class Evaluation {

    public static double getUAS(Tree tree) {
        int correct = 0;
        for(Token token : tree.getTokens()) {
            if(token.isRoot())
                continue;
            if(token.getHeadIndex() == token.getGoldIndex())
                correct++;
        }
        return ((double)correct/(tree.getTokens().size()) - 1); //-1 because root shoudn't be counted
    }

    public static double getLAS(Tree tree) {
        int correct = 0;
        for(Token token : tree.getTokens()) {
            if(token.isRoot())
                continue;
            if((token.getHeadIndex() == token.getGoldIndex()) && (token.getRel().equals(token.getGoldRel())))
                correct++;
        }
        return ((double)correct/(tree.getTokens().size() - 1));
    }

    public static double macroUAS(Corpus corpus) {
        double nominator = 0;
        for(Tree tree : corpus.getTrees()) {
            nominator += getUAS(tree);
        }
        return (double) (nominator/corpus.getTrees().size());
    }

    public static double macroLAS(Corpus corpus) {
        double nominator = 0;
        for(Tree tree : corpus.getTrees()) {
            nominator += getLAS(tree);
        }
        return (double) (nominator/corpus.getTrees().size());
    }

    public static double microUAS(Corpus corpus) {
        double nominator = 0;
        double denominator = 0;
        for(Tree tree : corpus.getTrees()) {
            for(Token token : tree.getTokens()) {
                if(token.isRoot())
                    continue;
                if(token.getHeadIndex() == token.getGoldIndex())
                    nominator++;
            }
            denominator += (tree.getTokens().size() - 1);
        }
        System.out.println(nominator + "/" + denominator);
        return (double) (nominator/denominator);
    }

    public static double microLAS(Corpus corpus) {
        double nominator = 0;
        double denominator = 0;
        for(Tree tree : corpus.getTrees()) {
            for(Token token : tree.getTokens()) {
                if(token.isRoot())
                    continue;
                if((token.getHeadIndex() == token.getGoldIndex()) && (token.getRel().equals(token.getGoldRel())))
                    nominator++;
            }
            denominator += (tree.getTokens().size() - 1);
        }
        System.out.println(nominator + "/" + denominator);
        return (double) (nominator/denominator);
    }
}
