package com.sdp.ml;

import com.sdp.parsing.Parser;
import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

/**
 * Created by Julia on 05.07.2018.
 */
public class FeatureExtraction {

    public static List<String> extractFeatures (Parser parser) {
        List<String> features = new ArrayList<String>();

        Stack<Token> stack = parser.getStack();
        Deque<Token> buffer = parser.getBuffer();

        if(!stack.isEmpty()) {
            features.add("S[0]_form=" + stack.peek().getForm()); //form of first token on stack
            features.add("S[0]_pos=" + stack.peek().getPos()); //POS of first token on stack
            features.add("S[0]_lemma=" + stack.peek().getLemma()); //lemma of first token on stack
        }
        else {
            features.add("S[0]=NULL"); //dummy feature for empty stack
        }

        features.add("B[0]_form=" + buffer.peekFirst().getForm()); //form of token in front of buffer
        features.add("B[0]_pos=" + buffer.peekFirst().getPos()); //POS of token in front of buffer
        features.add("B[0]_lemma=" + buffer.peekFirst().getLemma()); //POS of token in front of buffer

        Token[] bufferArray = buffer.toArray(new Token[0]);
        if(buffer.size() > 3) {
            features.add("B[1]=" + bufferArray[1].getForm()); //form of second element in buffer
            features.add("B[1]=" + bufferArray[1].getPos()); //pos of second element in buffer
            features.add("B[2]=" + bufferArray[2].getPos()); //pos of third element in buffer
            features.add("B[3]=" + bufferArray[3].getPos()); //pos of fourth element in buffer
        }
        else if(buffer.size() == 3) {
            features.add("B[1]=" + bufferArray[1].getForm()); //form of second element in buffer
            features.add("B[1]=" + bufferArray[1].getPos()); //pos of second element in buffer
            features.add("B[2]=" + bufferArray[2].getPos()); //pos of third element in buffer
            features.add("B[3]=null"); //pos of fourth element in buffer
        }
        else if(buffer.size() == 2) {
            features.add("B[1]=" + bufferArray[1].getForm()); //form of second element in buffer
            features.add("B[1]=" + bufferArray[1].getPos()); //pos of second element in buffer
            features.add("B[2]=null"); //pos of third element in buffer
            features.add("B[3]=null"); //pos of fourth element in buffer
        }
        else {
                features.add("B[1]=null"); //form of second element in buffer
                features.add("B[1]=null"); //pos of second element in buffer
                features.add("B[2]=null"); //pos of third element in buffer
                features.add("B[3]=null"); //pos of fourth element in buffer
        }

        return features;
    }
}
