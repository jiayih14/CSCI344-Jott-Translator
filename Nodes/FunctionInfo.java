package Nodes;

import java.util.List;

public class FunctionInfo {

    private String returnType;
    private List<String> parameterTypes;

    public FunctionInfo(String returnType, List<String> parameterTypes) {
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getParameterTypes() {
        return parameterTypes;
    }
}
