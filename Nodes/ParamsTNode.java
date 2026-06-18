/**
 * File name: ParamsTNode.java
 * Author: Alvin Jiang
 *
 * This file defines the ParamsTNode class, which represents a grammar
 * in the Jott parse tree shown the following:
 *
 *  <params_t> -> ,<expr>
 *
 * The class provides functionality to parse based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ParamsTNode implements JottTree {

    private ExprNode expr;

    /**
     * Constructor for Params T Node
     * @param expr expr node
     */
    public ParamsTNode(ExprNode expr) {
        this.expr = expr;
    }

    public static ParamsTNode parse(ArrayList<Token> tokens) {
        ParserHelper.expect(tokens, TokenType.COMMA, "Expected ',' before next parameter");
        ExprNode expr = ExprNode.parse(tokens);
        return new ParamsTNode(expr);
    }

    @Override
    public String convertToJott() {
        return ", " + expr.convertToJott();
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
