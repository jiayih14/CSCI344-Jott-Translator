/**
 * File name: FuncDefParamsTNode.java
 * Author: Alvin Jiang
 *
 * This file defines the FuncDefParamsTNode class, which represents a grammar for parameter declaration
 *  in the Jott parse tree shown the following:
 *
 *  <func_def_params_t> -> ,<id>: <type>
 *
 * The class provides functionality to parse a parameter declaration statement based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FuncDefParamsTNode implements JottTree {

    private Token id;
    private TypeNode type;

    /**
     * Constructor for Function Definition Parameter T Node
     * @param id id token
     * @param type type node
     */
    public FuncDefParamsTNode(Token id, TypeNode type) {
        this.id = id;
        this.type = type;
    }

    public static FuncDefParamsTNode parse(ArrayList<Token> tokens) {
        ParserHelper.expect(tokens, TokenType.COMMA, "Expected ',' before next function parameter");
        Token id = ParserHelper.expect(tokens, TokenType.ID_KEYWORD, "Expected a parameter name after ','");
        ParserHelper.expect(tokens, TokenType.COLON, "Expected ':' after parameter name");
        TypeNode type = TypeNode.parse(tokens);
        return new FuncDefParamsTNode(id, type);
    }

    @Override
    public String convertToJott() {
        return ", " + id.getToken() + ":" + type.convertToJott();
    }
    /**
     * Renders one additional Java parameter, such as ", double y". This node
     * owns the separator, matching <func_def_params_t> -> ,<id>:<type>.
     *
     * @param className the enclosing Java class name, passed through unchanged
     * @return this parameter preceded by its ", " separator
     */
    @Override
    public String convertToJava(String className){
        return ", " + type.convertToJava(className) + " " + TargetNames.java(id.getToken());
    }

    /**
     * Renders one additional C parameter, such as ", double y".
     *
     * @return this parameter preceded by its ", " separator
     */
    @Override
    public String convertToC(){
        return ", " + type.convertToC() + " " + TargetNames.c(id.getToken());
    }

    /**
     * Renders one additional Python parameter, such as ", y". Python
     * parameters carry names only.
     *
     * @return this parameter name preceded by its ", " separator
     */
    @Override
    public String convertToPython(){
        return ", " + TargetNames.python(id.getToken());
    }

    @Override
    public boolean validateTree() {
        if (type == null || !type.validateTree()) {
            return false;
        }

        String name = id.getToken();
        if (name == null || name.isEmpty() || !Character.isLowerCase(name.charAt(0))) {
            System.err.println("Semantic Error:");
            System.err.println("Parameter " + name + " must start with a lowercase letter.");
            System.err.println(id.getFilename() + ":" + id.getLineNum());
            return false;
        }

        if (!SemanticAnalyzer.declareVariable(id.getToken(), type.getType())) {
            System.err.println("Semantic Error:");
            System.err.println("Duplicate parameter " + id.getToken());
            System.err.println(id.getFilename() + ":" + id.getLineNum());
            return false;
        }
        SemanticAnalyzer.lookupVariable(id.getToken()).setInitialized(true);

        return true;
    }

    public String getType(){
        return this.type.getType();
    }
}
