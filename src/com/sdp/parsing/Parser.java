package com.sdp.parsing;

import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Course: Statistical Dependency Parsing, SS 2018
 * Author: Julia Koch
 * Class Description: Abstract class for an Arc-Standard transition-based dependency parser
 * structure and transitions are equal for Oracle and actual parsing
 * implementing subclasses: Oracle, TransitionParser
 */
public abstract class Parser implements Serializable{
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

    //initialize stack and buffer according to the tree to parse
    public void init(Tree tree) {
        //clear stack and buffer before parsing a new tree
        stack.clear();
        buffer.clear();

        //push root token on stack
        stack.push(tree.getByIndex(0));
        //add all tokens of tree to buffer, except root
        buffer.addAll(tree.getTokens().stream().filter(t -> !t.isRoot()).collect(Collectors.toList()));
    }

    //Arc-standard left arc transition
    //introduce arc from top of buffer to top of stack, pop stack
    //returns true, if the transition was executed successfully, i.e. the transition was valid
    protected boolean leftArc() {
        //precondition: stack is not empty and top of stack is not root
        if((!stack.isEmpty()) && (!stack.peek().isRoot())) {
            Token dependant = stack.pop();
            Token head = buffer.peek();
            dependant.setHeadIndex(head.getIndex());
            return true;
        }
        return false;
    }

    //Arc-standard right arc transition
    //introduce arc from top of stack to top of buffer,
    // remove dependent from buffer and shift top of stack to buffer
    //returns true, if the transition was executed successfully, i.e. the transition was valid
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

    //Arc-standard shift transition
    //shift top of buffer to top of stack
    //returns true, if the transition was executed successfully, i.e. the transition was valid
    protected boolean shift() {
        //precondition: buffer size is at least 2 or stack is empty
        if((buffer.size() >= 2) || stack.isEmpty()) {
            Token token = buffer.pop();
            stack.push(token);
            return true;
        }
        return false;
    }

    //configuration is terminal, if buffer is empty
    public boolean isTerminal() {
        return buffer.isEmpty();
    }
    public abstract List<String> parse(Tree tree); //abstract method: parse a tree and return sequence of transitions
}
