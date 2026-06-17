package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ParamsNode implements JottTree {

    private ExprNode firstParam;
    private ArrayList<ParamsTNode> additionalParams;

    public ParamsNode(ExprNode firstParam, ArrayList<ParamsTNode> additionalParams) {
        this.firstParam = firstParam;
        this.additionalParams = additionalParams;
    }

    public static ParamsNode parse(ArrayList<Token> tokens) {
        ArrayList<ParamsTNode> additionalParams = new ArrayList<>();

        if (ParserHelper.checkType(tokens, TokenType.R_BRACKET)) {
            return new ParamsNode(null, additionalParams);
        }

        ExprNode firstParam = ExprNode.parse(tokens);

        while (ParserHelper.checkType(tokens, TokenType.COMMA)) {
            additionalParams.add(ParamsTNode.parse(tokens));
        }

        return new ParamsNode(firstParam, additionalParams);
    }

    @Override
    public String convertToJott() {
        if (firstParam == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(firstParam.convertToJott());
        for (ParamsTNode paramsT : additionalParams) {
            sb.append(paramsT.convertToJott());
        }
        return sb.toString();
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
