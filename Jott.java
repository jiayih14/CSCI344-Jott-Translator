/**
 * Jott.java    
 * @author: Kifekachukwu Nwosu
 * This method is the entry point for the Jott compiler. It takes in a Jott source file, tokenizes it, parses it into a syntax tree, and then validates the tree. If any step fails, it prints an error message and exits.
 */


import provided.JottParser;
import provided.JottTokenizer;
import provided.JottTree;
import provided.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Jott {

    public static void main(String[] args) {

        //

        if (args.length != 3) {
            System.err.println("Usage: java Jott <input.jott> <output file> <Jott|Java|C|Python>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];
        String targetLanguage = args[2];

   // Tokenize
ArrayList<Token> tokens = JottTokenizer.tokenize(inputFile);

if (tokens == null) {
    return;
}

// Parse
JottTree tree = JottParser.parse(tokens);

if (tree == null) {
    return;
}
     if (!tree.validateTree()) {
            return;
        }
}
}