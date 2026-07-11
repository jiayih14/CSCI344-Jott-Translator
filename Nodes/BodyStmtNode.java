package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

/**
 * @author Kifekachukwu Nwosu, Jiayi Huang
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


    public static BodyStmtNode parse(ArrayList<Token> tokens){
        if(ParserHelper.checkValue(tokens, "If")){
            IfStmtNode ifNode = IfStmtNode.parse(tokens);
            return new BodyStmtNode(ifNode);
        }
        else if(ParserHelper.checkValue(tokens, "While")){
            WhileNode whileNode = WhileNode.parse(tokens);
            return new BodyStmtNode(whileNode);
        }
        else if(ParserHelper.checkValue(tokens, "::")){
            FuncCallNode funcCallNode = FuncCallNode.parse(tokens);
            BodyStmtNode bodyStmtNode = new BodyStmtNode(funcCallNode);
            ParserHelper.expectValue(tokens, ";", "Expecting \";\" after function call");
            return bodyStmtNode;
        }
        else if(ParserHelper.checkType(tokens, TokenType.ID_KEYWORD)){
            AsmtNode asmtNode = AsmtNode.parse(tokens);
            return new BodyStmtNode(asmtNode);
        }
        else{
            throw ParserHelper.error(tokens, "Expect function call or if statement or while statement or Identifier");
        }

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
        if (ifStmtNode != null) {
            return ifStmtNode.validateTree();
        }
        if (whileNode != null) {
            return whileNode.validateTree();
        }
        if (asmtNode != null) {
            return asmtNode.validateTree();
        }
        if (funcCallNode != null) {
            return funcCallNode.validateTree();
        }
        return false;
    }

    public boolean guaranteesReturn() {
        if (ifStmtNode != null) {
            return ifStmtNode.guaranteesReturn();
        }
        if (whileNode != null) {
            return whileNode.guaranteesReturn();
        }
        return false;
    }
}
