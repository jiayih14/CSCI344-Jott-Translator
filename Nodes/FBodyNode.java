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
    /**
     * Renders the contents of a function body at the level a Java method body
     * sits at: inside the class and inside the method, so two levels deep.
     * FunctionDefNode passes its level explicitly; this is only a fallback.
     */
    @Override
    public String convertToJava(String className){
        return convertToJava(className, 2);
    }

    /**
     * Generates the Java form of this function body at the given indent level.
     *
     * Emits the variable declarations in source order and then the body. Only
     * the contents are produced; the signature and the enclosing braces belong
     * to FunctionDefNode.
     *
     * VarDecNode supplies its own ';' but no indentation and no newline, so
     * those are added here. BodyNode renders its own statements at the level it
     * is given, so its result is appended unchanged.
     *
     * @param className the enclosing Java class name, passed through unchanged
     * @param indentLevel the level this body's declarations and statements sit at
     * @return the Java code for this function body, or "" when it is empty
     */
    public String convertToJava(String className, int indentLevel) {

        StringBuilder sb = new StringBuilder();

        for (VarDecNode vd : this.varDec) {
            sb.append(indent(indentLevel))
              .append(vd.convertToJava(className))
              .append("\n");
        }

        sb.append(this.bodyNode.convertToJava(className, indentLevel));

        return sb.toString();
    }

    /**
     * Renders the contents of a function body at the level a C function body
     * sits at: one level inside the function. FunctionDefNode passes its level
     * explicitly; this is only a fallback.
     */
    @Override
    public String convertToC(){
        return convertToC(1);
    }

    /**
     * Generates the C form of this function body at the given indent level.
     *
     * @param indentLevel the level this body's declarations and statements sit at
     * @return the C code for this function body, or "" when it is empty
     */
    public String convertToC(int indentLevel) {

        StringBuilder sb = new StringBuilder();

        for (VarDecNode vd : this.varDec) {
            sb.append(indent(indentLevel))
              .append(vd.convertToC())
              .append("\n");
        }

        sb.append(this.bodyNode.convertToC(indentLevel));

        return sb.toString();
    }

    /**
     * Renders the contents of a function body at the level a Python function
     * body sits at: one level inside the "def". FunctionDefNode passes its
     * level explicitly; this is only a fallback.
     */
    @Override
    public String convertToPython(){
        return convertToPython(1);
    }

    /**
     * Generates the Python form of this function body at the given indent level.
     *
     * Python has no declaration syntax, so a declaration that converts to
     * nothing contributes no line at all. An empty body returns an empty
     * string; supplying Python's required filler statement for a wholly empty
     * function belongs to FunctionDefNode.
     *
     * @param indentLevel the level this body's declarations and statements sit at
     * @return the Python code for this function body, or "" when it is empty
     */
    public String convertToPython(int indentLevel) {

        StringBuilder sb = new StringBuilder();

        for (VarDecNode vd : this.varDec) {
            String declaration = vd.convertToPython();
            if (declaration == null || declaration.isBlank()) {
                continue;
            }
            sb.append(indent(indentLevel)).append(declaration).append("\n");
        }

        sb.append(this.bodyNode.convertToPython(indentLevel));

        return sb.toString();
    }

    private static String indent(int indentLevel) {
        return "    ".repeat(indentLevel);
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
