package Nodes;

import provided.JottTree;

import java.util.ArrayList;
/**
 * File name: FBodyNode.java
 * Author: Kifekachukwu Nwosu
 *
 * This file defines the FBodyNode class, which represents a grammar for function body
 * in the Jott parse tree shown the following:
 *
 *
 * The class provides functionality to parse a function body based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */
public class FBodyNode implements JottTree {

    ArrayList<VarDecNode> varDec;
    private ReturnStmtNode returnStmt;

    private ArrayList<VarDecNode> varDecList;
    private BodyNode bodyNode;

    public FBodyNode(ArrayList<VarDecNode> varDecList, BodyNode bodyNode) {
        this.varDecList = varDecList;
        this.bodyNode = bodyNode;
    }

    @Override
    public String convertToJott() {

        String result = "";

        if (varDecList != null) {
            for (VarDecNode varDec : varDecList) {
                result += varDec.convertToJott();
            }
        }

        if (bodyNode != null) {
            result += bodyNode.convertToJott();
        }

        return result;
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
