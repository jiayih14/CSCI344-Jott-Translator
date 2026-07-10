package Nodes;

public class VariableInfo {

    private String type;
    private boolean initialized;

    public VariableInfo(String type) {
        this.type = type;
        this.initialized = false;
    }

    public String getType() {
        return this.type;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
