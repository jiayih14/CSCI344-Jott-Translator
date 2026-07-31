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
        return convertToJava(className, 0);
    }

    /**
     * Generates the Java form of this statement at the given indent level.
     *
     * If and While own their own indentation, nested body indentation, and
     * trailing newline, so they are passed through untouched. Leaf statements
     * get their indentation and newline from here.
     *
     * A function call is an expression in the Jott grammar
     * (<operand> -> <func_call>), so the statement level semicolon is added
     * here. An assignment is already a statement and supplies its own.
     *
     * @param className the enclosing Java class name
     * @param indentLevel the level this statement sits at
     * @return the Java code for this statement, newline terminated
     */
    public String convertToJava(String className, int indentLevel) {

        if (ifStmtNode != null) {
            return ifStmtNode.convertToJava(className, indentLevel);
        }

        if (whileNode != null) {
            return whileNode.convertToJava(className, indentLevel);
        }

        if (asmtNode != null) {
            return indent(indentLevel) + asmtNode.convertToJava(className) + "\n";
        }

        if (funcCallNode != null) {
            return indent(indentLevel) + funcCallNode.convertToJava(className) + ";\n";
        }

        return "";
    }

    @Override
    public String convertToC(){
        return convertToC(0);
    }

    /**
     * Generates the C form of this statement at the given indent level.
     *
     * @param indentLevel the level this statement sits at
     * @return the C code for this statement, newline terminated
     */
    public String convertToC(int indentLevel) {

        if (ifStmtNode != null) {
            return ifStmtNode.convertToC(indentLevel);
        }

        if (whileNode != null) {
            return whileNode.convertToC(indentLevel);
        }

        if (asmtNode != null) {
            return indent(indentLevel) + asmtNode.convertToC() + "\n";
        }

        if (funcCallNode != null) {
            return indent(indentLevel) + funcCallNode.convertToC() + ";\n";
        }

        return "";
    }

    @Override
    public String convertToPython(){
        return convertToPython(0);
    }

    /**
     * Generates the Python form of this statement at the given indent level.
     * Python takes no statement terminator, only the newline.
     *
     * @param indentLevel the level this statement sits at
     * @return the Python code for this statement, newline terminated
     */
    public String convertToPython(int indentLevel) {

        if (ifStmtNode != null) {
            return ifStmtNode.convertToPython(indentLevel);
        }

        if (whileNode != null) {
            return whileNode.convertToPython(indentLevel);
        }

        if (asmtNode != null) {
            return indent(indentLevel) + asmtNode.convertToPython() + "\n";
        }

        if (funcCallNode != null) {
            return indent(indentLevel) + funcCallNode.convertToPython() + "\n";
        }

        return "";
    }

    private static String indent(int indentLevel) {
        return "    ".repeat(indentLevel);
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
