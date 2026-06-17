package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ParamsTNode implements JottTree {

    private ExprNode expr;

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
