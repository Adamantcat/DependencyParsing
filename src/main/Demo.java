package main;

import com.sdp.evaluation.Evaluation;
import com.sdp.ml.MulticlassPerceptron;
import com.sdp.parsing.Oracle;
import com.sdp.parsing.Parser;
import com.sdp.parsing.TransitionParser;
import com.sdp.util.Corpus;
import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia on 26.04.2018.
 */
public class Demo {
    public static void main(String[] args) {

        //Test tree
        Token t0 = new Token(0, "ROOT", "P_ROOT", -1);
        t0.setGoldIndex(-1);
        Token t1 = new Token(1, "John", "NN", -1);
        t1.setGoldIndex(2);
        Token t2 = new Token(2, "saw", "V", -1);
        t2.setGoldIndex(0);
        Token t3 = new Token(3, "Mary", "NN", -1);
        t3.setGoldIndex(2);

        List<Token> tokens = new ArrayList<Token>();
        tokens.add(t0);
        tokens.add(t1);
        tokens.add(t2);
        tokens.add(t3);

        String first1k = "C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\train\\wsj_train.only-projective.first-1k.conll06";
        String first5k = "C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\train\\wsj_train.only-projective.first-5k.conll06";
        Corpus training = new Corpus();
        training.readFile(first5k);

        Corpus test = new Corpus();
        test.readFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\dev\\wsj_dev.conll06_pred.pred");
        test.readGold("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\dev\\wsj_dev.conll06_gold.gold");
        //corpus.getTrees().forEach(tree -> System.out.println(tree));
        //System.out.println(corpus.getTrees().get(0));

        // corpus.writeFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\results\\wsj_dev_out.txt");


        System.out.println("training");
        MulticlassPerceptron model = new MulticlassPerceptron();
        model.train(training);
        Parser parser = new TransitionParser(model);



        System.out.println("\ntest\n");
       /* Tree tree = new Tree(tokens);
        Tree first = training.getTrees().get(0);
        first.clear();
        Oracle oracle = new Oracle();
        List<String> transitions = oracle.parse(tree);
        tree.clear();

        System.out.println("oracle\n");
        transitions.forEach(t -> System.out.println(t));

        List<String> predictedTransitions = parser.parse(tree);
        System.out.println("\npredicted\n");
        predictedTransitions.forEach(t -> System.out.println(t)); */

        test.getTrees().forEach(tree -> tree.clear());
        test.getTrees().forEach(tree -> parser.parse(tree));

        double uas = Evaluation.microUAS(test);
        double las = Evaluation.microLAS(test);
        System.out.println("UAS: " + uas);
        System.out.println("LAS: " + las);

    }
}
