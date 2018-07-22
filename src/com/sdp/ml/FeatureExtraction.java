package com.sdp.ml;

import com.sdp.parsing.Parser;
import com.sdp.util.Token;
import com.sdp.util.Tree;

import java.util.*;

/**
 * Created by Julia on 05.07.2018.
 */
public class FeatureExtraction {

    public static List<String> extractFeatures(Parser parser, Tree tree) {
        List<String> features = new ArrayList<String>();

        Stack<Token> stack = parser.getStack();
        Deque<Token> buffer = parser.getBuffer();
        Comparator<Token> comparator = Comparator.comparing(Token::getIndex);

        Token s0 = null; //first token on stack
        Token b0 = buffer.peekFirst(); //first token on buffer
        //Basic Features
        if (!stack.isEmpty()) {
            s0 = stack.peek();
            features.add("S[0]_form=" + s0.getForm()); //form of first token on stack
            features.add("S[0]_pos=" + s0.getPos()); //POS of first token on stack
            features.add("S[0]_lemma=" + s0.getLemma()); //lemma of first token on stack

            //two-word features
            features.add("S[0]_form+B[0]_form=" + s0.getForm() + "+" + b0.getForm());
            features.add("S[0]_form+B[0]_pos=" + s0.getForm() + "+" + b0.getPos());
            features.add("S[0]_pos+B[0]_pos=" + s0.getPos() + "+" + b0.getPos());

            //combined
            features.add("S[0]_form+pos=" + s0.getForm() + "+" + s0.getPos());
        } else {
            features.add("S[0]_pos+B[0]_pos=null+" + b0.getPos());
            //features.add("S[0]=null"); //dummy feature for empty stack -> negative effects
        }

        features.add("B[0]_form=" + b0.getForm()); //form of token in front of buffer
        features.add("B[0]_pos=" + b0.getPos()); //POS of token in front of buffer
        features.add("B[0]_lemma=" + b0.getLemma()); //POS of token in front of buffer

        //combined
        features.add("B[0]_form+pos=" + b0.getForm() + "+" + b0.getPos());

        Token[] bufferArray = buffer.toArray(new Token[0]);
        if (buffer.size() > 3) {
            features.add("B[1]_form=" + bufferArray[1].getForm()); //form of second element in buffer
            features.add("B[1]_pos=" + bufferArray[1].getPos()); //pos of second element in buffer
            features.add("B[2]_form=" + bufferArray[2].getForm()); //from of third element in buffer
            features.add("B[2]_pos=" + bufferArray[2].getPos()); //pos of third element in buffer
            features.add("B[3]_form=" + bufferArray[3].getForm()); //from of fourth element in buffer
            features.add("B[3]_pos=" + bufferArray[3].getPos()); //pos of fourth element in buffer

            features.add("B[0]_pos+B[1]_pos=" + b0.getPos() + "+" + bufferArray[1].getPos());
            features.add("B[0]_pos+B[1]_pos+B[2]_pos=" + b0.getPos() + "+" + bufferArray[1].getPos() + "+" + bufferArray[2].getPos());

            if (s0 != null) {
                features.add("S[0]_pos[B[0]_pos+B[1]_pos=" + s0.getPos() + "+" + b0.getPos() + "+" + bufferArray[1].getPos());
            }
            //combined
            features.add("B[1]_form+pos=" + bufferArray[1].getForm() + "+" + bufferArray[1].getPos());
            //features.add("B[2]_form+pos=" + bufferArray[2].getForm() + "+" + bufferArray[2].getPos());
            //features.add("B[3]_form+pos=" + bufferArray[3].getForm() + "+" + bufferArray[3].getPos());

        } else if (buffer.size() == 3) {
            features.add("B[1]_form=" + bufferArray[1].getForm()); //form of second element in buffer
            features.add("B[1]_pos=" + bufferArray[1].getPos()); //pos of second element in buffer
            features.add("B[2]_form=" + bufferArray[2].getForm()); //form of third element in buffer
            features.add("B[2]_pos=" + bufferArray[2].getPos()); //pos of third element in buffer
            features.add("B[3]_form=null"); //form of fourth element in buffer
            features.add("B[3]_pos=null"); //pos of fourth element in buffer

            features.add("B[0]_pos+B[1]_pos=" + b0.getPos() + "+" + bufferArray[1].getPos());
            features.add("B[0]_pos+B[1]_pos+B[2]_pos=" + b0.getPos() + "+" + bufferArray[1].getPos() + "+" + bufferArray[2].getPos());

            if (s0 != null) {
                features.add("S[0]_pos[B[0]_pos+B[1]_pos=" + s0.getPos() + "+" + b0.getPos() + "+" + bufferArray[1].getPos());
            }

            //combined
            features.add("B[1]_form+pos=" + bufferArray[1].getForm() + "+" + bufferArray[1].getPos());
            //features.add("B[2]_form+pos=" + bufferArray[2].getForm() + "+" + bufferArray[2].getPos());
            //features.add("B[3]_form+pos=null");

        } else if (buffer.size() == 2) {
            features.add("B[1]_form=" + bufferArray[1].getForm()); //form of second element in buffer
            features.add("B[1]_pos=" + bufferArray[1].getPos()); //pos of second element in buffer
            features.add("B[2]_form=null"); //form of third element in buffer
            features.add("B[2]_pos=null"); //pos of third element in buffer
            features.add("B[3]_form=null"); //form of fourth element in buffer
            features.add("B[3]_pos=null"); //pos of fourth element in buffer

            features.add("B[0]_pos+B[1]_pos=" + b0.getPos() + "+" + bufferArray[1].getPos());

            if (s0 != null) {
                features.add("S[0]_pos[B[0]_pos+B[1]_pos=" + s0.getPos() + "+" + b0.getPos() + "+" + bufferArray[1].getPos());
            }

            //combined
            features.add("B[1]_form+pos=" + bufferArray[1].getForm() + "+" + bufferArray[1].getPos());
            //features.add("B[2]_form+pos=null");
            //features.add("B[3]_form+pos=null");
        } else {
            // features.add("buffer_length=1");
            features.add("B[1]_form=null"); //form of second element in buffer
            features.add("B[1]_pos=null"); //pos of second element in buffer
            features.add("B[2]_form=null"); //form of third element in buffer
            features.add("B[2]_pos=null"); //pos of third element in buffer
            features.add("B[3]_form=null"); //form of fourth element in buffer
            features.add("B[3]_pos=null"); //pos of fourth element in buffer

            //combined
//            features.add("B[1]_form+pos=null");
//            features.add("B[2]_form+pos=null");
//            features.add("B[3]_form+pos=null");
        }


        Optional<Token> ld_b = tree.getDependents(b0).stream().filter(t -> t.getIndex() < b0.getIndex()).min(comparator); //find left most dependent of B[0]
        if (ld_b.isPresent())
            features.add("ld(B[0])_pos=" + ld_b.get().getPos());
        else
            features.add("ld(B[0])_pos=null");

        Optional<Token> rd_b = tree.getDependents(b0).stream().filter(t -> t.getIndex() > b0.getIndex()).max(comparator); //find left most dependent of B[0]
        if (rd_b.isPresent())
            features.add("rd(B[0])_pos=" + rd_b.get().getPos());
        else
            features.add("rd(B[0])_pos=null");


        if (s0 != null) {
            Optional<Token> ld_s = tree.getDependents(s0).stream().filter(t -> t.getIndex() < stack.peek().getIndex()).min(comparator); //find left most dependent of S[0]
            if (ld_s.isPresent()) {
                features.add("ld(S[0])_pos=" + ld_s.get().getPos());
                //features.add("ld(S[0])_pos+S[0]_pos+B[0]_pos=" + ld_s.get().getPos() + stack.peek().getPos() + buffer.peekFirst().getPos());
                features.add("rd(S[0])_pos+B[0]_pos=" + ld_s.get().getPos() + buffer.peekFirst().getPos());
                //features.add("ld(S[0])_pos+S[0]_pos=" + ld_s.get().getPos() + stack.peek().getPos());
            } else {
                features.add("ld(S[0])_pos=null");
            }
            Optional<Token> rd_s = tree.getDependents(s0).stream().filter(t -> t.getIndex() > stack.peek().getIndex()).max(comparator); //find left most dependent of S[0]
            if (rd_s.isPresent()) {
                features.add("rd(S[0])_pos=" + rd_s.get().getPos());
                //features.add("rd(S[0])_pos+S[0]_pos+B[0]_pos=" + rd_s.get().getPos() + stack.peek().getPos() + buffer.peekFirst().getPos());
                features.add("rd(S[0])_pos+B[0]_pos=" + rd_s.get().getPos() + buffer.peekFirst().getPos());
                //features.add("ld(S[0])_pos+S[0]_pos=" + rd_s.get().getPos() + stack.peek().getPos());
            } else {
                features.add("rd(S[0])_pos=null");
            }
        }

        return features;
    }

    public static List<String> extractLabelFeatures(Token dependent, Tree tree) {
        List<String> features = new ArrayList<String>();

        Token head = tree.getByIndex(dependent.getHeadIndex());
        if(head != null) {
            features.add("h_form=" + head.getForm());
            features.add("h_pos=" + head.getPos());
        }
        else {
            features.add("h_form=null");
            features.add("h_pos=null");
        }

        features.add("d_form=" + dependent.getForm());
        features.add("d_pos=" + dependent.getPos());

        Set<Token> children = tree.getDependents(dependent);

        return features;
    }
}
