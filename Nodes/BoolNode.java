/**
 * File name: BoolNode.java
 * Author: Alvin Jiang and Teju Rajbabu
 *
 * This file defines the BoolNode class, which represents a boolean variable
 *  in the Jott parse tree shown the following:
 *
 *  <bool> -> True | False
 *
 * The class provides functionality to parse boolean variable based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class BoolNode implements JottTree {

    private Token boolToken;

    /**
     * Constructor for Bool Node
     * @param boolToken boolean token
     */
    public BoolNode(Token boolToken) {
        this.boolToken = boolToken;
    }

    public static BoolNode parse(ArrayList<Token> tokens) {
        if (!checkIsBool(tokens)) {
            throw ParserHelper.error(tokens, "Expected 'True' or 'False'");
        }
        Token token = tokens.remove(0);
        return new BoolNode(token);
    }

    public static boolean checkIsBool(ArrayList<Token> tokens) {
        return ParserHelper.checkType(tokens, TokenType.ID_KEYWORD)
                && (ParserHelper.checkValue(tokens, "True") || ParserHelper.checkValue(tokens, "False"));
    }

    @Override
    public String convertToJott() {
        return boolToken.getToken();
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
        return "Boolean";
    }

    @Override
    public boolean validateTree() {
        return boolToken != null
            && (boolToken.getToken().equals("True") || boolToken.getToken().equals("False"));
    }

}
