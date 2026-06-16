package Nodes;

import provided.JottTree;

import java.util.ArrayList;

public class BodyNode implements JottTree {

    private ArrayList<BodyStmtNode> bodyStmtNodeList;
    private ReturnStmtNode returnStmtNode;


    public BodyNode(ArrayList<BodyStmtNode> bodyStmtNodeList, ReturnStmtNode returnStmtNode){
        this.bodyStmtNodeList = bodyStmtNodeList;
        this.returnStmtNode = returnStmtNode;
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
