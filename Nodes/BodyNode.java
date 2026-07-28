package Nodes;

import provided.JottTree;
import provided.Token;

import javax.swing.text.html.parser.Parser;
import java.util.ArrayList;
/***
 * @author Kifekachukwu Nwosu, Jiayi Huang, Teju Rajbabu
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

    public static BodyNode parse(ArrayList<Token> tokens){
        ArrayList<BodyStmtNode> bodystmts = new ArrayList<>();
        while(!ParserHelper.checkValue(tokens, "Return") && !ParserHelper.checkValue(tokens, "}")){
            BodyStmtNode bodystmtNode = BodyStmtNode.parse(tokens);
            bodystmts.add(bodystmtNode);
        }

        if (ParserHelper.checkValue(tokens, "Return")){
            ReturnStmtNode returnStmtNode = ReturnStmtNode.parse(tokens);
            return new BodyNode(bodystmts, returnStmtNode);
        }

        return new BodyNode(bodystmts, null);
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
    public String convertToJava(String className) {
        return convertToJava(1);
    }

    public String convertToJava(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = "    ".repeat(indentLevel);

        for (BodyStmtNode stmt : bodyStmtNodeList) {
            sb.append(indent)
              .append(stmt.convertToJava())
              .append("\n");
        }

        if (returnStmtNode != null) {
            sb.append(indent)
              .append(returnStmtNode.convertToJava())
              .append(";\n");
        }

        return sb.toString();
    }

    @Override
    public String convertToC() {
        return convertToC(1);
    }

    public String convertToC(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = "    ".repeat(indentLevel);

        for (BodyStmtNode stmt : bodyStmtNodeList) {
            sb.append(indent)
              .append(stmt.convertToC())
              .append("\n");
        }

        if (returnStmtNode != null) {
            sb.append(indent)
              .append(returnStmtNode.convertToC())
              .append(";\n");
        }

        return sb.toString();
    }

    @Override
    public String convertToPython() {
        return convertToPython(1);
    }

    public String convertToPython(int indentLevel) {
        StringBuilder sb = new StringBuilder();

        for (BodyStmtNode stmt : bodyStmtNodeList) {
            sb.append(stmt.convertToPython(indentLevel));
        }

        if (returnStmtNode != null) {
            sb.append("    ".repeat(indentLevel))
              .append(returnStmtNode.convertToPython())
              .append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean validateTree() {
        for (BodyStmtNode stmt : this.bodyStmtNodeList) {
            if (!stmt.validateTree()) {
                return false;
            }
        }
        if (this.returnStmtNode != null) {
            return this.returnStmtNode.validateTree();
        }
        return true;
    }
    public boolean hasReturnStatement() {
    return returnStmtNode != null;
}

    public boolean guaranteesReturn() {
        if (returnStmtNode != null) {
            return true;
        }
        if (bodyStmtNodeList.isEmpty()) {
            return false;
        }
        BodyStmtNode last = bodyStmtNodeList.get(bodyStmtNodeList.size() - 1);
        return last.guaranteesReturn();
    }
}
