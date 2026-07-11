/**
 * File name: OperandNode.java
 * Author: Alvin Jiang and Teju Rajbabu
 *
 * This file defines the OperandNode class, which represents a grammar for operand statement or function call
 * in the Jott parse tree shown the following:
 *
 *  <operand> -> <id> | <num> | <func_call> | -<num>
 *
 * The class provides functionality to parse a parameter declaration statement based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class OperandNode implements JottTree {

    private Token idOrNum;
    private boolean negative;
    private FuncCallNode funcCall;

    /**
     * Constructor for Operand Node that takes in id/num and sign
     * @param idOrNum id or num token
     * @param negative bool for sign
     */
    public OperandNode(Token idOrNum, boolean negative) {
        this.idOrNum = idOrNum;
        this.negative = negative;
    }

    /**
     * Constructor for Operand Node that takes in function call node
     * @param funcCall function call node
     */
    public OperandNode(FuncCallNode funcCall) {
        this.funcCall = funcCall;
    }

    public static OperandNode parse(ArrayList<Token> tokens) {
        if (ParserHelper.checkType(tokens, TokenType.FC_HEADER)) {
            return new OperandNode(FuncCallNode.parse(tokens));
        }

        if (ParserHelper.checkType(tokens, TokenType.MATH_OP) && ParserHelper.checkValue(tokens, "-")) {
            tokens.remove(0);
            Token num = ParserHelper.expect(tokens, TokenType.NUMBER, "Expected a number after '-'");
            return new OperandNode(num, true);
        }

        if (ParserHelper.checkType(tokens, TokenType.NUMBER)) {
            return new OperandNode(tokens.remove(0), false);
        }

        if (ParserHelper.checkType(tokens, TokenType.ID_KEYWORD)) {
            return new OperandNode(tokens.remove(0), false);
        }

        throw ParserHelper.error(tokens, "Expected an identifier, number, negative number, or function call");
    }

    @Override
    public String convertToJott() {
        if (funcCall != null) {
            return funcCall.convertToJott();
        }
        return (negative ? "-" : "") + idOrNum.getToken();
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

 @Override
public boolean validateTree() {

    // Function call
    if (funcCall != null) {
        return funcCall.validateTree();
    }

    // Identifier
    if (idOrNum.getTokenType() == TokenType.ID_KEYWORD) {

        VariableInfo variable =
                SemanticAnalyzer.lookupVariable(idOrNum.getToken());

        if (variable == null) {
            System.err.println("Semantic Error:");
            System.err.println("Undefined variable " + idOrNum.getToken());
            System.err.println(idOrNum.getFilename() + ":" + idOrNum.getLineNum());
            return false;
        }

        if (!variable.isInitialized()) {
            System.err.println("Semantic Error:");
            System.err.println("Variable " + idOrNum.getToken()
                    + " used before initialization.");
            System.err.println(idOrNum.getFilename() + ":" + idOrNum.getLineNum());
            return false;
        }

        return true;
    }

    // Number literal
    if (idOrNum.getTokenType() == TokenType.NUMBER) {
        return true;
    }

    return false;
}

    public String getType() {

        // Function call
        if (funcCall != null) {
            return funcCall.getType();
        }

        // Identifier
        if (idOrNum.getTokenType() == TokenType.ID_KEYWORD) {

            VariableInfo variable =
                    SemanticAnalyzer.lookupVariable(idOrNum.getToken());

            if (variable == null) {
                return null;
            }

            return variable.getType();
        }

        // Number
        if (idOrNum.getTokenType() == TokenType.NUMBER) {

            String number = idOrNum.getToken();

            if (number.contains(".")) {
                return "Double";
            }

            return "Integer";
        }

        return null;
    }
}
