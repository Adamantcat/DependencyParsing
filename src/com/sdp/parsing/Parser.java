package com.sdp.parsing;

import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by Julia on 07.06.2018.
 */
public abstract class Parser {
    protected Stack<Token> stack;
    protected Deque<Token> buffer;
    public static final String leftArc = "leftArc";
    public static final String rightArc = "rightArc";
    public static final String shift = "shift";
    public static final String[] transitions = {leftArc, rightArc, shift};

    public Parser() {
        this.stack = new Stack();
        this.buffer = new ArrayDeque();
    }

    public Stack<Token> getStack() {
        return this.stack;
    }

    public Deque<Token> getBuffer() {
        return this.buffer;
    }

    public void init(Tree tree) {
        //clear stack and buffer before parsing a new tree
        stack.clear();
        buffer.clear();

        //push root token on stack
        stack.push(tree.getByIndex(0));
        //add all tokens of tree to buffer, except root
        buffer.addAll(tree.getTokens().stream().filter(t -> !t.isRoot()).collect(Collectors.toList()));
    }

    protected boolean leftArc() {
        if((!stack.isEmpty()) && (!stack.peek().isRoot())) {
            Token dependant = stack.pop();
            Token head = buffer.peek();
            dependant.setHeadIndex(head.getIndex());
            return true;
        }
        return false;
    }

    protected boolean rightArc() {
        if(!stack.isEmpty() && !buffer.isEmpty()) {
            Token head = stack.pop();
            Token dependant = buffer.pop();
            dependant.setHeadIndex(head.getIndex());
            buffer.addFirst(head);
            return true;
        }
        return false;
    }

    protected boolean shift() {
        if((buffer.size() >= 2) || stack.isEmpty()) {
            Token token = buffer.pop();
            stack.push(token);
            return true;
        }
        return false;
    }

    public boolean isTerminal() {
        return buffer.isEmpty();
    }
    public abstract List<String> parse(Tree tree);
}
