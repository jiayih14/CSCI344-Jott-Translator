/**
 * File name: VarDecNode.java
 * Author: Jiayi Huang
 *
 * This file defines the VarDecNode class, which represents a grammar for variable declaration
 * in the Jott parse tree shown the following:
 *
 *  <type> <id>;
 *
 * The class provides functionality to parse variable declaration based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class VarDecNode implements JottTree {

    private TypeNode typeNode;
    private Token id;

    /**
     * This is a constructor for varDecNode
     * @param type
     * @param id
     */
    public VarDecNode(TypeNode type, Token id){
        this.typeNode = type;
        this.id = id;
    }

    /**
     * This is a parse function that will parse variable type declaration statement.
     * @param tokens a list of tokens
     * @return VarDecNode
     */
    public static VarDecNode parse(ArrayList<Token> tokens){
        TypeNode type = TypeNode.parse(tokens);
        Token id = ParserHelper.expect(tokens, TokenType.ID_KEYWORD, "Expected a valid identifier");
        ParserHelper.expect(tokens, TokenType.SEMICOLON, "Expected ';' to end assignment statement");

        return new VarDecNode(type, id);
    }

    @Override
    public String convertToJott() {
        return this.typeNode.convertToJott() + " " + this.id.getToken() + ";";
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
