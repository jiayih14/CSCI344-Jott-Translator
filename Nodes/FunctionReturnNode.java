package Nodes;

import provided.JottTree;
import provided.Token;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FunctionReturnNode implements JottTree {

    private TypeNode type;


    /**
     * Constructor for Function Return Node
     * @param type
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
        Token firstToken = ParserHelper.peek(tokens);

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
        return false;
    }
}
