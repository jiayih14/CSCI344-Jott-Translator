package Nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;
/**
 * File name: FBodyNode.java
 * Author: Teju Rajbabu, Jiayi Huang
 *
 * This file defines the FBodyNode class, which represents the grammar:
 *
 * < f_body > -> < var_dec >* < body >
 *
 * The class provides functionality to parse a func body based on the grammar.
 * It also allows conversion back to Jott.
 */
public class FBodyNode implements JottTree {

    ArrayList<VarDecNode> varDec;
    private BodyNode bodyNode;

    public FBodyNode(ArrayList<VarDecNode> varDec, BodyNode bodyNode){
        this.varDec = varDec;
        this.bodyNode = bodyNode;
    }

    public static FBodyNode parse(ArrayList<Token> tokens) {

        ArrayList<VarDecNode> varDecs = new ArrayList<>();

        // Parse zero or more var_decs
        while (ParserHelper.checkValue(tokens, "Double") ||
                ParserHelper.checkValue(tokens, "Integer") ||
                ParserHelper.checkValue(tokens, "String") ||
                ParserHelper.checkValue(tokens, "Boolean")) {

            varDecs.add(VarDecNode.parse(tokens));
        }

        // NOW check for body
//        if (!tokens.isEmpty() && tokens.get(0).getToken().equals("{")) {
//            BodyNode body = BodyNode.parse(tokens);
//            return new FBodyNode(varDecs, body);
//        }
        BodyNode body = BodyNode.parse(tokens);

        // If no body, this is a syntax error
//        JottParser.reportError("Expected '{' to begin function body", tokens.get(0));
        return new FBodyNode(varDecs, body);
    }

    @Override
    public String convertToJott() {
        StringBuilder result = new StringBuilder();
        for(VarDecNode vd : this.varDec){
            result.append(vd.convertToJott());
        }
        result.append(this.bodyNode.convertToJott());
        return result.toString();
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
        for (VarDecNode vd : this.varDec) {
            if (!vd.validateTree()) {
                return false;
            }
        }
        return this.bodyNode != null && this.bodyNode.validateTree();
    }
}
