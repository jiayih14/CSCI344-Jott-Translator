/**
 * File name: FuncCallNode.java
 * Author: Alvin Jiang
 *
 * This file defines the FuncCallNode class, which represents a grammar for function call
 *  in the Jott parse tree shown the following:
 *
 *  <func_call> -> :: <id>[<params>]
 *
 * The class provides functionality to parse a function call statement based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FuncCallNode implements JottTree {

    private Token funcName;
    private ParamsNode params;

    /**
     * Constructor for FuncCall Node
     * @param funcName function name token
     * @param params parameter node
     */
    public FuncCallNode(Token funcName, ParamsNode params) {
        this.funcName = funcName;
        this.params = params;
    }

    public static FuncCallNode parse(ArrayList<Token> tokens) {
        ParserHelper.expect(tokens, TokenType.FC_HEADER, "Expected '::' to start a function call");
        Token funcName = ParserHelper.expect(tokens, TokenType.ID_KEYWORD, "Expected a function name after '::'");
        ParserHelper.expect(tokens, TokenType.L_BRACKET, "Expected '[' after function name");
        ParamsNode params = ParamsNode.parse(tokens);
        ParserHelper.expect(tokens, TokenType.R_BRACKET, "Expected ']' to close function call");
        return new FuncCallNode(funcName, params);
    }

    @Override
    public String convertToJott() {
        return "::" + funcName.getToken() + "[" + params.convertToJott() + "]";
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
        return false;
    }
}
