/**
 * File name: ExprNode.java
 * Author: Alvin Jiang and Teju Rajbabu
 *
 * This file defines the ExprNode class, which represents an expression
 *  in the Jott parse tree shown the following:
 *
 *  <expr> -> <operand> | <operand> <relop> <operand> |
 * <operand> <mathop> <operand> | <string_literal> |
 * <bool>
 *
 * The class provides functionality to parse expression statement based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ExprNode implements JottTree {

    private OperandNode leftOperand;
    private Token operator;
    private OperandNode rightOperand;
    private Token stringLiteral;
    private BoolNode boolNode;

    /**
     * Constructor for Expr Node that takes in string token
     * @param stringLiteral string token
     */
    public ExprNode(Token stringLiteral) {
        this.stringLiteral = stringLiteral;
    }


    /**
     * Constructor for Expr Node that takes in boolean node
     * @param boolNode boolean node
     */
    public ExprNode(BoolNode boolNode) {
        this.boolNode = boolNode;
    }

    /**
     * Constructor for Expr Node that takes in operand node
     * @param operand operand node
     */
    public ExprNode(OperandNode operand) {
        this.leftOperand = operand;
    }

    /**
     * Constructor for Expr Node that takes in left operand node, operator token, and right
     * operand node
     * @param leftOperand
     * @param operator
     * @param rightOperand
     */
    public ExprNode(OperandNode leftOperand, Token operator, OperandNode rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public static ExprNode parse(ArrayList<Token> tokens) {
        if (ParserHelper.checkType(tokens, TokenType.STRING)) {
            return new ExprNode(tokens.remove(0));
        }

        if (BoolNode.checkIsBool(tokens)) {
            return new ExprNode(BoolNode.parse(tokens));
        }

        OperandNode left = OperandNode.parse(tokens);

        if (ParserHelper.checkType(tokens, TokenType.REL_OP) || ParserHelper.checkType(tokens, TokenType.MATH_OP)) {
            Token op = tokens.remove(0);
            OperandNode right = OperandNode.parse(tokens);
            return new ExprNode(left, op, right);
        }

        return new ExprNode(left);
    }

    @Override
    public String convertToJott() {
        if (stringLiteral != null) {
            return stringLiteral.getToken();
        }
        if (boolNode != null) {
            return boolNode.convertToJott();
        }
        if (operator != null) {
            return leftOperand.convertToJott() + " " + operator.getToken() + " " + rightOperand.convertToJott();
        }
        return leftOperand.convertToJott();
    }
    @Override
    public String convertToJava(String className){
        return null;
    }

    @Override
    public String convertToC(){
        return null;
    }
    @Override
    public String convertToPython(){
        return null;
    }

    public String getType() {
        if (stringLiteral != null) {
            return "String";
        }

        if (boolNode != null) {
            return "Boolean";
        }

        if (operator == null) {
            return leftOperand.getType();
        }

        String leftType = leftOperand.getType();
        String rightType = rightOperand.getType();

        if (operator.getType() == TokenType.REL_OP) {
            return "Boolean";
        }

        if (operator.getType() == TokenType.MATH_OP) {
            if (leftType.equals("Double") || rightType.equals("Double")) {
                return "Double";
            }
            return "Integer";
        }
        return null;
    }

    @Override
    public boolean validateTree() {

        if (stringLiteral != null) {
            return true;
        }

        if (boolNode != null) {
            return boolNode.validateTree();
        }

        if (operator == null) {
            return leftOperand.validateTree();
        }

        if (!leftOperand.validateTree()) {
            return false;
        }
        if (!rightOperand.validateTree()) {
            return false;
        }

        String leftType = leftOperand.getType();
        String rightType = rightOperand.getType();

        if (leftType.equals("Void") || rightType.equals("Void")) {
            ParserHelper.semanticError(
                "Void cannot be used inside an expression."
            );
            return false;
        }

        if (operator.getType() == TokenType.REL_OP) {
            if (!leftType.equals(rightType)) {
                ParserHelper.semanticError(
                    "Relational operator '" + operator.getToken() +
                    "' requires both operands to have the same type."
                );
                return false;
            }
            return true;
        }

        if (operator.getType() == TokenType.MATH_OP) {
            boolean leftNumeric = leftType.equals("Integer") || leftType.equals("Double");
            boolean rightNumeric = rightType.equals("Integer") || rightType.equals("Double");

            if (!leftNumeric || !rightNumeric) {
                ParserHelper.semanticError(
                    "Math operator '" + operator.getToken() +
                    "' requires numeric operands."
                );
                return false;
            }
            return true;
        }

        ParserHelper.semanticError("Invalid operator in expression.");
        return false;
    }
}   
