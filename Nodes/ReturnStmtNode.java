package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ReturnStmtNode implements JottTree {


    private ExprNode exprNode;

    /**
     * This is a constructor for ReturnStmtNode
     * @param exprNode
     */
    public ReturnStmtNode(ExprNode exprNode){
        this.exprNode = exprNode;
    }

    /**
     * This is a parse function that would parse a return statement
     * @param tokens a list of tokens
     * @return a ReturnStmtNode if parse successfully.
     */
    public static ReturnStmtNode parse(ArrayList<Token> tokens){
            ParserHelper.expectValue(tokens, "Return", "Expected a keyword \"Return\"");
            ReturnStmtNode resultNode = new ReturnStmtNode(ExprNode.parse(tokens));
            ParserHelper.expect(tokens, TokenType.SEMICOLON, "Expected ';' to end assignment statement");
            return resultNode;
    }

    @Override
    public String convertToJott() {
        return "Return" + this.exprNode.convertToJott() + ";";
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
