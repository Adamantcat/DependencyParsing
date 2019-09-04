package com.sdp.parsing;

import com.sdp.util.Token;
import com.sdp.util.Tree;
import java.util.ArrayList;
import java.util.List;

/**
 * Course: Statistical Dependency Parsing, SS 2018
 * Author: Julia Koch
 * Class Description: Arc-Standard Oracle parser
 */
public class Oracle extends Parser {

    public Oracle() {
        super();
    }

    //oracle parsing of a given gold tree
    //returns sequence of oracle transitions
    public List<String> parse(Tree tree) {
        init(tree);
        List<String> transitions = new ArrayList<String>();
        while (!isTerminal()) {
            transitions.add(nextTransition(tree));
        }
        return transitions;
    }

    //returns true, if the next transition should be left arc
    private boolean shouldLeftArc() {
        if (!stack.isEmpty() && !buffer.isEmpty())
            return stack.peek().getGoldIndex() == buffer.peekFirst().getIndex();
        return false;
    }

    //returns true, if the next transition should be right arc
    //precondition: dependent has already collected all its children
    private boolean shouldRightArc(Tree tree) {
        if (!stack.isEmpty() || buffer.isEmpty())
            return (buffer.peekFirst().getGoldIndex() == stack.peek().getIndex()) && hasAllChildren(tree, buffer.peekFirst());
        return false;
    }

    //check whether a token has collected all children
    private boolean hasAllChildren(Tree tree, Token token) {
        return tree.getAllChildren(token).equals(tree.getDependents(token));
    }

    //execute the next oracle transition,
    //i.e. do not parse whole tree at once but do only one step
    public String nextTransition(Tree tree) {
        String transition = "";
        if (shouldLeftArc()) {
            leftArc(); //no need to check that left arc transition actually happened, since oracle never predicts invalid transitions
            transition = Parser.leftArc;
        } else if (shouldRightArc(tree)) {
            rightArc(); //no need to check that right arc transition actually happened
            transition = Parser.rightArc;
        } else {
            shift(); //no need to check that shift transition actually happened
            transition = Parser.shift;
        }
        return transition;
    }
}
