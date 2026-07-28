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
    public String convertToPython() {
        return convertToPython(1);
    }

    public String convertToPython(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = "    ".repeat(indentLevel);

        for (VarDecNode vd : varDec) {
            sb.append(indent)
              .append(vd.convertToPython())
              .append("\n");
        }

        sb.append(bodyNode.convertToPython(indentLevel));

        return sb.toString();
    }

    @Override
    public String convertToJava(String className) {
        return convertToJava(1);
    }

    public String convertToJava(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = "    ".repeat(indentLevel);

        for (VarDecNode vd : varDec) {
            sb.append(indent)
              .append(vd.convertToJava())
              .append(";\n");
        }

        sb.append(bodyNode.convertToJava(indentLevel));

        return sb.toString();
    }

    @Override
    public String convertToC() {
        return convertToC(1);
    }

    public String convertToC(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = "    ".repeat(indentLevel);

        for (VarDecNode vd : varDec) {
            sb.append(indent)
              .append(vd.convertToC())
              .append(";\n");
        }

        sb.append(bodyNode.convertToC(indentLevel));

        return sb.toString();
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

    public boolean hasReturnStatement() {
        return bodyNode != null && bodyNode.hasReturnStatement();
    }

    public boolean guaranteesReturn() {
        return bodyNode != null && bodyNode.guaranteesReturn();
    }
}
