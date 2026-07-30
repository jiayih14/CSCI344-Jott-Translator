/**
 * Jott.java
 * @author: Kifekachukwu Nwosu
 * This method is the entry point for the Jott compiler. It takes in a Jott source file, tokenizes it, parses it into a syntax tree, and then validates the tree. If any step fails, it prints an error message and exits.
 * Once the tree is valid it is translated to the requested target language and
 * written to the requested output file.
 */


import provided.JottParser;
import provided.JottTokenizer;
import provided.JottTree;
import provided.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Jott {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println("Usage: java Jott <input.jott> <output file> <Jott|Java|C|Python>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];
        String targetLanguage = args[2];

        try {

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

            // Semantic analysis; an invalid program is never translated
            if (!tree.validateTree()) {
                return;
            }

            // Code generation. The whole program is generated before the output
            // file is touched, so a failed run leaves no partial file behind.
            String generated = translate(tree, targetLanguage, outputFile);

            if (generated == null) {
                return;
            }

            writeOutput(generated, outputFile);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Translates a validated tree into the requested language.
     *
     * @param tree the validated Jott tree
     * @param targetLanguage the language named on the command line
     * @param outputFile the output path, used to name the Java class
     * @return the generated source, or null if the language is not supported
     */
    private static String translate(JottTree tree, String targetLanguage, String outputFile) {

        switch (targetLanguage) {
            case "Jott":
                return tree.convertToJott();
            case "Java":
                return tree.convertToJava(classNameFor(outputFile));
            case "C":
                return tree.convertToC();
            case "Python":
                return tree.convertToPython();
            default:
                System.err.println("Unsupported target language: " + targetLanguage);
                System.err.println("Expected one of: Jott, Java, C, Python");
                return null;
        }
    }

    /**
     * Derives the Java class name from the output file, since Java requires the
     * two to match. Only the directories and the final extension are dropped;
     * the name itself is left exactly as the user wrote it.
     *
     * @param outputFile the output path given on the command line
     * @return the class name to generate
     */
    private static String classNameFor(String outputFile) {

        Path fileName = Paths.get(outputFile).getFileName();

        if (fileName == null) {
            return outputFile;
        }

        String name = fileName.toString();
        int extension = name.lastIndexOf('.');

        if (extension > 0) {
            return name.substring(0, extension);
        }

        return name;
    }

    /**
     * Writes the generated source to the output file, replacing any file
     * already there.
     *
     * @param generated the generated source
     * @param outputFile the output path given on the command line
     */
    private static void writeOutput(String generated, String outputFile) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(generated);
        } catch (IOException e) {
            System.err.println("Could not write output file " + outputFile + ": " + e.getMessage());
        }
    }
}
