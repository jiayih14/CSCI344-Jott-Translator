package provided;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author
 */

import Nodes.ProgramNode;

import java.util.ArrayList;

public class JottParser {

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens){
        // this line is commented out because Function Def Node is a stubbed node
        // when everything is ready, remove return null and uncomment the following line :)

//		return ProgramNode.parse(tokens);
        return null;
    }
}
