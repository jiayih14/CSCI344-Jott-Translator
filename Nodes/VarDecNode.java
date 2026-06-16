package Nodes;

import provided.JottTree;
import provided.Token;

public class VarDecNode implements JottTree {

    private Token type;
    private Token id;
    public VarDecNode(Token type, Token id){
        this.type = type;
        this.id = id;
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
