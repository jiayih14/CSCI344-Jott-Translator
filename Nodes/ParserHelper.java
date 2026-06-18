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

    public static Token peek(ArrayList<Token> tokens) {
        return tokens.isEmpty() ? null : tokens.get(0);
    }

    public static boolean checkType(ArrayList<Token> tokens, TokenType type) {
        Token token = peek(tokens);
        return token != null && token.getTokenType() == type;
    }

    public static boolean checkValue(ArrayList<Token> tokens, String value) {
        Token token = peek(tokens);
        return token != null && token.getToken().equals(value);
    }

    public static Token expect(ArrayList<Token> tokens, TokenType type, String errorMessage) {
        if (!checkType(tokens, type)) {
            throw error(tokens, errorMessage);
        }
        return tokens.remove(0);
    }

    public static Token expectValue(ArrayList<Token> tokens, String value, String errorMessage) {
        if (!checkValue(tokens, value)) {
            throw error(tokens, errorMessage);
        }
        return tokens.remove(0);
    }

    public static JottParseException error(ArrayList<Token> tokens, String message) {
        Token found = peek(tokens);
        String location = (found == null)
                ? "end of file"
                : found.getFilename() + ":" + found.getLineNum();
        return new JottParseException("Syntax Error\n" + message + "\n" + location);
    }
}
