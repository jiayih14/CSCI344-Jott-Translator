package Nodes;

import provided.JottTree;
import provided.Token;

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

    private Token keyword;
    private ExprNode condition;
    private BodyNode ifBody;
    private ArrayList<ElseIfNode> elseIfList;
    private ElseNode elseNode;

    public IfStmtNode(Token keyword, ExprNode condition, BodyNode ifBody,
        ArrayList<ElseIfNode> elseIfList, ElseNode elseNode) {
            this.keyword = keyword;
            this.condition = condition;
            this.ifBody = ifBody;
            this.elseIfList = (elseIfList != null) ? elseIfList : new ArrayList<>();
            this.elseNode = elseNode;
        }
        public static IfStmtNode parse(ArrayList<Token> tokens) {

            // "If"
            Token keyword = ParserHelper.expectValue(tokens, "If", "Expected 'If'");

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

            // "}"
            ParserHelper.expectValue(tokens, "}", "Expected '}' to end If body");

            // parse ElseIf blocks
            ArrayList<ElseIfNode> elseIfs = new ArrayList<>();
            while (ParserHelper.checkValue(tokens, "Elseif")) {
                elseIfs.add(ElseIfNode.parse(tokens));
            }

            // Else is optional per the grammar: <else> -> Else{<body>} | e
            ElseNode elseNode = null;
            if (ParserHelper.checkValue(tokens, "Else")) {
                elseNode = ElseNode.parse(tokens);
            }

            return new IfStmtNode(keyword, cond, body, elseIfs, elseNode);
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
        return convertToJava(className, 0);
    }

    /**
     * Generates the Java form of this if statement at the given indent level.
     * The body is generated one level deeper; the elseif/else branches stay at
     * the same level as the "if" so they line up with it.
     *
     * @param className the enclosing Java class name
     * @param indentLevel the level the "if" keyword itself sits at
     * @return the Java code for this if statement, newline terminated
     */
    public String convertToJava(String className, int indentLevel) {
        String pad = indent(indentLevel);
        StringBuilder sb = new StringBuilder();

        sb.append(pad).append("if (").append(condition.convertToJava(className)).append(") {\n");
        sb.append(ifBody.convertToJava(className, indentLevel + 1));
        sb.append(pad).append("}");

        // Each branch supplies its own leading " else if" / " else" so it joins
        // onto the closing brace above rather than starting a new line.
        for (ElseIfNode e : elseIfList) {
            sb.append(e.convertToJava(className, indentLevel));
        }

        if (elseNode != null) {
            sb.append(elseNode.convertToJava(className, indentLevel));
        }

        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String convertToC() {
        return convertToC(0);
    }

    /**
     * Generates the C form of this if statement at the given indent level.
     *
     * @param indentLevel the level the "if" keyword itself sits at
     * @return the C code for this if statement, newline terminated
     */
    public String convertToC(int indentLevel) {
        String pad = indent(indentLevel);
        StringBuilder sb = new StringBuilder();

        sb.append(pad).append("if (").append(condition.convertToC()).append(") {\n");
        sb.append(ifBody.convertToC(indentLevel + 1));
        sb.append(pad).append("}");

        for (ElseIfNode e : elseIfList) {
            sb.append(e.convertToC(indentLevel));
        }

        if (elseNode != null) {
            sb.append(elseNode.convertToC(indentLevel));
        }

        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String convertToPython() {
        return convertToPython(0);
    }

    /**
     * Generates the Python form of this if statement at the given indent level.
     *
     * @param indentLevel the level the "if" keyword itself sits at
     * @return the Python code for this if statement, newline terminated
     */
    public String convertToPython(int indentLevel) {
        String pad = indent(indentLevel);
        StringBuilder sb = new StringBuilder();

        sb.append(pad).append("if ").append(condition.convertToPython()).append(":\n");
        sb.append(pythonBody(ifBody, indentLevel + 1));

        for (ElseIfNode e : elseIfList) {
            sb.append(e.convertToPython(indentLevel));
        }

        if (elseNode != null) {
            sb.append(elseNode.convertToPython(indentLevel));
        }

        return sb.toString();
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
            System.err.println("If condition must evaluate to Boolean");
            System.err.println(keyword.getFilename() + ":" + keyword.getLineNum());
            return false;
        }
        if (ifBody == null) {
            return false;
        }
        if (!ifBody.validateTree()) {
            return false;
        }
        for (ElseIfNode e : elseIfList) {
            if (!e.validateTree()) {
                return false;
            }
        }
        if (elseNode != null) {
            if (!elseNode.validateTree()) {
                return false;
            }
        }
        return true;
    }

    public boolean guaranteesReturn() {
        if (elseNode == null) {
            return false;
        }
        if (!ifBody.guaranteesReturn()) {
            return false;
        }
        for (ElseIfNode e : elseIfList) {
            if (!e.guaranteesReturn()) {
                return false;
            }
        }
        return elseNode.guaranteesReturn();
    }
}
