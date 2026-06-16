package Nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;

public class FuncDefParamsNode implements JottTree {

    private Token varName;
    private Token type;
    public FuncDefParamsNode(Token varName, Token type){
        this.varName = varName;
        this.type = type;
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
