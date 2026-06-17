package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class TypeNode implements JottTree {

    private Token typeToken;

    public TypeNode(Token typeToken) {
        this.typeToken = typeToken;
    }

    public static TypeNode parse(ArrayList<Token> tokens) {
        if (!checkIsType(tokens)) {
            throw ParserHelper.error(tokens, "Expected a type ('Double', 'Integer', 'String', or 'Boolean')");
        }
        return new TypeNode(tokens.remove(0));
    }

    public static boolean checkIsType(ArrayList<Token> tokens) {
        return ParserHelper.checkType(tokens, TokenType.ID_KEYWORD)
                && (ParserHelper.checkValue(tokens, "Double")
                || ParserHelper.checkValue(tokens, "Integer")
                || ParserHelper.checkValue(tokens, "String")
                || ParserHelper.checkValue(tokens, "Boolean"));
    }

    @Override
    public String convertToJott() {
        return typeToken.getToken();
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
