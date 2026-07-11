/**
 * File name: ReturnStmtNode.java
 * Author: Jiayi Huang
 *
 * This file defines the ReturnStmtNode class, which represents a grammar for return statement
 * in the Jott parse tree shown the following:
 *
 *  Return <expr>; | e
 *
 * The class provides functionality to parse the return statement of a function based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ReturnStmtNode implements JottTree {


    private ExprNode exprNode;

    /**
     * This is a constructor for ReturnStmtNode
     * @param exprNode
     */
    public ReturnStmtNode(ExprNode exprNode){
        this.exprNode = exprNode;
    }

    /**
     * This is a parse function that would parse a return statement
     * @param tokens a list of tokens
     * @return a ReturnStmtNode if parse successfully.
     */
    public static ReturnStmtNode parse(ArrayList<Token> tokens){
            ParserHelper.expectValue(tokens, "Return", "Expected a keyword \"Return\"");
            ReturnStmtNode resultNode = new ReturnStmtNode(ExprNode.parse(tokens));
            ParserHelper.expect(tokens, TokenType.SEMICOLON, "Expected ';' to end assignment statement");
            return resultNode;
    }

    @Override
    public String convertToJott() {
        return "Return " + this.exprNode.convertToJott() + ";";
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

    if (!exprNode.validateTree()) {
        return false;
    }

    FunctionInfo currentFunction = SemanticAnalyzer.getCurrentFunction();

    if (currentFunction == null) {
        System.err.println("Semantic Error:");
        System.err.println("Return statement outside of a function.");
        return false;
    }

    String expected = currentFunction.getReturnType();
    String actual = exprNode.getType();

    if (actual == null) {
        return false;
    }

    if (!expected.equals(actual)) {
        System.err.println("Semantic Error:");
        System.err.println("Return type does not match function return type.");
        return false;
    }

    return true;
}
public String getType() {
    return exprNode.getType();
}
}
