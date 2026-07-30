package testers;

/*
  Jott code generation tester. This will test Phase 4 (code generation)
  of the Jott project.

  This tester assumes a working and valid tokenizer, parser, and semantic
  analyzer, and runs the full pipeline:
  tokenize -> parse -> validateTree() -> convertToJott/Java/C/Python.

  Every .jott file in the phase4testcases directory is treated as a valid
  program and is required to translate to all four targets. The generated
  Jott is then re-tokenized, re-parsed, re-validated, and translated again,
  which must reproduce the same Jott; a translator that loses or corrupts
  part of the program shows up as a difference here.

  No compiler or interpreter for the target languages is needed to run this
  tester.

  This expects the test cases to be in the directory phase4testcases in the
  same directory as the tester is run.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import provided.JottParser;
import provided.JottTokenizer;
import provided.JottTree;
import provided.Token;

public class JottPhase4Tester {

    private static final String TEST_DIR = "phase4testcases";

    /* Holds the Jott produced while round tripping a program. */
    private static final String TEMP_FILE = TEST_DIR + "/phase4TestTemp.jott";

    ArrayList<TestCase> testCases;

    private static class TestCase {
        String testName;
        String fileName;

        public TestCase(String testName, String fileName) {
            this.testName = testName;
            this.fileName = fileName;
        }
    }

    /**
     * Builds one test case per Jott file in the test directory, so a fixture
     * added to that directory is exercised without changing this tester.
     */
    private void createTestCases() {
        this.testCases = new ArrayList<>();

        File directory = new File(TEST_DIR);
        String[] files = directory.list();

        if (files == null) {
            System.err.println("Could not read the directory " + TEST_DIR);
            return;
        }

        Arrays.sort(files);

        for (String file : files) {
            if (!file.endsWith(".jott") || TEMP_FILE.endsWith(file)) {
                continue;
            }
            String name = file.substring(0, file.length() - ".jott".length()).replace('_', ' ');
            testCases.add(new TestCase(name, file));
        }
    }

    /**
     * Translates one program to every target and round trips its Jott.
     *
     * @param test the test case to run
     * @return true if the program translated to all four targets and its Jott
     *         survived a round trip unchanged
     */
    private boolean phase4Test(TestCase test) {

        JottTree root = buildTree(test, TEST_DIR + "/" + test.fileName);

        if (root == null) {
            return false;
        }

        String className = classNameFor(test.fileName);

        String jott = root.convertToJott();
        String java = root.convertToJava(className);
        String c = root.convertToC();
        String python = root.convertToPython();

        if (!checkTarget(test, "Jott", jott)
                || !checkTarget(test, "Java", java)
                || !checkTarget(test, "C", c)
                || !checkTarget(test, "Python", python)) {
            return false;
        }

        // Java requires the class name to match the file name it is written to
        if (!java.contains("class " + className)) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("\t\tGenerated Java does not declare class " + className);
            return false;
        }

        return roundTrip(test, jott, className);
    }

    /**
     * Writes the generated Jott back out, reads it as a program again, and
     * checks that translating it once more produces the same Jott.
     *
     * @param test the test case being run
     * @param jott the Jott produced from the original program
     * @param className the class name to use when regenerating Java
     * @return true if the program survived the round trip unchanged
     */
    private boolean roundTrip(TestCase test, String jott, String className) {

        try {
            FileWriter writer = new FileWriter(TEMP_FILE);
            writer.write(jott);
            writer.close();
        } catch (IOException e) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("\t\tCould not write " + TEMP_FILE + ": " + e.getMessage());
            return false;
        }

        JottTree root = buildTree(test, TEMP_FILE);

        if (root == null) {
            System.err.println("\t\tThe generated Jott is not a valid Jott program");
            return false;
        }

        String second = root.convertToJott();

        if (second == null || !second.equals(jott)) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("\t\tTranslating the generated Jott changed the program");
            System.err.println("\t\tExpected: " + jott);
            System.err.println("\t\tGot     : " + second);
            return false;
        }

        // the other targets must still be produced from the regenerated tree
        return checkTarget(test, "Java", root.convertToJava(className))
                && checkTarget(test, "C", root.convertToC())
                && checkTarget(test, "Python", root.convertToPython());
    }

    /**
     * Runs the earlier phases over one file.
     *
     * @param test the test case being run
     * @param fileName the file to read
     * @return the validated tree, or null if any phase failed
     */
    private JottTree buildTree(TestCase test, String fileName) {

        ArrayList<Token> tokens = JottTokenizer.tokenize(fileName);

        if (tokens == null) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("\t\tExpected a list of tokens from " + fileName + ", but got null");
            return null;
        }

        JottTree root = JottParser.parse(tokens);

        if (root == null) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("\t\tExpected a JottTree from " + fileName + ", but got null");
            return null;
        }

        if (!root.validateTree()) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("\t\tExpected " + fileName + " to be a valid Jott program");
            return null;
        }

        return root;
    }

    /**
     * @param test the test case being run
     * @param target the name of the target language
     * @param generated the code produced for that target
     * @return true if the target produced code
     */
    private boolean checkTarget(TestCase test, String target, String generated) {

        if (generated == null) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("\t\tExpected " + target + " code, but got null");
            return false;
        }

        if (generated.isEmpty()) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("\t\tExpected " + target + " code, but got an empty program");
            return false;
        }

        return true;
    }

    /**
     * Derives the Java class name from a file name the way Jott.java does.
     *
     * @param fileName the Jott source file name
     * @return the class name to generate
     */
    private String classNameFor(String fileName) {

        String name = fileName.substring(0, fileName.length() - ".jott".length());
        StringBuilder result = new StringBuilder();
        boolean capitalize = true;

        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (ch == '_') {
                capitalize = true;
            } else if (capitalize) {
                result.append(Character.toUpperCase(ch));
                capitalize = false;
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    private boolean runTest(TestCase test) {
        System.out.println("Running Test: " + test.testName + " (" + test.fileName + ")");
        return phase4Test(test);
    }

    public static void main(String[] args) {
        System.out.println("NOTE: System.err may print during tests. This is expected for failing cases.");
        JottPhase4Tester tester = new JottPhase4Tester();

        int numTests = 0;
        int passedTests = 0;
        tester.createTestCases();
        for (JottPhase4Tester.TestCase test : tester.testCases) {
            numTests++;
            if (tester.runTest(test)) {
                passedTests++;
                System.out.println("\tPassed\n");
            } else {
                System.out.println("\tFailed\n");
            }
        }

        System.out.printf("Passed: %d/%d%n", passedTests, numTests);
    }
}
