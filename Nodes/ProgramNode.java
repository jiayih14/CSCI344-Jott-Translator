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

    /**
     * Generates the Java form of the whole program: the class every Java
     * program needs, holding each translated function in source order.
     *
     * FunctionDefNode already emits its methods at one level of indentation,
     * so nothing is indented here. Java resolves static calls regardless of
     * declaration order, so the source order needs no rearranging.
     *
     * @param className the class name, which Java requires to match the output
     *                  file name; used as given
     * @return the Java code for this program
     */
    @Override
    public String convertToJava(String className){
        StringBuilder result = new StringBuilder();

        result.append("public class ").append(className).append(" {\n");

        for (FunctionDefNode node : this.functionDefNode) {
            result.append(node.convertToJava(className));
        }

        result.append("}\n");

        return result.toString();
    }

    /**
     * Generates the C form of the whole program: the default headers followed
     * by each translated function in source order.
     *
     * The three headers are included unconditionally, as the spec directs, so
     * that printing and string handling always resolve. No prototypes are
     * emitted: Jott already requires a function to be defined before it is
     * called, which Phase 3 validates, so definitions in source order are
     * always in scope at their call sites.
     *
     * @return the C code for this program
     */
    @Override
    public String convertToC(){
        StringBuilder result = new StringBuilder();

        result.append("#include <stdio.h>\n");
        result.append("#include <string.h>\n");
        result.append("#include <stdlib.h>\n");

        // C has no string concatenation of its own, and Jott's concat has to
        // return a new string rather than write into either argument, so the
        // translator supplies one. Emitted ahead of every function, like the
        // headers, so it is in scope wherever concat is called.
        result.append("char *jott_concat(const char *a, const char *b) {\n");
        result.append("    char *result = malloc(strlen(a) + strlen(b) + 1);\n");
        result.append("    strcpy(result, a);\n");
        result.append("    strcat(result, b);\n");
        result.append("    return result;\n");
        result.append("}\n");

        // Jott prints a Double with a fractional part even when it is whole,
        // as in 3.0 + 2.0 becoming 5.0, which no printf conversion does on its
        // own, so the point is restored when the formatted value lacks one.
        result.append("void jott_print_double(double value) {\n");
        result.append("    char buffer[64];\n");
        result.append("    snprintf(buffer, sizeof(buffer), \"%g\", value);\n");
        result.append("    if (strpbrk(buffer, \".eE\") == NULL) {\n");
        result.append("        printf(\"%s.0\\n\", buffer);\n");
        result.append("    } else {\n");
        result.append("        printf(\"%s\\n\", buffer);\n");
        result.append("    }\n");
        result.append("}\n");

        for (FunctionDefNode node : this.functionDefNode) {
            result.append(node.convertToC());
        }

        return result.toString();
    }

    /**
     * Generates the Python form of the whole program: each translated function
     * in source order, then the call to main that Python requires.
     *
     * The call is emitted once, after every definition, so that main and
     * everything it reaches are already bound no matter where main appeared in
     * the Jott source.
     *
     * @return the Python code for this program
     */
    @Override
    public String convertToPython(){
        StringBuilder result = new StringBuilder();

        for (FunctionDefNode node : this.functionDefNode) {
            result.append(node.convertToPython());
        }

        result.append("main()\n");

        return result.toString();
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
