package Nodes;

import provided.JottTree;

import java.util.ArrayList;
/***
 * @author Kifekachukwu Nwosu
 * This class represents the body of a function, which consists of a list of statements and an optional return statement.
 * The body of a function is defined in the Jott grammar as follows:
 */
public class BodyNode implements JottTree {

    private ArrayList<BodyStmtNode> bodyStmtNodeList;
    private ReturnStmtNode returnStmtNode;


    public BodyNode(ArrayList<BodyStmtNode> bodyStmtNodeList, ReturnStmtNode returnStmtNode){
        this.bodyStmtNodeList = bodyStmtNodeList;
        this.returnStmtNode = returnStmtNode;
    }

@Override
public String convertToJott() {

    StringBuilder sb = new StringBuilder();

    for (BodyStmtNode stmt : bodyStmtNodeList) {
        sb.append(stmt.convertToJott());
    }

    if (returnStmtNode != null) {
        sb.append(returnStmtNode.convertToJott());
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
