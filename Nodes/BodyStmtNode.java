package Nodes;

import provided.JottTree;
/**
 * @author Kifekachukwu Nwosu
 *
 * Represents a single statement inside a function body.
 *
 * <body_stmt> -> <if_stmt>
 *              | <while_loop>
 *              | <asmt>
 *              | <func_call>;
 */
public class BodyStmtNode implements JottTree {

    public BodyStmtNode(){
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
