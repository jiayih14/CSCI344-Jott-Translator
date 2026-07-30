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
    /**
     * Renders one additional argument, separator included. This node owns the
     * ", " just as it does for Jott, matching <params_t> -> ,<expr>.
     *
     * @param className the enclosing Java class name, passed through unchanged
     * @return this argument preceded by its ", " separator
     */
    @Override
    public String convertToJava(String className){
        return ", " + expr.convertToJava(className);
    }

    /**
     * @return this argument preceded by its ", " separator, in C
     */
    @Override
    public String convertToC(){
        return ", " + expr.convertToC();
    }

    /**
     * @return this argument preceded by its ", " separator, in Python
     */
    @Override
    public String convertToPython(){
        return ", " + expr.convertToPython();
    }

    @Override
    public boolean validateTree() {
    return expr.validateTree();
    }

    public String getType(){
        return this.expr.getType();
    }

    /**
     * @return the expression this argument holds, for callers that need the
     *         argument itself rather than its rendered parameter list fragment
     */
    public ExprNode getExpr(){
        return this.expr;
    }

    public Token getLocationToken(){
        return this.expr.getLocationToken();
    }
}
