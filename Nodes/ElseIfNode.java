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

    private JottTree condition;
    private BodyNode body;

    public ElseIfNode(JottTree condition, BodyNode body) {
        this.condition = condition;
        this.body = body;
    }

    public static ElseIfNode parse(ArrayList<Token> tokens) {
        ParserHelper.expectValue(tokens, "Elseif", "Expected 'Elseif'");

        ParserHelper.expectValue(tokens, "[", "Expected '[' after Elseif");
        JottTree condition = ExprNode.parse(tokens);
        ParserHelper.expectValue(tokens, "]", "Expected ']' after elseif condition");

        ParserHelper.expectValue(tokens, "{", "Expected '{' to start elseif body");
        BodyNode body = BodyNode.parse(tokens);
        ParserHelper.expectValue(tokens, "}", "Expected '}' to close elseif body");

        return new ElseIfNode(condition, body);
    }

    @Override
    public String convertToJott() {
        return "Elseif [" + condition.convertToJott() + "] {" + body.convertToJott() + "}";
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
