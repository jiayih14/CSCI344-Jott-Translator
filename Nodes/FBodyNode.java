package Nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;

public class FBodyNode implements JottTree {

    ArrayList<VarDecNode> varDec;
    private ReturnStmtNode returnStmt;

    public FBodyNode(ArrayList<VarDecNode> varDec, ReturnStmtNode returnStmt){
        this.varDec = varDec;
        this.returnStmt = returnStmt;
    }

    public static FBodyNode parse(ArrayList<Token> tokens){
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
