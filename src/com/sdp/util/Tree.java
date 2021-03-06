package com.sdp.util;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Course: Statistical Dependency Parsing, SS 2018
 * Author: Julia Koch
 * Class Description: Class to represent a dependency tree in Conll06-format.
 * A tree is a List of Tokens
 */
public class Tree implements Serializable {

    private List<Token> tokens; //the tokens of this tree

    //Constructor
    public Tree(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tree tree = (Tree) o;
        return tokens.equals(tree.tokens);
    }

    @Override
    public int hashCode() {
        return tokens.hashCode();
    }

    //get predicted dependents of a token
    public Set<Token> getDependents(Token token) {
        Set<Token> dependents = new HashSet<Token>();
        for (Token t : tokens) {
            if (t.getHeadIndex() == (token.getIndex()))
                dependents.add(t);
        }
        return dependents;
    }

    //get gold dependents of a token
    public Set<Token> getAllChildren(Token token) {
        Set<Token> children = new HashSet<Token>();
        for (Token t : tokens) {
            if (t.getGoldIndex() == (token.getIndex()))
                children.add(t);
        }
        return children;
    }

    //return token with a given index
    public Token getByIndex(int index) {
        if (index == -1)
            return null;
        return tokens.get(index);
    }

    //reset predicted heads and relations
    public void clear() {
        for (Token token : tokens) {
            token.setHeadIndex(-1);
            token.setRel("_");
        }
    }

    public String toString() {
        String s = "Tree = [";
        for (Token token : tokens) {
            s += token.toString() + "\n";
        }
        return s + "]";
    }
}
