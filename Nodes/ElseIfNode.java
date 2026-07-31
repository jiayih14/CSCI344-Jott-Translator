package Nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;

/**
 * File name: ElseIfNode.java
 * Author: Teju Rajbabu
 *
 * This file defines the ElseIfNode class, which represents the grammar:
 *
 * <elseif> -> Elseif [<expr>] {<body>}
 *
 * The class provides functionality to parse an elseif-statement.
 */

public class ElseIfNode implements JottTree {

    private Token keyword;
    private ExprNode condition;
    private BodyNode body;

    public ElseIfNode(Token keyword, ExprNode condition, BodyNode body) {
        this.keyword = keyword;
        this.condition = condition;
        this.body = body;
    }

    public static ElseIfNode parse(ArrayList<Token> tokens) {
        Token keyword = ParserHelper.expectValue(tokens, "Elseif", "Expected 'Elseif'");

        ParserHelper.expectValue(tokens, "[", "Expected '[' after Elseif");
        ExprNode condition = ExprNode.parse(tokens);
        ParserHelper.expectValue(tokens, "]", "Expected ']' after elseif condition");

        ParserHelper.expectValue(tokens, "{", "Expected '{' to start elseif body");
        BodyNode body = BodyNode.parse(tokens);
        ParserHelper.expectValue(tokens, "}", "Expected '}' to close elseif body");

        return new ElseIfNode(keyword, condition, body);
    }

    @Override
    public String convertToJott() {
        return "Elseif [" + condition.convertToJott() + "] {" + body.convertToJott() + "}";
    }

    @Override
    public String convertToJava(String className) {
        return convertToJava(className, 0);
    }

    /**
     * Generates the Java form of this elseif branch at the given indent level.
     * IfStmtNode appends this directly onto the closing brace of the previous
     * block, so the result starts with a space and is not newline terminated.
     *
     * @param className the enclosing Java class name
     * @param indentLevel the level the enclosing "if" keyword sits at
     * @return the Java code for this branch, joined onto a preceding "}"
     */
    public String convertToJava(String className, int indentLevel) {
        return " else if (" + condition.convertToJava(className) + ") {\n"
                + body.convertToJava(className, indentLevel + 1)
                + indent(indentLevel) + "}";
    }

    @Override
    public String convertToC() {
        return convertToC(0);
    }

    /**
     * Generates the C form of this elseif branch at the given indent level.
     *
     * @param indentLevel the level the enclosing "if" keyword sits at
     * @return the C code for this branch, joined onto a preceding "}"
     */
    public String convertToC(int indentLevel) {
        return " else if (" + condition.convertToC() + ") {\n"
                + body.convertToC(indentLevel + 1)
                + indent(indentLevel) + "}";
    }

    @Override
    public String convertToPython() {
        return convertToPython(0);
    }

    /**
     * Generates the Python form of this elseif branch at the given indent level.
     * Python has no closing brace to attach to, so this branch supplies its own
     * indentation and newline.
     *
     * @param indentLevel the level the enclosing "if" keyword sits at
     * @return the Python code for this branch, newline terminated
     */
    public String convertToPython(int indentLevel) {
        return indent(indentLevel) + "elif " + condition.convertToPython() + ":\n"
                + pythonBody(indentLevel + 1);
    }

    /**
     * Jott allows an empty body, which Python cannot express, so an empty
     * block becomes a single "pass".
     */
    private String pythonBody(int indentLevel) {
        String generated = body.convertToPython(indentLevel);
        if (generated.isBlank()) {
            return indent(indentLevel) + "pass\n";
        }
        return generated;
    }

    private static String indent(int indentLevel) {
        return "    ".repeat(indentLevel);
    }

    @Override
    public boolean validateTree() {
        if (condition == null) {
            return false;
        }
        if (!condition.validateTree()) {
            return false;
        }
        if (!"Boolean".equals(condition.getType())) {
            System.err.println("Semantic Error:");
            System.err.println("Elseif condition must evaluate to Boolean");
            System.err.println(keyword.getFilename() + ":" + keyword.getLineNum());
            return false;
        }
        if (body == null) {
            return false;
        }
        return body.validateTree();
    }

    public boolean guaranteesReturn() {
        return body != null && body.guaranteesReturn();
    }
}
