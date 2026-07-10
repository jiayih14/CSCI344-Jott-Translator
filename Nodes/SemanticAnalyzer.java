package Nodes;

import java.util.*;

public class SemanticAnalyzer {

    // Variables are scoped
    private static Stack<HashMap<String, VariableInfo>> variableScopes;

    // Functions are global
    private static HashMap<String, FunctionInfo> functions;

    // Current function being validated
    private static FunctionInfo currentFunction;

    public static void initialize() {
        variableScopes.clear();
        functions.clear();
        currentFunction = null;
    }

    public static void enterScope() {
        variableScopes.push(new HashMap<>());
    }

    public static void exitScope() {
        variableScopes.pop();
    }

    public static HashMap<String, VariableInfo> currentScope() {
        return variableScopes.peek();
    }

    public static FunctionInfo getCurrentFunction() {
        return currentFunction;
    }

    public static void setCurrentFunction(FunctionInfo function) {
        currentFunction = function;
    }

    public static HashMap<String, FunctionInfo> getFunctions() {
        return functions;
    }

    public static boolean declareVariable(String name, String type) {
        if(currentScope().containsKey(name))
            return false;

        currentScope().put(name, new VariableInfo(type));

        return true;
    }

    public static boolean declareFunction(String name,
                                          FunctionInfo function) {

        if(functions.containsKey(name))
            return false;

        functions.put(name, function);
        return true;
    }

    public static VariableInfo lookupVariable(String name) {

        for(int i = variableScopes.size()-1; i>=0; i--) {

            if(variableScopes.get(i).containsKey(name))
                return variableScopes.get(i).get(name);
        }

        return null;
    }

    public static FunctionInfo lookupFunction(String name) {
        return functions.get(name);
    }
}
