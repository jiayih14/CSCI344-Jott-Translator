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
}
