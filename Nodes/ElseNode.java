package Nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;

/**
 * File name: ElseNode.java
 * Author: Teju Rajbabu
 *
 * This file defines the ElseNode class, which represents the grammar:
 *
 * <else> -> Else {<body>}
 *
 * The class provides functionality to parse an else-statement.
 */

public class ElseNode implements JottTree {

    private BodyNode body;

    public ElseNode(BodyNode body) {
        this.body = body;
    }

    public static ElseNode parse(ArrayList<Token> tokens) {
        ParserHelper.expectValue(tokens, "Else", "Expected 'Else'");

        ParserHelper.expectValue(tokens, "{", "Expected '{' after Else");
        BodyNode body = BodyNode.parse(tokens);
        ParserHelper.expectValue(tokens, "}", "Expected '}' to close else body");

        return new ElseNode(body);
    }

    @Override
    public String convertToJott() {
        return "Else {" + body.convertToJott() + "}";
    }

    @Override
    public String convertToJava(String className) {
        return convertToJava(className, 0);
    }

    /**
     * Generates the Java form of this else branch at the given indent level.
     * IfStmtNode appends this directly onto the closing brace of the previous
     * block, so the result starts with a space and is not newline terminated.
     *
     * @param className the enclosing Java class name
     * @param indentLevel the level the enclosing "if" keyword sits at
     * @return the Java code for this branch, joined onto a preceding "}"
     */
    public String convertToJava(String className, int indentLevel) {
        return " else {\n"
                + body.convertToJava(className, indentLevel + 1)
                + indent(indentLevel) + "}";
    }

    @Override
    public String convertToC() {
        return convertToC(0);
    }

    /**
     * Generates the C form of this else branch at the given indent level.
     *
     * @param indentLevel the level the enclosing "if" keyword sits at
     * @return the C code for this branch, joined onto a preceding "}"
     */
    public String convertToC(int indentLevel) {
        return " else {\n"
                + body.convertToC(indentLevel + 1)
                + indent(indentLevel) + "}";
    }

    @Override
    public String convertToPython() {
        return convertToPython(0);
    }

    /**
     * Generates the Python form of this else branch at the given indent level.
     * Python has no closing brace to attach to, so this branch supplies its own
     * indentation and newline.
     *
     * @param indentLevel the level the enclosing "if" keyword sits at
     * @return the Python code for this branch, newline terminated
     */
    public String convertToPython(int indentLevel) {
        return indent(indentLevel) + "else:\n" + pythonBody(indentLevel + 1);
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
        if (body == null) {
            return false;
        }
        return body.validateTree();
    }

    public boolean guaranteesReturn() {
        return body != null && body.guaranteesReturn();
    }
}
