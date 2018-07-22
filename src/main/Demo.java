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

        Tree testTree = new Tree(tokens);

        String first1k_en = "C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\train\\wsj_train.only-projective.first-1k.conll06";
        String first5k_en = "C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\train\\wsj_train.only-projective.first-5k.conll06";
        String complete_en = "C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\train\\wsj_train.only-projective.conll06";

        String first1k_de = "C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\german\\train\\tiger-2.2.train.only-projective.first-1k.conll06";
        String first5k_de = "C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\german\\train\\tiger-2.2.train.only-projective.first-5k.conll06";
        String complete_de = "C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\german\\train\\tiger-2.2.train.only-projective.conll06";

        Corpus training = new Corpus();
        training.readFile(complete_en);

        Corpus test_en = new Corpus();
        test_en.readFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\dev\\wsj_dev.conll06_blind.blind");
        test_en.readGold("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\dev\\wsj_dev.conll06_gold.gold");

        Corpus test_de = new Corpus();
        test_de.readFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\german\\dev\\tiger-2.2.dev.conll06.blind");
        test_de.readGold("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\german\\dev\\tiger-2.2.dev.conll06.gold");


        System.out.println("training");
        MulticlassPerceptron model = new MulticlassPerceptron();
        model.train(training, 10);
        Parser parser = new TransitionParser(model);



        System.out.println("\ntest\n");

        test_en.getTrees().forEach(tree -> tree.clear());
        test_en.getTrees().forEach(tree -> parser.parse(tree));

        //parser.parse(testTree);

        test_de.writeFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\results\\german\\wsj_dev_out.txt");

        double uas = Evaluation.microUAS(test_en);
        double las = Evaluation.microLAS(test_en);
        System.out.println("UAS: " + uas);
        System.out.println("LAS: " + las);

    }
}
