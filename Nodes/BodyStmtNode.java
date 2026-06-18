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

    private IfStmtNode ifStmtNode;
    private WhileNode whileNode;
    private AsmtNode asmtNode;
    private FuncCallNode funcCallNode;

    public BodyStmtNode(IfStmtNode ifStmtNode) {
        this.ifStmtNode = ifStmtNode;
    }

    public BodyStmtNode(WhileNode whileNode) {
        this.whileNode = whileNode;
    }

    public BodyStmtNode(AsmtNode asmtNode) {
        this.asmtNode = asmtNode;
    }

    public BodyStmtNode(FuncCallNode funcCallNode) {
        this.funcCallNode = funcCallNode;
    }


    @Override
    public String convertToJott() {

        if (ifStmtNode != null) {
            return ifStmtNode.convertToJott();
        }

        if (whileNode != null) {
            return whileNode.convertToJott();
        }

        if (asmtNode != null) {
            return asmtNode.convertToJott();
        }

        if (funcCallNode != null) {
            return funcCallNode.convertToJott() + ";";
        }

        return "";
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
