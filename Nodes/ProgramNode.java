/**
 * File name: ProgramNode.java
 * Author: Jiayi Huang
 *
 * This file defines the ProgramNode class, which represents a grammar for start of the program
 * in the Jott parse tree shown the following:
 *
 *  <function_def>*<EOF>
 *
 * The class provides functionality to parse the start of program based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;

public class ProgramNode implements JottTree {
    private ArrayList<FunctionDefNode> functionDefNode;

    /**
     * Constructor for program node.
     * @param functionDefNodes
     */
    public ProgramNode (ArrayList<FunctionDefNode> functionDefNodes){
        this.functionDefNode = functionDefNodes;
    }

    /**
     * Parse function that will return a program node.
     * @param tokens a list of tokens to be parsed
     * @return a program node, this will be the root node.
     */
    public static ProgramNode parse(ArrayList<Token> tokens){
        ArrayList<FunctionDefNode> result = new ArrayList<>();


        while(ParserHelper.peek(tokens) != null){
            // while tokens are not empty, keep parsing function def node
            FunctionDefNode funcNode = FunctionDefNode.parse(tokens);
            result.add(funcNode);
        }
        return new ProgramNode(result);
    }

    @Override
    public String convertToJott() {
        StringBuilder result = new StringBuilder();

        for(FunctionDefNode node: this.functionDefNode){
            result.append(node.convertToJott());
        }
        return result.toString();
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

    SemanticAnalyzer.initialize();
    SemanticAnalyzer.enterScope();

    // Register and validate each function in source order, so a function
    // is only visible to calls that occur at or after its own definition.
    for (FunctionDefNode node : functionDefNode) {
        if (!node.registerFunction()) {
            SemanticAnalyzer.exitScope();
            return false;
        }
        if (!node.validateTree()) {
            SemanticAnalyzer.exitScope();
            return false;
        }
    }

    FunctionInfo main = SemanticAnalyzer.lookupFunction("main");

    if (main == null) {
        System.err.println("Semantic Error:");
        System.err.println("Missing/incorrectly defined main function.");
        SemanticAnalyzer.exitScope();
        return false;
    }

    FunctionDefNode mainNode = null;
    for (FunctionDefNode node : functionDefNode) {
        if (node.getNameToken().getToken().equals("main")) {
            mainNode = node;
            break;
        }
    }

if (!main.getReturnType().equals("Void")) {
    System.err.println("Semantic Error:");
    System.err.println("Main function must return Void.");
    if (mainNode != null) {
        System.err.println(mainNode.getNameToken().getFilename() + ":" + mainNode.getNameToken().getLineNum());
    }
    SemanticAnalyzer.exitScope();
    return false;
}

    if (!main.getParameterTypes().isEmpty()) {
        System.err.println("Semantic Error:");
        System.err.println("Main function cannot have parameters.");
        if (mainNode != null) {
            System.err.println(mainNode.getNameToken().getFilename() + ":" + mainNode.getNameToken().getLineNum());
        }
        SemanticAnalyzer.exitScope();
        return false;
    }

    SemanticAnalyzer.exitScope();
    return true;
}
}
