/**
 * File name: AsmtNode.java
 * Author: Alvin Jiang and Teju Rajbabu
 *
 * This file defines the AsmtNode class, which represents an assignment
 * statement in the Jott parse tree shown the following:
 *
 * <id>=< expr>
 *
 * The class provides functionality to parse assignment statements based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;


public class AsmtNode implements JottTree {

    private Token id;
    private ExprNode expr;

    /**
     * Constructor for AsmtNode Node
     * @param id id token
     * @param expr expression node
     */
    public AsmtNode(Token id, ExprNode expr) {
        this.id = id;
        this.expr = expr;
    }

    public static AsmtNode parse(ArrayList<Token> tokens) {
        Token id = ParserHelper.expect(tokens, TokenType.ID_KEYWORD, "Expected an identifier at the start of an assignment");
        ParserHelper.expect(tokens, TokenType.ASSIGN, "Expected '=' in assignment");
        ExprNode expr = ExprNode.parse(tokens);
        ParserHelper.expect(tokens, TokenType.SEMICOLON, "Expected ';' to end assignment statement");
        return new AsmtNode(id, expr);
    }

    @Override
    public String convertToJott() {
        return id.getToken() + " = " + expr.convertToJott() + ";";
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

        if (id == null || expr == null) {
            return false;
        }

        if (!expr.validateTree()) {
            return false;
        }

        VariableInfo var = SemanticAnalyzer.lookupVariable(id.getToken());

        if (var == null) {
            System.err.println("Semantic Error:");
            System.err.println("Use of undefined variable " + id.getToken());
            System.err.println(id.getFilename() + ":" + id.getLineNum());
            return false;
        }

        String declaredType = var.getType();

        String exprType = expr.getType();

        if ("Void".equals(exprType)) {
            System.err.println("Semantic Error:");
            System.err.println("Cannot assign Void to variable " + id.getToken());
            System.err.println(id.getFilename() + ":" + id.getLineNum());
            return false;
        }

        if (!declaredType.equals(exprType)) {
            System.err.println("Semantic Error:");
            System.err.println("Invalid type in assignment to " + id.getToken());
            System.err.println("Expected: " + declaredType + ", Found: " + exprType);
            System.err.println(id.getFilename() + ":" + id.getLineNum());
            return false;
        }

        SemanticAnalyzer.markInitialized(id.getToken());

        return true;
    }


}
