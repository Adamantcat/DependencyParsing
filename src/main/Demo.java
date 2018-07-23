package main;

import com.sdp.evaluation.Evaluation;
import com.sdp.ml.MulticlassPerceptron;
import com.sdp.parsing.Oracle;
import com.sdp.parsing.Parser;
import com.sdp.parsing.TransitionParser;
import com.sdp.util.Corpus;
import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

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

        Corpus training_en = new Corpus();
        training_en.readFile(complete_en);

        Corpus training_de = new Corpus();
        training_de.readFile(complete_de);

        Corpus test_en = new Corpus();
        test_en.readFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\dev\\wsj_dev.conll06_blind.blind");
        test_en.readGold("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\english\\dev\\wsj_dev.conll06_gold.gold");

        Corpus test_de = new Corpus();
        test_de.readFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\german\\dev\\tiger-2.2.dev.conll06.blind");
        test_de.readGold("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\german\\dev\\tiger-2.2.dev.conll06.gold");

        MulticlassPerceptron model_en = new MulticlassPerceptron();
        String model_en_path = ".\\model\\model_en.txt";

        File model_en_in = new File(model_en_path);
        if(model_en_in.exists() && !model_en_in.isDirectory()) {
            System.out.println("LOGGING: read model from file");
            try {
                model_en = model_en.read(model_en_path);
            } catch (Exception e) {
                System.out.println("could not read model from file");
                e.printStackTrace();
            }
        }
    else {
            System.out.println("training");

            model_en = new MulticlassPerceptron(training_en.getTagset());
            model_en.train(training_en, 1);
            try {
                model_en.save(model_en_path);
            } catch (IOException e) {
                System.out.println("Could not save model");
                e.printStackTrace();
            }

        }

        MulticlassPerceptron model_de = new MulticlassPerceptron();
        String model_de_path = ".\\model\\model_de.txt";


        File model_de_in = new File(model_de_path);
        if(model_de_in.exists() && !model_en_in.isDirectory()) {
            System.out.println("LOGGING: read model from file");
            try {
                model_de = model_de.read(model_de_path);
            } catch (Exception e) {
                System.out.println("could not read model from file");
                e.printStackTrace();
            }
        }
        else {
            System.out.println("training");

            model_de = new MulticlassPerceptron(training_de.getTagset());
            model_de.train(training_de, 1);
            try {
                model_en.save(model_de_path);
            } catch (IOException e) {
                System.out.println("Could not save model");
                e.printStackTrace();
            }

        }


        System.out.println("\ntest\n");
        Parser parser = new TransitionParser(model_de);
        test_en.getTrees().forEach(tree -> tree.clear());
        test_en.getTrees().forEach(tree -> parser.parse(tree));


        test_de.writeFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\results\\german\\tiger-2.2_dev_out.txt");
        //test_en.writeFile("C:\\Users\\Julia\\Documents\\Master\\SS18\\DependencyParsing\\results\\english\\wsj_dev_out.txt");

        double uas = Evaluation.microUAS(test_en);
        double las = Evaluation.microLAS(test_en);
        System.out.println("UAS: " + uas);
        System.out.println("LAS: " + las);

    }
}
