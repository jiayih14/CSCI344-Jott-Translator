/**
 * File name: FunctionReturnNode.java
 * Author: Jiayi Huang
 *
 * This file defines the FunctionReturnNode class, which represents a grammar for function return type
 * declaration in the Jott parse tree shown the following:
 *
 *  <function_return> -> <type> | Void
 *
 * The class provides functionality to parse a parameter declaration statement based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FunctionReturnNode implements JottTree {

    private TypeNode type;


    /**
     * Constructor for Function Return Node
     * @param type type node
     */
    public FunctionReturnNode (TypeNode type){
        this.type = type;
    }

    /**
     * parse function for function return node
     * @param tokens a list of tokens to be consumed
     * @return a FunctionReturnNode
     */
    public static FunctionReturnNode parse(ArrayList<Token> tokens){

        if(ParserHelper.checkValue(tokens, "Void")){
            tokens.remove(0);
            return new FunctionReturnNode(null);
        }
        else{
            return new FunctionReturnNode(TypeNode.parse(tokens));
        }
    }

    @Override
    public String convertToJott() {
        if(this.type == null){
            return "Void";
        }
        else{
            return this.type.convertToJott();
        }
    }
    /**
     * Renders this return type in Java. A Void function, represented by a null
     * type, becomes "void"; every other type is translated by TypeNode.
     *
     * @param className the enclosing Java class name, passed through unchanged
     * @return the Java return type
     */
    @Override
    public String convertToJava(String className){
        if (this.type == null) {
            return "void";
        }
        return this.type.convertToJava(className);
    }

    /**
     * Renders this return type in C. A Void function becomes "void".
     *
     * @return the C return type
     */
    @Override
    public String convertToC(){
        if (this.type == null) {
            return "void";
        }
        return this.type.convertToC();
    }

    /**
     * Python "def" carries no return type, so this contributes nothing for
     * both Void and non-Void functions.
     *
     * @return the empty string
     */
    @Override
    public String convertToPython(){
        return "";
    }

    @Override
    public boolean validateTree() {
        if(this.type == null){
            return true;
        }
        else{
            return this.type.validateTree();
        }
    }

    public String getType() {
        if (this.type == null) {
            return "Void";
        }
        return this.type.getType();
    }
}
