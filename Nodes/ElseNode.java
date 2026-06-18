package Nodes;

import provided.JottTree;

public class ElseNode implements JottTree {

    public ElseNode() {
    }

    @Override
    public String convertToJott() {
        return "";
    }

    @Override
    public String convertToJava(String className) {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
}