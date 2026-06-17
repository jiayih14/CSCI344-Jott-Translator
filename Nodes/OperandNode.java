package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class OperandNode implements JottTree {

    private Token idOrNum;
    private boolean negative;
    private FuncCallNode funcCall;

    public OperandNode(Token idOrNum, boolean negative) {
        this.idOrNum = idOrNum;
        this.negative = negative;
    }

    public OperandNode(FuncCallNode funcCall) {
        this.funcCall = funcCall;
    }

    public static OperandNode parse(ArrayList<Token> tokens) {
        if (ParserHelper.checkType(tokens, TokenType.FC_HEADER)) {
            return new OperandNode(FuncCallNode.parse(tokens));
        }

        if (ParserHelper.checkType(tokens, TokenType.MATH_OP) && ParserHelper.checkValue(tokens, "-")) {
            tokens.remove(0);
            Token num = ParserHelper.expect(tokens, TokenType.NUMBER, "Expected a number after '-'");
            return new OperandNode(num, true);
        }

        if (ParserHelper.checkType(tokens, TokenType.NUMBER)) {
            return new OperandNode(tokens.remove(0), false);
        }

        if (ParserHelper.checkType(tokens, TokenType.ID_KEYWORD)) {
            return new OperandNode(tokens.remove(0), false);
        }

        throw ParserHelper.error(tokens, "Expected an identifier, number, negative number, or function call");
    }

    @Override
    public String convertToJott() {
        if (funcCall != null) {
            return funcCall.convertToJott();
        }
        return (negative ? "-" : "") + idOrNum.getToken();
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
