package com.sdp.util;


import java.io.Serializable;

public class Token implements Serializable{

    private int index;
    private String form;
    private String lemma;
    private String pos;
    private String xpos;
    private String morph;
    private String rel;
    private int goldIndex;
    private int headIndex;
    private String goldRel;

    public Token() {
        this.index = -1;
        this.form = "";
        this.lemma = "";
        this.pos = "";
        this.xpos = "";
        this.morph = "";
        this.rel = "";
        this.goldIndex = -1;
        this.headIndex = -1;
        this.goldRel = "_";
    }

    public Token(int index, String form, String pos, int headIndex) {
        this.index = index;
        this.form = form;
        this.pos = pos;
        this.headIndex = headIndex;

        this.lemma = "";
        this.xpos = "";
        this.morph = "";
        this.rel = "";
        this.goldIndex = -1;
        this.headIndex = -1;
        this.goldRel = "_";
    }

    public boolean isRoot() {
        return (index == 0 && form.equals("ROOT"));
    }

    public int getIndex() {
        return index;
    }

    public void setId(int id) {
        this.index = id;
    }

    public int getHeadIndex() {
        return headIndex;
    }

    public void setHeadIndex(int headIndex) {
        this.headIndex = headIndex;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getXpos() {
        return xpos;
    }

    public void setXpos(String xpos) {
        this.xpos = xpos;
    }

    public String getMorph() {
        return morph;
    }

    public void setMorph(String morph) {
        this.morph = morph;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public void setGoldIndex(int goldIndex) {
        this.goldIndex = goldIndex;
    }

    public int getGoldIndex() {
        return goldIndex;
    }

    public String getGoldRel() {
        return goldRel;
    }

    public void setGoldRel(String goldRel) {
        this.goldRel = goldRel;
    }

    @Override
    public String toString() {
        return "Token{" +
                "index=" + index +
                ", form='" + form + '\'' +
                ", lemma='" + lemma + '\'' +
                ", pos='" + pos + '\'' +
                ", xpos='" + xpos + '\'' +
                ", morph='" + morph + '\'' +
                ", rel='" + rel + '\'' +
                ", goldIndex=" + goldIndex +
                ", headIndex=" + headIndex +
                ", goldRel='" + goldRel + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (index != token.index) return false;
        if (goldIndex != token.goldIndex) return false;
        if (headIndex != token.headIndex) return false;
        if (!form.equals(token.form)) return false;
        if (!lemma.equals(token.lemma)) return false;
        if (!pos.equals(token.pos)) return false;
        if (!xpos.equals(token.xpos)) return false;
        if (!morph.equals(token.morph)) return false;
        if (!rel.equals(token.rel)) return false;
        return goldRel.equals(token.goldRel);
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + form.hashCode();
        result = 31 * result + lemma.hashCode();
        result = 31 * result + pos.hashCode();
        result = 31 * result + xpos.hashCode();
        result = 31 * result + morph.hashCode();
        result = 31 * result + rel.hashCode();
        result = 31 * result + goldIndex;
        result = 31 * result + headIndex;
        result = 31 * result + goldRel.hashCode();
        return result;
    }

}

