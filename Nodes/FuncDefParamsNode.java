package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FuncDefParamsNode implements JottTree {

    private Token id;
    private TypeNode type;
    private ArrayList<FuncDefParamsTNode> rest;

    /**
     * Constructor for Function Definition Params Node.
     * An empty parameter list is represented by a null id.
     * @param id the first parameter name, or null when there are no parameters
     * @param type the first parameter type, or null when there are no parameters
     * @param rest the remaining comma separated parameters
     */
    public FuncDefParamsNode(Token id, TypeNode type, ArrayList<FuncDefParamsTNode> rest) {
        this.id = id;
        this.type = type;
        this.rest = rest;
    }

    /**
     * parse function for function definition params node
     * @param tokens a list of tokens to be consumed
     * @return a FuncDefParamsNode
     */
    public static FuncDefParamsNode parse(ArrayList<Token> tokens) {
        ArrayList<FuncDefParamsTNode> rest = new ArrayList<>();

        // empty parameter list: the next token closes the bracket
        if (ParserHelper.checkType(tokens, TokenType.R_BRACKET)) {
            return new FuncDefParamsNode(null, null, rest);
        }

        Token id = ParserHelper.expect(tokens, TokenType.ID_KEYWORD, "Expected a parameter name");
        ParserHelper.expect(tokens, TokenType.COLON, "Expected ':' after parameter name");
        TypeNode type = TypeNode.parse(tokens);

        while (ParserHelper.checkType(tokens, TokenType.COMMA)) {
            rest.add(FuncDefParamsTNode.parse(tokens));
        }

        return new FuncDefParamsNode(id, type, rest);
    }

    @Override
    public String convertToJott() {
        if (id == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        result.append(id.getToken()).append(":").append(type.convertToJott());
        for (FuncDefParamsTNode node : rest) {
            result.append(node.convertToJott());
        }
        return result.toString();
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
        if (id == null) {
            return true;
        }

        if (type == null || !type.validateTree()) {
            return false;
        }

        if (!SemanticAnalyzer.declareVariable(id.getToken(), type.getType())) {
            System.err.println("Semantic Error:");
            System.err.println("Duplicate parameter " + id.getToken());
            System.err.println(id.getFilename() + ":" + id.getLineNum());
            return false;
        }
        SemanticAnalyzer.lookupVariable(id.getToken()).setInitialized(true);

        for (FuncDefParamsTNode node : rest) {
            if (!node.validateTree()) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<String> getParameterTypes(){
        ArrayList<String> typeList = new ArrayList<>();

        //append the first parameter
        if (this.type != null){
            typeList.add(this.type.getType());
        }

        //append the rest
        for(FuncDefParamsTNode r: this.rest){
            typeList.add(r.getType());
        }

        return typeList;

    }
}
