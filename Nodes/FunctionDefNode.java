package Nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;

public class FunctionDefNode implements JottTree {

    private Token funcName;
    private ArrayList<FuncDefParamsNode> funcDefParams;
    private FunctionReturnNode funcReturn;
    private FBodyNode fBody;

    public FunctionDefNode (Token token, ArrayList<FuncDefParamsNode> funcDefParams,
                                    FunctionReturnNode funcReturn, FBodyNode fBody){
        this.funcName = token;
        this.funcDefParams = funcDefParams;
        this.funcReturn = funcReturn;
        this.fBody = fBody;
    }

    public static FunctionDefNode parse(ArrayList<Token> tokens){
        return null;
    }

    @Override
    public String convertToJott() {
        return null;
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
