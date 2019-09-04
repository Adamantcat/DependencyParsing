package main;

import com.sdp.evaluation.Evaluation;
import com.sdp.ml.MulticlassPerceptron;
import com.sdp.parsing.Parser;
import com.sdp.parsing.TransitionParser;
import com.sdp.util.Corpus;
import java.io.File;
import java.io.IOException;

/**
 * Course: Statistical Dependency Parsing, SS 2018
 * Author: Julia Koch
 * Class Description: Main class: parse Conll06 files in english or german
 * input: language (english or german), filename: file to parse, goldfile: gold trees for parsed dataset (optional)
 */
public class Demo {
    public static void main(String[] args) {

        String lang = "";
        String testFile = "";
        String goldFile = "";

        //read arguments from command line
        if (args.length < 2) {
            System.out.println("Usage: java -jar DependencyParsing.jar <language> <filename> ?<goldfile>");
            System.out.println("language: english || german ");
            System.out.println("filename: the file to parse");
            System.out.println("goldfile: gold trees for parsed dataset (optional)");
            System.exit(0);
        } else if (args.length == 3) {
            lang = args[0];
            testFile = args[1];
            goldFile = args[2];
        } else {
            lang = args[0];
            testFile = args[1];
        }

        //training sets, only used when no pre-trained model was found
        String first1k_en = ".\\english\\train\\wsj_train.only-projective.first-1k.conll06";
        String first5k_en = ".\\english\\train\\wsj_train.only-projective.first-5k.conll06";
        String complete_en = ".\\train\\wsj_train.only-projective.conll06";

        String first1k_de = ".\\german\\train\\tiger-2.2.train.only-projective.first-1k.conll06";
        String first5k_de = ".\\german\\train\\tiger-2.2.train.only-projective.first-5k.conll06";
        String complete_de = ".\\train\\tiger-2.2.train.only-projective.conll06";

        Corpus training = new Corpus(); //training data
        MulticlassPerceptron model = new MulticlassPerceptron(); //the model
        String model_path = ""; //path where pre-trained model is located
        String out = ""; //output file for parsed dataset

        //initialize variables according to language
        switch (lang) {
            case "english":
                training.readFile(complete_en);
                model_path = ".\\model\\model_en.txt";
                out = ".\\results\\english\\wsj_test_out.txt";
                break;

            case "german":
                training.readFile(complete_de);
                model_path = ".\\model\\model_de.txt";
                out = ".\\results\\german\\tiger-2.2_test_out.txt";
                break;

            default:
                System.out.println("This language is not available");
                System.exit(0);
                break;
        }

        //read a pre-trained model from file
        File model_in = new File(model_path);
        if (model_in.exists() && !model_in.isDirectory()) { //check if file exists
            System.out.println("read model from file");
            try {
                model = model.read(model_path);
            } catch (Exception e) {
                System.out.println("could not read model from file");
                e.printStackTrace();
            }
        } else { //if no pre-trained model exists, train a new model on training data
            System.out.println("training");

            model = new MulticlassPerceptron(training.getTagset()); //train with labels
            model.train(training, 10);
            try {
                model.save(model_path); //save the new model to file system
            } catch (IOException e) {
                System.out.println("Could not save model");
                e.printStackTrace();
            }
        }

        System.out.println("\ntesting\n");

        Corpus test = new Corpus(); //the test data
        test.readFile(testFile);

        //Parse test data
        Parser parser = new TransitionParser(model);
        test.getTrees().forEach(tree -> tree.clear());
        test.getTrees().forEach(tree -> parser.parse(tree));

        //if goldFile is given by user, use it for evaluation
        if (goldFile != "") {
            test.readGold(goldFile);
            double uas = Evaluation.microUAS(test);
            double las = Evaluation.microLAS(test);
            System.out.println("UAS: " + uas);
            System.out.println("LAS: " + las);
        }
        test.writeFile(out); //write parsed test data to output
    }
}
