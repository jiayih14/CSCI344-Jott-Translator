/**
 * File name: FuncDefParamsTNode.java
 * Author: Alvin Jiang
 *
 * This file defines the FuncDefParamsTNode class, which represents a grammar for parameter declaration
 *  in the Jott parse tree shown the following:
 *
 *  <func_def_params_t> -> ,<id>: <type>
 *
 * The class provides functionality to parse a parameter declaration statement based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FuncDefParamsTNode implements JottTree {

    private Token id;
    private TypeNode type;

    /**
     * Constructor for Function Definition Parameter T Node
     * @param id id token
     * @param type type node
     */
    public FuncDefParamsTNode(Token id, TypeNode type) {
        this.id = id;
        this.type = type;
    }

    public static FuncDefParamsTNode parse(ArrayList<Token> tokens) {
        ParserHelper.expect(tokens, TokenType.COMMA, "Expected ',' before next function parameter");
        Token id = ParserHelper.expect(tokens, TokenType.ID_KEYWORD, "Expected a parameter name after ','");
        ParserHelper.expect(tokens, TokenType.COLON, "Expected ':' after parameter name");
        TypeNode type = TypeNode.parse(tokens);
        return new FuncDefParamsTNode(id, type);
    }

    @Override
    public String convertToJott() {
        return ", " + id.getToken() + ":" + type.convertToJott();
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
