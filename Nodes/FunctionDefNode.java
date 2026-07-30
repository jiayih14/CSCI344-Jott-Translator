package Nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FunctionDefNode implements JottTree {

    private Token funcName;
    private FuncDefParamsNode funcDefParams;
    private FunctionReturnNode funcReturn;
    private FBodyNode fBody;

    /**
     * Constructor for Function Definition Node
     * @param token the function name token
     * @param funcDefParams the parameter list node
     * @param funcReturn the return type node
     * @param fBody the function body node
     */
    public FunctionDefNode (Token token, FuncDefParamsNode funcDefParams,
                                    FunctionReturnNode funcReturn, FBodyNode fBody){
        this.funcName = token;
        this.funcDefParams = funcDefParams;
        this.funcReturn = funcReturn;
        this.fBody = fBody;
    }

    /**
     * parse function for function definition node
     * @param tokens a list of tokens to be consumed
     * @return a FunctionDefNode
     */
    public static FunctionDefNode parse(ArrayList<Token> tokens){
        ParserHelper.expectValue(tokens, "Def", "Expected 'Def' to begin a function definition");
        Token name = ParserHelper.expect(tokens, TokenType.ID_KEYWORD, "Expected a function name after 'Def'");
        ParserHelper.expect(tokens, TokenType.L_BRACKET, "Expected '[' after function name");
        FuncDefParamsNode params = FuncDefParamsNode.parse(tokens);
        ParserHelper.expect(tokens, TokenType.R_BRACKET, "Expected ']' after function parameters");
        ParserHelper.expect(tokens, TokenType.COLON, "Expected ':' after function parameters");
        FunctionReturnNode funcReturn = FunctionReturnNode.parse(tokens);
        ParserHelper.expect(tokens, TokenType.L_BRACE, "Expected '{' to begin function body");
        FBodyNode fBody = FBodyNode.parse(tokens);
        ParserHelper.expect(tokens, TokenType.R_BRACE, "Expected '}' to close function body");

        return new FunctionDefNode(name, params, funcReturn, fBody);
    }

    @Override
    public String convertToJott() {
        return "Def " + funcName.getToken()
                + "[" + funcDefParams.convertToJott() + "]"
                + ":" + funcReturn.convertToJott()
                + "{" + fBody.convertToJott() + "}";
    }
    /**
     * Generates the Java form of this function definition.
     *
     * The method sits one level in, inside the class that ProgramNode will
     * emit, and its body one level deeper. The class wrapper itself is not
     * generated here.
     *
     * Jott's main is special: Java requires "public static void
     * main(String args[])", so the Jott parameter list and return type are not
     * used for it.
     *
     * @param className the enclosing Java class name, passed through unchanged
     * @return the Java code for this function, newline terminated
     */
    @Override
    public String convertToJava(String className){

        StringBuilder sb = new StringBuilder();

        if (isMain()) {
            sb.append(indent(1)).append("public static void main(String args[]) {\n");
        } else {
            sb.append(indent(1)).append("public static ")
              .append(funcReturn.convertToJava(className)).append(" ")
              .append(TargetNames.java(funcName.getToken()))
              .append("(").append(funcDefParams.convertToJava(className)).append(") {\n");
        }

        sb.append(fBody.convertToJava(className, 2));
        sb.append(indent(1)).append("}\n");

        return sb.toString();
    }

    /**
     * Generates the C form of this function definition.
     *
     * The signature sits at column zero and the body one level in. The
     * includes belong to ProgramNode.
     *
     * Jott's main is special: C wants "int main(void)" and requires a return,
     * so "return 1;" is appended. A valid Jott main is Void and therefore
     * contributes no return statement of its own.
     *
     * @return the C code for this function, newline terminated
     */
    @Override
    public String convertToC(){

        StringBuilder sb = new StringBuilder();

        if (isMain()) {
            sb.append("int main(void) {\n");
            sb.append(fBody.convertToC(1));
            sb.append(indent(1)).append("return 1;\n");
        } else {
            String params = funcDefParams.convertToC();
            if (params.isEmpty()) {
                // C wants an explicit void for a parameterless function
                params = "void";
            }
            sb.append(funcReturn.convertToC()).append(" ")
              .append(TargetNames.c(funcName.getToken()))
              .append("(").append(params).append(") {\n");
            sb.append(fBody.convertToC(1));
        }

        sb.append("}\n");

        return sb.toString();
    }

    /**
     * Generates the Python form of this function definition.
     *
     * Python carries no return type, and the trailing call to main belongs to
     * ProgramNode. An empty body becomes a single "pass", since FBodyNode
     * returns nothing for a body with no contents.
     *
     * @return the Python code for this function, newline terminated
     */
    @Override
    public String convertToPython(){

        StringBuilder sb = new StringBuilder();

        sb.append("def ").append(TargetNames.python(funcName.getToken()))
          .append("(").append(funcDefParams.convertToPython()).append("):\n");

        String body = fBody.convertToPython(1);
        if (body.isBlank()) {
            body = indent(1) + "pass\n";
        }
        sb.append(body);

        return sb.toString();
    }

    /**
     * @return true if this is the Jott main function, which every target
     *         language translates specially
     */
    private boolean isMain() {
        return "main".equals(funcName.getToken());
    }

    private static String indent(int indentLevel) {
        return "    ".repeat(indentLevel);
    }

@Override
public boolean validateTree() {

    FunctionInfo function =
            SemanticAnalyzer.lookupFunction(funcName.getToken());

    SemanticAnalyzer.setCurrentFunction(function);

    SemanticAnalyzer.enterScope();

    // Declare function parameters
    if (!funcDefParams.validateTree()) {
        SemanticAnalyzer.exitScope();
        return false;
    }

    // Validate function body
    if (!fBody.validateTree()) {
        SemanticAnalyzer.exitScope();
        return false;
    }
    // Non-Void functions must have a guaranteed return path
    if (!funcReturn.getType().equals("Void")
        && !fBody.guaranteesReturn()) {

    System.err.println("Semantic Error:");
    System.err.println("Missing return for non-Void function "
            + funcName.getToken());
    System.err.println(funcName.getFilename() + ":"
            + funcName.getLineNum());

    SemanticAnalyzer.exitScope();
    return false;
}

    SemanticAnalyzer.exitScope();
    return true;
}

    public Token getNameToken() {
        return funcName;
    }

    public boolean registerFunction(){
        String name = this.funcName.getToken();
        if (name == null || name.isEmpty() || !Character.isLowerCase(name.charAt(0))) {
            System.err.println("Semantic Error:");
            System.err.println("Function " + name + " must start with a lowercase letter.");
            System.err.println(this.funcName.getFilename() + ":" + this.funcName.getLineNum());
            return false;
        }

        FunctionInfo funcInfo = new FunctionInfo(this.funcReturn.getType(), this.funcDefParams.getParameterTypes());
        if(!SemanticAnalyzer.declareFunction(this.funcName.getToken(), funcInfo)){
            System.err.println("Semantic Error:");
            System.err.println("Function " + this.funcName.getToken() + " already defined.");
            System.err.println(this.funcName.getFilename() + ":" + this.funcName.getLineNum());
            return false;
        }
        return true;
    }
}
