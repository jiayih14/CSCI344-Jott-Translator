package testers;

/*
  Jott semantic analysis tester. This will test Phase 3 (semantic
  analysis / validateTree()) of the Jott project.

  This tester assumes a working and valid tokenizer and parser, and
  runs the full pipeline: tokenize -> parse -> validateTree().

  This expects the test cases to be in the directory phase3testcases in the
  same directory as the tester is run.
 */

import java.util.ArrayList;

import provided.JottParser;
import provided.JottTokenizer;
import provided.JottTree;
import provided.Token;

public class JottSemanticTester {
    ArrayList<TestCase> testCases;

    private static class TestCase {
        String testName;
        String fileName;
        boolean error;

        public TestCase(String testName, String fileName, boolean error) {
            this.testName = testName;
            this.fileName = fileName;
            this.error = error;
        }
    }

    private void createTestCases() {
        this.testCases = new ArrayList<>();

        // Pre-existing Phase 3 fixtures
        testCases.add(new TestCase("hello world", "helloWorld.jott", false));
        testCases.add(new TestCase("provided writeup example1", "providedExample1.jott", false));
        testCases.add(new TestCase("larger valid program", "largerValid.jott", false));
        testCases.add(new TestCase("func call invalid param type (error)", "funcCallParamInvalid.jott", true));
        testCases.add(new TestCase("call to undefined function (error)", "funcNotDefined.jott", true));
        testCases.add(new TestCase("relop type mismatch via func return (error)", "funcReturnInExpr.jott", true));
        testCases.add(new TestCase("wrong return type in func (error)", "funcWrongParamType.jott", true));
        testCases.add(new TestCase("bad fixture, lowercase def (error)", "ifStmtReturns.jott", true));
        testCases.add(new TestCase("main return type not Void (error)", "mainReturnNotInt.jott", true));
        testCases.add(new TestCase("bad fixture, lowercase def (error)", "mismatchedReturn.jott", true));
        testCases.add(new TestCase("missing function call params (error)", "missingFuncParams.jott", true));
        testCases.add(new TestCase("missing main (error)", "missingMain.jott", true));
        testCases.add(new TestCase("missing return non-Void func (error)", "missingReturn.jott", true));
        testCases.add(new TestCase("if without full return coverage (error)", "noReturnIf.jott", true));
        testCases.add(new TestCase("return only in while (error)", "noReturnWhile.jott", true));
        testCases.add(new TestCase("return type id mismatch (error)", "returnId.jott", true));
        testCases.add(new TestCase("bad fixture, lowercase def (error)", "validLoop.jott", true));
        testCases.add(new TestCase("void assigned to typed return (error)", "voidReturn.jott", true));
        testCases.add(new TestCase("bad fixture, lowercase def (error)", "whileKeyword.jott", true));

        // Task 1: builtin function completeness
        testCases.add(new TestCase("valid print with all 4 types", "valid_print.jott", false));
        testCases.add(new TestCase("print with zero args (error)", "print_zero_args.jott", true));
        testCases.add(new TestCase("print with multiple args (error)", "print_multi_args.jott", true));
        testCases.add(new TestCase("print with Void-returning arg (error)", "print_void_arg.jott", true));
        testCases.add(new TestCase("valid concat", "concat_valid.jott", false));
        testCases.add(new TestCase("concat wrong arg type (error)", "concat_invalid_type.jott", true));
        testCases.add(new TestCase("concat wrong arity (error)", "concat_invalid_arity.jott", true));
        testCases.add(new TestCase("valid length", "length_valid.jott", false));
        testCases.add(new TestCase("length wrong arg type (error)", "length_invalid_type.jott", true));
        testCases.add(new TestCase("nested builtin calls", "nested_builtin.jott", false));
        testCases.add(new TestCase("redefine print (error)", "redefine_print.jott", true));
        testCases.add(new TestCase("redefine concat (error)", "redefine_concat.jott", true));
        testCases.add(new TestCase("redefine length (error)", "redefine_length.jott", true));

        // Task 2: function declaration ordering
        testCases.add(new TestCase("backward reference", "backward_reference.jott", false));
        testCases.add(new TestCase("forward reference (error)", "forward_reference.jott", true));
        testCases.add(new TestCase("self recursion", "self_recursion.jott", false));
        testCases.add(new TestCase("duplicate function name (error)", "duplicate_function.jott", true));
        testCases.add(new TestCase("main calling forward-defined function (error)", "main_first_forward_call.jott", true));
        testCases.add(new TestCase("main defined mid-file", "main_in_middle.jott", false));
        testCases.add(new TestCase("mutual recursion (error)", "mutual_recursion.jott", true));

        // Task 3: if/else return-path validation
        testCases.add(new TestCase("both branches return", "both_branches_return.jott", false));
        testCases.add(new TestCase("missing else with return inside (error)", "missing_else.jott", true));
        testCases.add(new TestCase("only one branch returns (error)", "one_branch_returns.jott", true));
        testCases.add(new TestCase("nested if satisfies outer branch, recursive proof", "nested_if_recursive_proof.jott", false));
        testCases.add(new TestCase("Void function partial returns", "void_function.jott", false));
        testCases.add(new TestCase("plain trailing return", "plain_trailing_return.jott", false));
        testCases.add(new TestCase("if/elseif/else all return", "elseif_chain_all_return.jott", false));
        testCases.add(new TestCase("elseif missing return (error)", "elseif_missing_return.jott", true));

        // Task 4: literal division-by-zero detection
        testCases.add(new TestCase("integer literal div by zero (error)", "int_div_zero.jott", true));
        testCases.add(new TestCase("double literal div by zero (error)", "double_div_zero.jott", true));
        testCases.add(new TestCase("negative zero literal divisor (error)", "neg_zero_div.jott", true));
        testCases.add(new TestCase("valid division", "valid_division.jott", false));
        testCases.add(new TestCase("variable divisor not flagged", "variable_divisor.jott", false));
        testCases.add(new TestCase("function call divisor not flagged", "funccall_divisor.jott", false));
        testCases.add(new TestCase("zero with non-division ops", "zero_with_other_ops.jott", false));
        testCases.add(new TestCase("zero as dividend", "zero_as_dividend.jott", false));

        // Task 5: lowercase identifier validation
        testCases.add(new TestCase("uppercase variable (error)", "uppercase_variable.jott", true));
        testCases.add(new TestCase("uppercase first param (error)", "uppercase_first_param.jott", true));
        testCases.add(new TestCase("uppercase second param (error)", "uppercase_second_param.jott", true));
        testCases.add(new TestCase("uppercase function name (error)", "uppercase_function_name.jott", true));
        testCases.add(new TestCase("all lowercase valid", "all_lowercase_valid.jott", false));
        testCases.add(new TestCase("function/variable shared name", "shared_name_namespace.jott", false));

        // Task 6: semantic error formatting (behavioral outcomes, unaffected by message format)
        testCases.add(new TestCase("main with parameters (error)", "main_with_params.jott", true));
        testCases.add(new TestCase("main missing entirely (error)", "main_missing.jott", true));
    }

    private boolean semanticTest(TestCase test) {
        ArrayList<Token> tokens = JottTokenizer.tokenize("phase3testcases/" + test.fileName);

        if (tokens == null) {
            return test.error;
        }

        JottTree root = JottParser.parse(tokens);

        if (root == null) {
            return test.error;
        }

        boolean valid = root.validateTree();

        return test.error ? !valid : valid;
    }

    private boolean runTest(TestCase test) {
        System.out.println("Running Test: " + test.testName + " (" + test.fileName + ")");
        return semanticTest(test);
    }

    public static void main(String[] args) {
        System.out.println("NOTE: System.err may print during tests. This is expected for error cases.");
        JottSemanticTester tester = new JottSemanticTester();

        int numTests = 0;
        int passedTests = 0;
        tester.createTestCases();
        for (JottSemanticTester.TestCase test : tester.testCases) {
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
