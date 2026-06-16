package Nodes;

import provided.JottTree;

import java.util.ArrayList;

public class ProgramNode implements JottTree {
    private ArrayList<FunctionDefNode> functionDefNode;

    public ProgramNode (ArrayList<FunctionDefNode> functionDefNodes){
        this.functionDefNode = functionDefNodes;
    }

    @Override
    public String convertToJott() {
        return null;
    }
    @Override
    public String convertToJava(String className){
        return null;
    }

    @Override
    public String convertToC(){
        return null;
    }
    @Override
    public String convertToPython(){
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
}
