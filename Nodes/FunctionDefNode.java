package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FunctionDefNode implements JottTree {

    private Token funcName;
    private FuncDefParamsNode funcDefParams;
    private FunctionReturnNode funcReturn;
    private FBodyNode fBody;

    /**
     * Constructor for Function Definition Node
     * @param token the function name token
     * @param funcDefParams the parameter list node
     * @param funcReturn the return type node
     * @param fBody the function body node
     */
    public FunctionDefNode (Token token, FuncDefParamsNode funcDefParams,
                                    FunctionReturnNode funcReturn, FBodyNode fBody){
        this.funcName = token;
        this.funcDefParams = funcDefParams;
        this.funcReturn = funcReturn;
        this.fBody = fBody;
    }

    /**
     * parse function for function definition node
     * @param tokens a list of tokens to be consumed
     * @return a FunctionDefNode
     */
    public static FunctionDefNode parse(ArrayList<Token> tokens){
        ParserHelper.expectValue(tokens, "Def", "Expected 'Def' to begin a function definition");
        Token name = ParserHelper.expect(tokens, TokenType.ID_KEYWORD, "Expected a function name after 'Def'");
        ParserHelper.expect(tokens, TokenType.L_BRACKET, "Expected '[' after function name");
        FuncDefParamsNode params = FuncDefParamsNode.parse(tokens);
        ParserHelper.expect(tokens, TokenType.R_BRACKET, "Expected ']' after function parameters");
        ParserHelper.expect(tokens, TokenType.COLON, "Expected ':' after function parameters");
        FunctionReturnNode funcReturn = FunctionReturnNode.parse(tokens);
        ParserHelper.expect(tokens, TokenType.L_BRACE, "Expected '{' to begin function body");
        FBodyNode fBody = FBodyNode.parse(tokens);
        ParserHelper.expect(tokens, TokenType.R_BRACE, "Expected '}' to close function body");

        return new FunctionDefNode(name, params, funcReturn, fBody);
    }

    @Override
    public String convertToJott() {
        return "Def " + funcName.getToken()
                + "[" + funcDefParams.convertToJott() + "]"
                + ":" + funcReturn.convertToJott()
                + "{" + fBody.convertToJott() + "}";
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
