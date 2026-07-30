/**
 * File name: VarDecNode.java
 * Author: Jiayi Huang
 *
 * This file defines the VarDecNode class, which represents a grammar for variable declaration
 * in the Jott parse tree shown the following:
 *
 *  <type> <id>;
 *
 * The class provides functionality to parse variable declaration based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class VarDecNode implements JottTree {

    private TypeNode typeNode;
    private Token id;

    /**
     * This is a constructor for varDecNode
     * @param type
     * @param id
     */
    public VarDecNode(TypeNode type, Token id){
        this.typeNode = type;
        this.id = id;
    }

    /**
     * This is a parse function that will parse variable type declaration statement.
     * @param tokens a list of tokens
     * @return VarDecNode
     */
    public static VarDecNode parse(ArrayList<Token> tokens){
        TypeNode type = TypeNode.parse(tokens);
        Token id = ParserHelper.expect(tokens, TokenType.ID_KEYWORD, "Expected a valid identifier");
        ParserHelper.expect(tokens, TokenType.SEMICOLON, "Expected ';' to end assignment statement");

        return new VarDecNode(type, id);
    }

    @Override
    public String convertToJott() {
        return this.typeNode.convertToJott() + " " + this.id.getToken() + ";";
    }
    @Override
    public String convertToJava(String className){
        return this.typeNode.convertToJava(className) + " "
                + TargetNames.java(this.id.getToken()) + ";";
    }

    /**
     * A declaration is a statement in C, so it ends in a semicolon just as the
     * Java and Jott forms do. TypeNode already yields "char *" for String, and
     * the pointer binds to the name.
     */
    @Override
    public String convertToC(){
        if(this.typeNode.getType().equals("String")){
            return this.typeNode.convertToC() + TargetNames.c(this.id.getToken()) + ";";
        }
        return this.typeNode.convertToC() + " " + TargetNames.c(this.id.getToken()) + ";";
    }

    /**
     * Python has no declaration syntax; a name comes into being when it is
     * assigned. The spec's example translates "Integer x; x = 5;" to just
     * "x=5", so a declaration contributes no line at all.
     */
    @Override
    public String convertToPython(){
        return "";
    }

    @Override
    public boolean validateTree() {
        if (!this.typeNode.validateTree()) {
            return false;
        }

        String name = this.id.getToken();
        if (name == null || name.isEmpty() || !Character.isLowerCase(name.charAt(0))) {
            System.err.println("Semantic Error:");
            System.err.println("Variable " + name + " must start with a lowercase letter.");
            System.err.println(id.getFilename() + ":" + id.getLineNum());
            return false;
        }

        if (!SemanticAnalyzer.declareVariable( this.id.getToken(), this.typeNode.getType())) {
            System.err.println("Semantic Error:");
            System.err.println("Variable " + id.getToken() + " already declared.");
            System.err.println(id.getFilename() + ":" + id.getLineNum());
            return false;
        }

        return true;
    }
}
