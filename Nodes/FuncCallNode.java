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

        // Validate argument expressions
        if (!this.params.validateTree()) {
            return false;
        }

        // Does the function exist?
        FunctionInfo function = SemanticAnalyzer.lookupFunction(funcName.getToken());


        if (function == null) {
            System.err.println("Call to unknown function " + this.funcName.getToken());
            return false;
        }

        // Check number of parameters
        int numOfParam = 0;
        if (this.params.getFirstParam() != null){
            numOfParam = 1 + this.params.getAdditionalParams().size();
        }
        if (numOfParam != function.getParameterTypes().size()) {

            System.err.println("Function " + this.funcName.getToken() + " expects "
                            + function.getParameterTypes().size()
                            + " parameter(s)."
                    );

            return false;
        }

        // Check each parameter type
        for (int i = 0; i < numOfParam; i++) {

            String expected = function.getParameterTypes().get(i);

            String actual;

            if (i == 0) {
                actual = params.getFirstParam().getType();
            } else {
                actual = params.getAdditionalParams().get(i - 1).getType();
            }

            if (!expected.equals(actual)) {
                System.err.println("Invalid parameter type in call to " + funcName.getToken());
                return false;
            }
        }
        return true;
    }

    public String getType(){
        return SemanticAnalyzer.lookupFunction(funcName.getToken()).getReturnType();
    }
}
