/**
 * File name: ExprNode.java
 * Author: Alvin Jiang and Teju Rajbabu and Kifekachukwu Nwosu
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
       if (stringLiteral != null) {
        return stringLiteral.getToken();
    }
    if (boolNode != null) {
        return boolNode.convertToJava(className);
    }
    if (operator != null) {
        // Java compares String contents with equals/compareTo; the operators
        // would compare references, and the ordering ones do not compile at all
        if (comparesStrings()) {
            String left = leftOperand.convertToJava(className);
            String right = rightOperand.convertToJava(className);
            if (operator.getToken().equals("==")) {
                return left + ".equals(" + right + ")";
            }
            if (operator.getToken().equals("!=")) {
                return "!" + left + ".equals(" + right + ")";
            }
            return left + ".compareTo(" + right + ") " + operator.getToken() + " 0";
        }
        return leftOperand.convertToJava(className) + " "
                + operator.getToken() + " "
                + rightOperand.convertToJava(className);
    }
    return leftOperand.convertToJava(className);
}

    /**
     * @return true if this is a relational comparison of two Strings, which
     *         Java and C cannot express with the operator alone
     */
    private boolean comparesStrings() {
        return operator != null
                && operator.getTokenType() == TokenType.REL_OP
                && "String".equals(leftOperand.getType())
                && "String".equals(rightOperand.getType());
    }

    @Override
    public String convertToC(){
    if (stringLiteral != null) {
        return stringLiteral.getToken();
    }
    if (boolNode != null) {
        return boolNode.convertToC();
    }
    if (operator != null) {
        // C compares char pointers with the operators, so string contents are
        // compared through strcmp instead
        if (comparesStrings()) {
            return "strcmp(" + leftOperand.convertToC() + ", "
                    + rightOperand.convertToC() + ") "
                    + operator.getToken() + " 0";
        }
        return leftOperand.convertToC() + " "
                + operator.getToken() + " "
                + rightOperand.convertToC();
    }
    return leftOperand.convertToC();    }
    @Override
    public String convertToPython(){
    if (stringLiteral != null) {
        return stringLiteral.getToken();
    }
    if (boolNode != null) {
        return boolNode.convertToPython();
    }
    if (operator != null) {
        String expression = leftOperand.convertToPython() + " "
                + operator.getToken() + " "
                + rightOperand.convertToPython();

        // Jott integer division truncates, but Python's / always produces a
        // float, so an integer quotient is truncated back toward zero to match
        // Jott and the other targets.
        if (operator.getToken().equals("/") && "Integer".equals(getType())) {
            return "int(" + expression + ")";
        }

        return expression;
    }
    return leftOperand.convertToPython();}

    @Override
    public boolean validateTree() {
        // String literal
        if (stringLiteral != null) {
            return true;
        }

        //  Bool literal
        if (boolNode != null) {
            return boolNode.validateTree();
        }

        // Single operand
        if (operator == null) {
            if (leftOperand == null) {
                return false;
            }
            return leftOperand.validateTree();
        }

        // Operator expression 
        if (leftOperand == null || rightOperand == null) {
            return false;
        }
        if (!leftOperand.validateTree() || !rightOperand.validateTree()) {
            return false;
        }

        String leftType = leftOperand.getType();
        String rightType = rightOperand.getType();

        // either type = null, semantic error
        if (leftType == null || rightType == null) {
            System.err.println("Semantic Error:");
            System.err.println("Invalid operand type in expression");
            System.err.println(operator.getFilename() + ":" + operator.getLineNum());
            return false;
        }

        // both must match
        if (operator.getTokenType() == TokenType.REL_OP) {
            if (!leftType.equals(rightType)) {
                System.err.println("Semantic Error:");
                System.err.println("Relational operator '" + operator.getToken()
                        + "' requires operands of the same type");
                System.err.println(operator.getFilename() + ":" + operator.getLineNum());
                return false;
            }
            return true;
        }

        // Both must be numeric
        if (operator.getTokenType() == TokenType.MATH_OP) {
            boolean leftNumeric = leftType.equals("Integer") || leftType.equals("Double");
            boolean rightNumeric = rightType.equals("Integer") || rightType.equals("Double");

            if (!leftNumeric || !rightNumeric) {
                System.err.println("Semantic Error:");
                System.err.println("Math operator '" + operator.getToken()
                        + "' requires numeric operands");
                System.err.println(operator.getFilename() + ":" + operator.getLineNum());
                return false;
            }

            if (operator.getToken().equals("/") && rightOperand.isZeroLiteral()) {
                System.err.println("Semantic Error:");
                System.err.println("Division by zero");
                System.err.println(operator.getFilename() + ":" + operator.getLineNum());
                return false;
            }

            return true;
        }

        // Unknown
        System.err.println("Semantic Error:");
        System.err.println("Invalid operator '" + operator.getToken() + "'");
        System.err.println(operator.getFilename() + ":" + operator.getLineNum());
        return false;
    }


    /**
     * @return true if this expression is a Boolean literal, which some targets
     *         cannot accept everywhere a condition is allowed
     */
    public boolean isBooleanLiteral() {
        return this.boolNode != null;
    }

    public Token getLocationToken() {
        if (stringLiteral != null) {
            return stringLiteral;
        }
        if (leftOperand != null) {
            return leftOperand.getLocationToken();
        }
        return null;
    }

    public String getType() {

        // String literal
        if (this.stringLiteral != null) {
            return "String";
        }

        // Boolean expression
        if (this.boolNode != null) {
            return "Boolean";
        }

        // Single operand
        if (operator == null) {
            return leftOperand.getType();
        }

        String leftType = leftOperand.getType();
        String rightType = rightOperand.getType();

        // Relational operators (<, >, ==, etc.)
        if (operator.getTokenType() == TokenType.REL_OP) {
            return "Boolean";
        }

        // Mathematical operators
        if (operator.getTokenType() == TokenType.MATH_OP) {

            // Integer + Integer -> Integer
            if (leftType.equals("Integer") && rightType.equals("Integer")) {
                return "Integer";
            }

            // Anything involving a Double -> Double
            if ((leftType.equals("Double") && rightType.equals("Double")) ||
                    (leftType.equals("Integer") && rightType.equals("Double")) ||
                    (leftType.equals("Double") && rightType.equals("Integer"))) {
                return "Double";
            }

            // Invalid math expression
            return null;
        }

        return null;
    }
}
