package Nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;
/***
 * @author Kifekachukwu Nwosu, Jiayi Huang
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
    public String convertToJava(String className){
        return convertToJava(className, 0);
    }

    /**
     * Generates the Java form of this body with its contents rendered at the
     * given indent level.
     *
     * The level received is the level of the body's own statements; it is not
     * incremented here. Control flow parents already pass indentLevel + 1.
     *
     * Each BodyStmtNode owns the indentation and newline of the statement it
     * holds, so it is delegated to at the same level. ReturnStmtNode is a leaf
     * that indents nothing and supplies its own ';', so this method adds its
     * indentation and newline.
     *
     * @param className the enclosing Java class name, passed through unchanged
     * @param indentLevel the level this body's statements sit at
     * @return the Java code for this body, or "" when the body is empty
     */
    public String convertToJava(String className, int indentLevel) {

        StringBuilder sb = new StringBuilder();

        for (BodyStmtNode stmt : bodyStmtNodeList) {
            sb.append(stmt.convertToJava(className, indentLevel));
        }

        if (returnStmtNode != null) {
            sb.append(indent(indentLevel))
              .append(returnStmtNode.convertToJava(className))
              .append("\n");
        }

        return sb.toString();
    }

    @Override
    public String convertToC(){
        return convertToC(0);
    }

    /**
     * Generates the C form of this body with its contents rendered at the given
     * indent level.
     *
     * @param indentLevel the level this body's statements sit at
     * @return the C code for this body, or "" when the body is empty
     */
    public String convertToC(int indentLevel) {

        StringBuilder sb = new StringBuilder();

        for (BodyStmtNode stmt : bodyStmtNodeList) {
            sb.append(stmt.convertToC(indentLevel));
        }

        if (returnStmtNode != null) {
            sb.append(indent(indentLevel))
              .append(returnStmtNode.convertToC())
              .append("\n");
        }

        return sb.toString();
    }

    @Override
    public String convertToPython(){
        return convertToPython(0);
    }

    /**
     * Generates the Python form of this body with its contents rendered at the
     * given indent level.
     *
     * An empty body returns an empty string; the control flow parents are
     * responsible for substituting "pass" where Python requires a statement.
     *
     * @param indentLevel the level this body's statements sit at
     * @return the Python code for this body, or "" when the body is empty
     */
    public String convertToPython(int indentLevel) {

        StringBuilder sb = new StringBuilder();

        for (BodyStmtNode stmt : bodyStmtNodeList) {
            sb.append(stmt.convertToPython(indentLevel));
        }

        if (returnStmtNode != null) {
            sb.append(indent(indentLevel))
              .append(returnStmtNode.convertToPython())
              .append("\n");
        }

        return sb.toString();
    }

    private static String indent(int indentLevel) {
        return "    ".repeat(indentLevel);
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
