package com.sdp.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Course: Statistical Dependency Parsing, SS 2018
 * Author: Julia Koch
 * Class Description: Class to represent a corpus dependency trees in Conll06-format
 */
public class Corpus {

    private List<Tree> trees; //the trees in this corpus
    private Set<String> tagset; //set of relations in this corpus (NMOD, SUBJ, OBJ, etc.)

    public Corpus() {
        this.trees = new ArrayList<Tree>();
        this.tagset = new HashSet<String>();
        tagset.add("_"); //add default value
    }

    public Set<String> getTagset() {
        return tagset;
    }

    public void setTagset(Set<String> tagset) {
        this.tagset = tagset;
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public void setTrees(List<Tree> trees) {
        this.trees = trees;
    }

    //read corpus from conll06 file
    //works for training file with gold trees and for unannotated test set
    public void readFile(String filename) {
        try {
            BufferedReader src = new BufferedReader(new FileReader(new File(filename)));

            trees = new ArrayList<Tree>();
            String line;
            List<Token> tokens = new ArrayList<Token>();
            Token root = new Token(0, "ROOT", "P_ROOT", -1); //add root token for first tree
            tokens.add(root);

            while ((line = src.readLine()) != null) {
                Tree tree;
                //blank line separates trees
                if (line.equals("")) {
                    tree = new Tree(tokens); //build tree from current list of tokens
                    trees.add(tree);
                    tokens = new ArrayList<Token>(); //reset list of tokens
                    root = new Token(0, "ROOT", "P_ROOT", -1); //root token for next tree
                    tokens.add(root);
                } else {
                    String[] fields = line.split("\t");
                    Token token = new Token();

                    token.setId(Integer.parseInt(fields[0].trim()));
                    token.setForm(fields[1].trim());
                    token.setLemma(fields[2].trim());
                    token.setPos(fields[3].trim());
                    token.setXpos(fields[4].trim());
                    token.setMorph(fields[5].trim());

                    //if file contains gold heads
                    if (!fields[6].equals("_"))
                        token.setGoldIndex(Integer.parseInt(fields[6].trim()));
                    else
                        token.setGoldIndex(-1);

                    String rel = fields[7].trim();
                    token.setGoldRel(rel);
                    tagset.add(rel); //add relation to tagset
                    tokens.add(token);
                }
            }

            src.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //read file containing gold trees in conll06 format
    //corresponding unannotated file needs to be read before
    public void readGold(String filename) {
        try {
            BufferedReader src = new BufferedReader(new FileReader(new File(filename)));
            int i = 0; //ith three in file is also at position i in list of trees of this corpus
            int j = 1; //index of token in tree, start at 1, since root is not in file
            String line;
            while (((line = src.readLine()) != null)) {
                Tree tree = trees.get(i);
                List<Token> tokens = tree.getTokens();

                if (line.equals("")) {
                    tree.setTokens(tokens);
                    trees.set(i, tree); //put updated tree back to list
                    i++;
                    j = 1;
                } else {
                    String[] fields = line.split("\t");
                    Token token = tokens.get(j);
                    token.setGoldRel(fields[7].trim());

                    if (!fields[6].equals("_")) {
                        token.setGoldIndex(Integer.parseInt(fields[6].trim()));
                        tokens.set(j, token);
                        j++;
                    } else
                        trees.get(i).getTokens().get(j).setHeadIndex(-1);
                }
            }
            src.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //write corpus to file in conll06-format
    public void writeFile(String filename) {
        try {
            File file = new File(filename);
            file.getParentFile().mkdirs(); //create necessary sub-directories
            file.createNewFile();// if file already exists will do nothing
            file.setWritable(true);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (Tree tree : trees) {
                for (Token token : tree.getTokens()) {
                    if (token.isRoot()) //don't write artificial root token to file
                        continue;
                    String res = token.getIndex() + "\t" + token.getForm() + "\t" + token.getLemma() + "\t"
                            + token.getPos() + "\t" + token.getXpos() + "\t" + token.getMorph() + "\t"
                            + token.getHeadIndex() + "\t" + token.getRel() + "\t_\t_\n";
                    out.write(res);
                }
                out.write("\n");
            }
            System.out.println("saved to file " + filename);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
