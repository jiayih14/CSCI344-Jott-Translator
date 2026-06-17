package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FuncDefParamsTNode implements JottTree {

    private Token id;
    private TypeNode type;

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
