package com.sdp.parsing;

import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia on 07.06.2018.
 */
public class Oracle extends Parser {

    public Oracle() {
        super();
    }

    public List<String> parse(Tree tree) {
        init(tree);
        List<String> transitions = new ArrayList<String>();
        while (!isTerminal()) {
            transitions.add(nextTransition(tree));
        }
        return transitions;
    }

    private boolean shouldLeftArc() {
        if (!stack.isEmpty() && !buffer.isEmpty())
            return stack.peek().getGoldIndex() == buffer.peekFirst().getIndex();
        return false;
    }

    private boolean shouldRightArc(Tree tree) {
        if (!stack.isEmpty() || buffer.isEmpty())
            return (buffer.peekFirst().getGoldIndex() == stack.peek().getIndex()) && hasAllChildren(tree, buffer.peekFirst());
        return false;
    }

    private boolean hasAllChildren(Tree tree, Token token) {
        return tree.getAllChildren(token).equals(tree.getDependents(token));
    }

    public String nextTransition(Tree tree) {
        String transition = "";
        boolean done = false;
        if (shouldLeftArc()) {
            done = leftArc(); //check that left arc transition actually happened
            transition = Parser.leftArc;
        } else if (shouldRightArc(tree)) {
            done = rightArc(); //check that right arc transition actually happened
            transition = Parser.rightArc;
        } else {
            done = shift(); //check that shift transition actually happened
            transition = Parser.shift;
        }
        return transition;
    }
}
