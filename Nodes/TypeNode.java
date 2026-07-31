/**
 * File name: TypeNode.java
 * Author: Alvin Jiang
 *
 * This file defines the TypeNode class, which represents a grammar for type declaration
 * in the Jott parse tree shown the following:
 *
 * Double | Integer | String | Boolean
 *
 * The class provides functionality to parse type declaration based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class TypeNode implements JottTree {

    private Token typeToken;

    /**
     * Constructor for Type Node
     * @param typeToken type token
     */
    public TypeNode(Token typeToken) {
        this.typeToken = typeToken;
    }

    public static TypeNode parse(ArrayList<Token> tokens) {
        if (!checkIsType(tokens)) {
            throw ParserHelper.error(tokens, "Expected a type ('Double', 'Integer', 'String', or 'Boolean')");
        }
        return new TypeNode(tokens.remove(0));
    }

    public static boolean checkIsType(ArrayList<Token> tokens) {
        return ParserHelper.checkType(tokens, TokenType.ID_KEYWORD)
                && (ParserHelper.checkValue(tokens, "Double")
                || ParserHelper.checkValue(tokens, "Integer")
                || ParserHelper.checkValue(tokens, "String")
                || ParserHelper.checkValue(tokens, "Boolean"));
    }

    public String getType(){
        return this.typeToken.getToken();
    }

    @Override
    public String convertToJott() {
        return typeToken.getToken();
    }
    @Override
    public String convertToJava(String className){
        return switch (this.typeToken.getToken()) {
            case "Integer" -> "int";
            case "Double" -> "double";
            case "String" -> "String";
            case "Boolean" -> "boolean";
            // parsing and validation admit only the four types above
            default -> this.typeToken.getToken();
        };
    }

    /**
     * C has no string or boolean type of its own: strings are character
     * pointers, and Boolean becomes int because stdbool.h is not among the
     * headers this translator emits and BoolNode already yields 1 and 0.
     */
    @Override
    public String convertToC(){
        return switch (this.typeToken.getToken()) {
            case "Integer" -> "int";
            case "Double" -> "double";
            case "String" -> "char *";
            case "Boolean" -> "int";
            // parsing and validation admit only the four types above
            default -> this.typeToken.getToken();
        };
    }
    @Override
    public String convertToPython(){
        return "";
    }

    @Override
    public boolean validateTree() {
        if (typeToken == null) {
            return false;
        }
        String val = typeToken.getToken();
        return "Integer".equals(val)
                || "Double".equals(val)
                || "String".equals(val)
                || "Boolean".equals(val);
    }
}
