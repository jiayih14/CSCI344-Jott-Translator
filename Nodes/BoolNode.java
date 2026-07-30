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
    /**
     * @return the Java form of this literal: true or false
     */
    @Override
    public String convertToJava(String className){
        return isTrue() ? "true" : "false";
    }

    /**
     * C has no boolean literal without stdbool.h, which is not one of the
     * headers this translator emits, so the values become 1 and 0.
     *
     * @return the C form of this literal: 1 or 0
     */
    @Override
    public String convertToC(){
        return isTrue() ? "1" : "0";
    }

    /**
     * @return the Python form of this literal: True or False
     */
    @Override
    public String convertToPython(){
        return isTrue() ? "True" : "False";
    }

    /**
     * @return true if this literal is Jott's True
     */
    private boolean isTrue() {
        return "True".equals(this.boolToken.getToken());
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
