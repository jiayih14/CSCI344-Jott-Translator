/**
 * File name: ParseHelper.java
 * Author: Alvin Jiang
 *
 * This file defines the ParseHelper class, which provide methods like peek, checkType, checkValue, expect
 * , expectValue, and error function for reusability of other parse function.
 */

package Nodes;

import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ParserHelper {

    private ParserHelper() {}

    /**
     * This function allows us to see the first token of the token list without actually consuming it.
     * @param tokens a list of token
     * @return first token
     */
    public static Token peek(ArrayList<Token> tokens) {
        return tokens.isEmpty() ? null : tokens.get(0);
    }

    /**
     * Check if the first token matches with the desired type
     * @param tokens token list
     * @param type token type that we want to check
     * @return T/F
     */
    public static boolean checkType(ArrayList<Token> tokens, TokenType type) {
        Token token = peek(tokens);
        return token != null && token.getTokenType() == type;
    }

    /**
     * Check if the first token matches with the desired value
     * @param tokens token list
     * @param value value that we want to match
     * @return T/F
     */
    public static boolean checkValue(ArrayList<Token> tokens, String value) {
        Token token = peek(tokens);
        return token != null && token.getToken().equals(value);
    }

    /**
     * This function allows us to check if the first token is desired type
     * If yes, return the token and remove it from token list
     * Otherwise, throw error with desired error message
     * @param tokens token list
     * @param type token type
     * @param errorMessage desired error message
     * @return consumed token
     */
    public static Token expect(ArrayList<Token> tokens, TokenType type, String errorMessage) {
        if (!checkType(tokens, type)) {
            throw error(tokens, errorMessage);
        }
        return tokens.remove(0);
    }


    /**
     * This function allows us to check if the first token matches with a certain string value or not
     * If yes, return the token and remove it from token list
     * Otherwise, throw error with desired error message
     * @param tokens token list
     * @param value string value
     * @param errorMessage desired error message
     * @return consumed token
     */
    public static Token expectValue(ArrayList<Token> tokens, String value, String errorMessage) {
        if (!checkValue(tokens, value)) {
            throw error(tokens, errorMessage);
        }
        return tokens.remove(0);
    }

    /**
     * This function allows us to throw error/exception when there is grammatical error
     * @param tokens a list of token
     * @param message error message that we wished to display
     * @return JottParseException
     */
    public static JottParseException error(ArrayList<Token> tokens, String message) {
        Token found = peek(tokens);
        String location = (found == null)
                ? "end of file"
                : found.getFilename() + ":" + found.getLineNum();
        return new JottParseException("Syntax Error\n" + message + "\n" + location);
    }
}
