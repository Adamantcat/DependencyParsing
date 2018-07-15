package com.sdp.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia on 26.04.2018.
 */
public class Corpus {

    private List<Tree> trees;

    public List<Tree> getTrees() {

        return trees;
    }

    public void setTrees(List<Tree> trees) {

        this.trees = trees;
    }

    public Corpus() {

        this.trees = new ArrayList<Tree>();
    }

    public Corpus(List<Tree> trees) {

        this.trees = trees;
    }

    public void readFile(String filename) {
        try {
            BufferedReader src = new BufferedReader(new FileReader(new File(filename)));

            trees = new ArrayList<Tree>();
            String line;
            List<Token> tokens = new ArrayList<Token>();
            Token root = new Token(0, "ROOT", "P_ROOT", -1);
            tokens.add(root);

            while ((line = src.readLine()) != null) {
                Tree tree;
                if (line.equals("")) {
                    tree = new Tree(tokens);
                    trees.add(tree);
                    tokens = new ArrayList<Token>();
                    root = new Token(0, "ROOT", "P_ROOT", -1);
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

                    if (!fields[6].equals("_"))
                        token.setGoldIndex(Integer.parseInt(fields[6].trim()));
                    else
                        token.setGoldIndex(-1);

                    token.setRel(fields[7].trim());
                    tokens.add(token);
                }
            }

            src.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readGold(String filename) {
        try{
            BufferedReader src = new BufferedReader(new FileReader(new File(filename)));
            int i = 0;
            int j = 1;
            String line;
            while (((line = src.readLine()) != null)) {
                Tree tree = trees.get(i);
                List<Token> tokens = tree.getTokens();

                if(line.equals("")) {
                    tree.setTokens(tokens);
                    trees.set(i, tree);
                    i++;
                    j = 1;
                }
                else {
                   String[] fields = line.split("\t");
                   Token token = tokens.get(j);
                   token.setGoldRel(fields[7].trim());

                    if (!fields[6].equals("_")) {
                        token.setGoldIndex(Integer.parseInt(fields[6].trim()));
                       // System.out.println(j + " " + token);
                       //System.out.println("HEAD: " + fields[6] + " " + token);
                        tokens.set(j, token);
                        j++;
                    }
                    else
                        trees.get(i).getTokens().get(j).setHeadIndex(-1);
                }
            }

            src.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String filename) {
        try {
            File file = new File(filename);
            file.getParentFile().mkdirs();
            file.createNewFile();// if file already exists will do nothing
            file.setWritable(true);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (Tree tree : trees) {
                for(Token token : tree.getTokens()) {
                    String res = token.getIndex() + "\t" + token.getForm() + "\t" + token.getLemma() + "\t"
                            + token.getPos() + "\t" + token.getXpos() + "\t" + token.getMorph() + "\t"
                            + token.getHeadIndex() + "\t" + token.getRel() + "\t_\t_\n";
                    out.write(res);
                }
                out.write("\n");
            }
            System.out.println("saved to file " + filename);
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
