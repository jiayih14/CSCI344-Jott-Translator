/**
 * File name: FuncCallNode.java
 * Author: Alvin Jiang
 *
 * This file defines the FuncCallNode class, which represents a grammar for function call
 *  in the Jott parse tree shown the following:
 *
 *  <func_call> -> :: <id>[<params>]
 *
 * The class provides functionality to parse a function call statement based on the provided grammar.
 * It also allows conversion to Jott, Java, C and Python
 */

package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FuncCallNode implements JottTree {

    private Token funcName;
    private ParamsNode params;

    /**
     * Constructor for FuncCall Node
     * @param funcName function name token
     * @param params parameter node
     */
    public FuncCallNode(Token funcName, ParamsNode params) {
        this.funcName = funcName;
        this.params = params;
    }

    public static FuncCallNode parse(ArrayList<Token> tokens) {
        ParserHelper.expect(tokens, TokenType.FC_HEADER, "Expected '::' to start a function call");
        Token funcName = ParserHelper.expect(tokens, TokenType.ID_KEYWORD, "Expected a function name after '::'");
        ParserHelper.expect(tokens, TokenType.L_BRACKET, "Expected '[' after function name");
        ParamsNode params = ParamsNode.parse(tokens);
        ParserHelper.expect(tokens, TokenType.R_BRACKET, "Expected ']' to close function call");
        return new FuncCallNode(funcName, params);
    }

    @Override
    public String convertToJott() {
        // A function call is an expression (<operand> -> <func_call>), so no
        // statement terminator here. BodyStmtNode adds the ';' when a call
        // appears as a standalone statement.
        return "::" + funcName.getToken() + "[" + params.convertToJott() + "]";
    }
    /**
     * A function call is an expression, so none of these methods emit a
     * statement terminator; BodyStmtNode adds it when a call stands alone as a
     * statement.
     *
     * Jott's builtins are part of the language rather than the source file, so
     * they translate to each language's own equivalent instead of being called
     * as user defined functions.
     */
    @Override
    public String convertToJava(String className){
        String name = this.funcName.getToken();
        String args = this.params.convertToJava(className);

        switch (name) {
            case "print":
                return javaPrint(className);
            case "concat":
                return javaConcat(className);
            case "length":
                return args + ".length()";
            default:
                return className + "." + TargetNames.java(name) + "(" + args + ")";
        }
    }

    /**
     * Builds the println call for Jott's print.
     *
     * A Boolean prints as True or False, which is what Jott specifies, rather
     * than the true or false Java would print for a boolean. Only the printed
     * text changes; the value itself stays a Java boolean.
     *
     * @param className the enclosing Java class name, passed through unchanged
     * @return the println expression for this print call
     */
    private String javaPrint(String className) {

        ExprNode argument = this.params.getFirstParam();

        if (argument == null) {
            return "System.out.println()";
        }

        String value = argument.convertToJava(className);

        if ("Boolean".equals(argument.getType())) {
            return "System.out.println((" + value + ") ? \"True\" : \"False\")";
        }

        if ("Double".equals(argument.getType())) {
            return "System.out.println(jott_double(" + value + "))";
        }

        return "System.out.println(" + value + ")";
    }

    /**
     * Java strings are immutable, so joining them with + already yields the new
     * String Jott's concat promises. The result is parenthesised so it stays a
     * single operand when it is nested in another expression, such as
     * ("a" + "b").length().
     *
     * @param className the enclosing Java class name, passed through unchanged
     * @return the Java concatenation expression
     */
    private String javaConcat(String className) {

        ExprNode first = firstArgument();
        ExprNode second = secondArgument();

        if (first == null || second == null) {
            return className + "." + this.funcName.getToken()
                    + "(" + this.params.convertToJava(className) + ")";
        }

        return "(" + first.convertToJava(className) + " + "
                + second.convertToJava(className) + ")";
    }

    @Override
    public String convertToC(){
        String name = this.funcName.getToken();
        String args = this.params.convertToC();

        switch (name) {
            case "print":
                return cPrint();
            case "concat":
                return cConcat();
            case "length":
                // strlen yields size_t, but length is an Integer in Jott, so
                // the result is narrowed here rather than everywhere it is used
                return "(int) strlen(" + args + ")";
            default:
                return TargetNames.c(name) + "(" + args + ")";
        }
    }

    /**
     * C cannot join strings with an operator, and strcat would write into its
     * first argument instead of producing the new string Jott promises, so the
     * call goes to the helper ProgramNode emits. Each argument appears once, so
     * arguments that are themselves calls are evaluated once.
     *
     * @return the C concatenation expression
     */
    private String cConcat() {

        ExprNode first = firstArgument();
        ExprNode second = secondArgument();

        if (first == null || second == null) {
            return this.funcName.getToken() + "(" + this.params.convertToC() + ")";
        }

        return "jott_concat(" + first.convertToC() + ", " + second.convertToC() + ")";
    }

    /**
     * Builds the printf call for Jott's print, which needs a format string
     * chosen from the argument's Jott type, plus the newline print always adds.
     *
     * Boolean prints as True or False rather than as the 1 or 0 it is stored
     * as, so that C matches the output Jott specifies.
     *
     * @return the printf expression for this print call
     */
    private String cPrint() {

        ExprNode argument = this.params.getFirstParam();

        if (argument == null) {
            return "printf(\"\\n\")";
        }

        String value = argument.convertToC();

        switch (String.valueOf(argument.getType())) {
            case "Integer":
                return "printf(\"%d\\n\", " + value + ")";
            case "Double":
                return "jott_print_double(" + value + ")";
            case "String":
                return "printf(\"%s\\n\", " + value + ")";
            case "Boolean":
                return "printf(\"%s\\n\", (" + value + ") ? \"True\" : \"False\")";
            default:
                // Semantic analysis rejects an untyped print argument, so this
                // is unreachable; emit the call unchanged rather than guess a
                // format that would silently print the wrong thing.
                return "print(" + value + ")";
        }
    }

    @Override
    public String convertToPython(){
        String name = this.funcName.getToken();
        String args = this.params.convertToPython();

        switch (name) {
            case "print":
                return pythonPrint(args);
            case "concat":
                return pythonConcat();
            case "length":
                return "len(" + args + ")";
            default:
                return TargetNames.python(name) + "(" + args + ")";
        }
    }

    /**
     * Builds the Python print call. Booleans already print as True and False,
     * so only a Double needs the helper ProgramNode emits to print the same
     * text the other targets do.
     *
     * @param args the translated argument list
     * @return the print expression for this call
     */
    private String pythonPrint(String args) {

        ExprNode argument = this.params.getFirstParam();

        if (argument != null && "Double".equals(String.valueOf(argument.getType()))) {
            return "print(jott_double(" + argument.convertToPython() + "))";
        }

        return "print(" + args + ")";
    }

    /**
     * Python strings are immutable, so + yields the new String Jott's concat
     * promises. Parenthesised so it nests safely inside other expressions.
     *
     * @return the Python concatenation expression
     */
    private String pythonConcat() {

        ExprNode first = firstArgument();
        ExprNode second = secondArgument();

        if (first == null || second == null) {
            return this.funcName.getToken() + "(" + this.params.convertToPython() + ")";
        }

        return "(" + first.convertToPython() + " + " + second.convertToPython() + ")";
    }

    /**
     * @return the first argument of this call, or null if there is none
     */
    private ExprNode firstArgument() {
        return this.params.getFirstParam();
    }

    /**
     * @return the second argument of this call, or null if there is none
     */
    private ExprNode secondArgument() {
        ArrayList<ParamsTNode> rest = this.params.getAdditionalParams();
        if (rest == null || rest.isEmpty()) {
            return null;
        }
        return rest.get(0).getExpr();
    }
@Override
public boolean validateTree() {

    if (!params.validateTree()) {
        return false;
    }

    FunctionInfo function = SemanticAnalyzer.lookupFunction(funcName.getToken());

    if (function == null) {
        System.err.println("Semantic Error:");
        System.err.println("Call to unknown function " + funcName.getToken());
        System.err.println(funcName.getFilename() + ":" + funcName.getLineNum());
        return false;
    }

    int actualCount = 0;
    if (params.getFirstParam() != null) {
        actualCount = 1 + params.getAdditionalParams().size();
    }

    int expectedCount = function.getParameterTypes().size();

    if (actualCount != expectedCount) {
        System.err.println("Semantic Error:");
        System.err.println("Function " + funcName.getToken()
                + " expects " + expectedCount
                + " parameter(s) but received "
                + actualCount + ".");
        System.err.println(funcName.getFilename() + ":" + funcName.getLineNum());
        return false;
    }

    for (int i = 0; i < actualCount; i++) {

        String expected = function.getParameterTypes().get(i);

        String actual;

        if (i == 0) {
            actual = params.getFirstParam().getType();
        } else {
            actual = params.getAdditionalParams().get(i - 1).getType();
        }

        if (!expected.equals("Any") && !expected.equals(actual)) {
            System.err.println("Semantic Error:");
            System.err.println("Invalid parameter type in call to function "
                    + funcName.getToken());
            System.err.println(funcName.getFilename() + ":" + funcName.getLineNum());
            return false;
        }
    }

    return true;
}

public Token getNameToken() {
    return funcName;
}

public String getType() {

    FunctionInfo function =
            SemanticAnalyzer.lookupFunction(funcName.getToken());

    if (function == null) {
        return null;
    }

    return function.getReturnType();
}
}
