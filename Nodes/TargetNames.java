/**
 * File name: TargetNames.java
 * Author: Alvin Jiang
 *
 * Jott function names are freer than the names the target languages allow. A
 * Jott program may legally define a function called "gets", "sizeof", or "len",
 * each of which would collide with something the generated code depends on: a C
 * keyword, a function from the headers every generated C program includes, an
 * inherited Java method, or a Python builtin the translator itself calls.
 *
 * This class renames only those names, so ordinary programs keep the names the
 * programmer wrote. The prefix contains an underscore, which the Jott tokenizer
 * never accepts in an identifier, so a renamed function can never collide with
 * another Jott function.
 */

package Nodes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TargetNames {

    private static final String PREFIX = "jott_";

    /** main names the program entry point in every target and is never renamed. */
    private static final String MAIN = "main";

    private static final Set<String> C_RESERVED = new HashSet<>(Arrays.asList(
            // keywords
            "auto", "break", "case", "char", "const", "continue", "default", "do",
            "double", "else", "enum", "extern", "float", "for", "goto", "if",
            "inline", "int", "long", "register", "restrict", "return", "short",
            "signed", "sizeof", "static", "struct", "switch", "typedef", "union",
            "unsigned", "void", "volatile", "while",
            // stdio.h
            "clearerr", "fclose", "feof", "ferror", "fflush", "fgetc", "fgetpos",
            "fgets", "fopen", "fprintf", "fputc", "fputs", "fread", "freopen",
            "fscanf", "fseek", "fsetpos", "ftell", "fwrite", "getc", "getchar",
            "gets", "perror", "printf", "putc", "putchar", "puts", "remove",
            "rename", "rewind", "scanf", "setbuf", "setvbuf", "snprintf",
            "sprintf", "sscanf", "tmpfile", "tmpnam", "ungetc",
            // string.h
            "memchr", "memcmp", "memcpy", "memmove", "memset", "strcat", "strchr",
            "strcmp", "strcoll", "strcpy", "strcspn", "strerror", "strlen",
            "strncat", "strncmp", "strncpy", "strpbrk", "strrchr", "strspn",
            "strstr", "strtok", "strxfrm",
            // stdlib.h
            "abort", "abs", "atexit", "atof", "atoi", "atol", "bsearch", "calloc",
            "div", "exit", "free", "getenv", "labs", "ldiv", "malloc", "qsort",
            "rand", "realloc", "srand", "strtod", "strtol", "strtoul", "system"));

    private static final Set<String> JAVA_RESERVED = new HashSet<>(Arrays.asList(
            // keywords
            "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double",
            "else", "enum", "extends", "final", "finally", "float", "for", "goto",
            "if", "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try", "void",
            "volatile", "while", "yield",
            // methods every class inherits, which a static method cannot hide
            "clone", "equals", "finalize", "getClass", "hashCode", "notify",
            "notifyAll", "toString", "wait",
            // the parameter of the main method the translator writes
            "args"));

    private static final Set<String> PYTHON_RESERVED = new HashSet<>(Arrays.asList(
            // keywords
            "and", "as", "assert", "async", "await", "break", "class", "continue",
            "def", "del", "elif", "else", "except", "finally", "for", "from",
            "global", "if", "import", "in", "is", "lambda", "nonlocal", "not",
            "or", "pass", "raise", "return", "try", "while", "with", "yield",
            // builtins the generated code itself calls
            "int", "len", "print"));

    /**
     * @param name the Jott function name
     * @return the name to use in generated Java
     */
    public static String java(String name) {
        return rename(name, JAVA_RESERVED);
    }

    /**
     * @param name the Jott function name
     * @return the name to use in generated C
     */
    public static String c(String name) {
        return rename(name, C_RESERVED);
    }

    /**
     * @param name the Jott function name
     * @return the name to use in generated Python
     */
    public static String python(String name) {
        return rename(name, PYTHON_RESERVED);
    }

    private static String rename(String name, Set<String> reserved) {
        if (MAIN.equals(name) || !reserved.contains(name)) {
            return name;
        }
        return PREFIX + name;
    }
}
