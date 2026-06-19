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

    private JottTree condition;
    private BodyNode body;

    public WhileNode(JottTree condition, BodyNode body) {
        this.condition = condition;
        this.body = body;
    }

    public static WhileNode parse(ArrayList<Token> tokens) {
        ParserHelper.expectValue(tokens, "While", "Expected 'While'");

        ParserHelper.expectValue(tokens, "[", "Expected '[' after While");
        JottTree condition = ExprNode.parse(tokens);
        ParserHelper.expectValue(tokens, "]", "Expected ']' after while condition");

        ParserHelper.expectValue(tokens, "{", "Expected '{' to start while body");
        BodyNode body = BodyNode.parse(tokens);
        ParserHelper.expectValue(tokens, "}", "Expected '}' to close while body");

        return new WhileNode(condition, body);
    }

    @Override
    public String convertToJott() {
        return "While [" + condition.convertToJott() + "] {" + body.convertToJott() + "}";
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
