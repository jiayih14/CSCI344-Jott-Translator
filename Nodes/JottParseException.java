/**
 * File name: JottParseException.java
 * Author: Alvin Jiang
 *
 * This file defines the exception class for JottParseException during parsing.
 */

package Nodes;

public class JottParseException extends RuntimeException {

    public JottParseException(String message) {
        super(message);
    }
}
