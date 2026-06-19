package Nodes;

import provided.JottTree;
import provided.Token;
import provided.JottParser;

import java.util.ArrayList;

/**
 * File name: IfStmtNode.java
 * Author: Teju Rajbabu, Jiayi Huang
 *
 * This file defines the IfStmtNode class, which represents the grammar:
 *
 * <if_stmt> -> If [<expr>] {<body>} <elseif> * <else>
 *
 * The class provides functionality to parse an if statement based on the grammar.
 * It also allows conversion back to Jott.
 */

public class IfStmtNode implements JottTree {

    private ExprNode condition;
    private BodyNode ifBody;
    private ArrayList<ElseIfNode> elseIfList;
    private ElseNode elseNode;

    public IfStmtNode(ExprNode condition, BodyNode ifBody, 
        ArrayList<ElseIfNode> elseIfList, ElseNode elseNode) {
            this.condition = condition;
            this.ifBody = ifBody;
            this.elseIfList = (elseIfList != null) ? elseIfList : new ArrayList<>();  //this.elseIfList = elseIfList;
            this.elseNode = elseNode;
        }
        public static IfStmtNode parse(ArrayList<Token> tokens) {

            // "If"
            ParserHelper.expectValue(tokens, "If", "Expected 'If'");

            // "["
            ParserHelper.expectValue(tokens, "[", "Expected '[' after If");

            // condition
            ExprNode cond = ExprNode.parse(tokens);

            // "]"
            ParserHelper.expectValue(tokens, "]", "Expected ']' after condition");

            // "{"
            ParserHelper.expectValue(tokens, "{", "Expected '{' to start If body");

            // parse body
            BodyNode body = BodyNode.parse(tokens);

            // optional return
//            ReturnStmtNode ret = null;
//            if (tokens.get(0).getToken().equals("Return")) {
//                ret = ReturnStmtNode.parse(tokens);
//            }

            // "}"
            ParserHelper.expectValue(tokens, "}", "Expected '}' to end If body");

//            BodyNode ifBody = new BodyNode(stmts, ret);

            // parse ElseIf blocks
            ArrayList<ElseIfNode> elseIfs = new ArrayList<>();
            while (ParserHelper.checkValue(tokens, "Elseif")) {
                elseIfs.add(ElseIfNode.parse(tokens));
            }

            // parse optional Else block
//            ElseNode elseNode = null;
//            if (ElseNode.startsElse(tokens)) {
//                elseNode = ElseNode.parse(tokens);
//            }
            ElseNode elseNode = ElseNode.parse(tokens);

            return new IfStmtNode(cond, body, elseIfs, elseNode);
        }



    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();

        sb.append("If[");
        sb.append(condition.convertToJott());
        sb.append("]{");
        sb.append(ifBody.convertToJott());
        sb.append("}");

        for (ElseIfNode e : elseIfList) {
            sb.append(e.convertToJott());
        }

        if (elseNode != null) {
            sb.append(elseNode.convertToJott());
        }

        return sb.toString();
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