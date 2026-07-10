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
        if (body == null) {
            return false;
        }
        return body.validateTree();
    }
}
