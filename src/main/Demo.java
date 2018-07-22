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
        MulticlassPerceptron model = new MulticlassPerceptron(training.getTagset());
        model.train(training, 5);
        Parser parser = new TransitionParser(model);

        System.out.println("\ntest\n");

        test_en.getTrees().forEach(tree -> tree.clear());
        test_en.getTrees().forEach(tree -> parser.parse(tree));

        //parser.parse(testTree);

        //test_de.writeFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\results\\german\\tiger-2.2_dev_out.txt");
        test_en.writeFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\results\\english\\wsj_dev_out.txt");

        double uas = Evaluation.microUAS(test_en);
        double las = Evaluation.microLAS(test_en);
        System.out.println("UAS: " + uas);
        System.out.println("LAS: " + las);

    }
}
