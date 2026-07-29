package Nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;

/**
 * File name: WhileNode.java
 * Author: Teju Rajbabu
 *
 * This file defines the WhileNode class, which represents the grammar:
 *
 * <while_loop> -> While [<expr>] {<body>}
 *
 * The class provides functionality to parse a while loop.
 */


public class WhileNode implements JottTree {

    private Token keyword;
    private ExprNode condition;
    private BodyNode body;

    public WhileNode(Token keyword, ExprNode condition, BodyNode body) {
        this.keyword = keyword;
        this.condition = condition;
        this.body = body;
    }

    public static WhileNode parse(ArrayList<Token> tokens) {
        Token keyword = ParserHelper.expectValue(tokens, "While", "Expected 'While'");

        ParserHelper.expectValue(tokens, "[", "Expected '[' after While");
        ExprNode condition = ExprNode.parse(tokens);
        ParserHelper.expectValue(tokens, "]", "Expected ']' after while condition");

        ParserHelper.expectValue(tokens, "{", "Expected '{' to start while body");
        BodyNode body = BodyNode.parse(tokens);
        ParserHelper.expectValue(tokens, "}", "Expected '}' to close while body");

        return new WhileNode(keyword, condition, body);
    }

    @Override
    public String convertToJott() {
        return "While [" + condition.convertToJott() + "] {" + body.convertToJott() + "}";
    }

    @Override
    public String convertToJava(String className) {
        return convertToJava(className, 0);
    }

    /**
     * Generates the Java form of this while loop at the given indent level.
     * The body is generated one level deeper.
     *
     * @param className the enclosing Java class name
     * @param indentLevel the level the "while" keyword itself sits at
     * @return the Java code for this while loop, newline terminated
     */
    public String convertToJava(String className, int indentLevel) {
        String pad = indent(indentLevel);
        return pad + "while (" + condition.convertToJava(className) + ") {\n"
                + body.convertToJava(className, indentLevel + 1)
                + pad + "}\n";
    }

    @Override
    public String convertToC() {
        return convertToC(0);
    }

    /**
     * Generates the C form of this while loop at the given indent level.
     *
     * @param indentLevel the level the "while" keyword itself sits at
     * @return the C code for this while loop, newline terminated
     */
    public String convertToC(int indentLevel) {
        String pad = indent(indentLevel);
        return pad + "while (" + condition.convertToC() + ") {\n"
                + body.convertToC(indentLevel + 1)
                + pad + "}\n";
    }

    @Override
    public String convertToPython() {
        return convertToPython(0);
    }

    /**
     * Generates the Python form of this while loop at the given indent level.
     *
     * @param indentLevel the level the "while" keyword itself sits at
     * @return the Python code for this while loop, newline terminated
     */
    public String convertToPython(int indentLevel) {
        return indent(indentLevel) + "while " + condition.convertToPython() + ":\n"
                + pythonBody(body, indentLevel + 1);
    }

    /**
     * Jott allows an empty body, which Python cannot express, so an empty
     * block becomes a single "pass".
     */
    private static String pythonBody(BodyNode body, int indentLevel) {
        String generated = body.convertToPython(indentLevel);
        if (generated.isBlank()) {
            return indent(indentLevel) + "pass\n";
        }
        return generated;
    }

    private static String indent(int indentLevel) {
        return "    ".repeat(indentLevel);
    }

    public boolean guaranteesReturn() {
        return false; // spec: a while loop never guarantees a function return
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
            System.err.println("While condition must evaluate to Boolean");
            System.err.println(keyword.getFilename() + ":" + keyword.getLineNum());
            return false;
        }
        if (body == null) {
            return false;
        }
        return body.validateTree();
    }
}
