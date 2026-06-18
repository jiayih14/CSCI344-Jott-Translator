/**
 * File name: JottParseException.java
 * Author: Alvin Jiang
 *
 * This file defines the exception class for JottParseException during parsing.
 */

package Nodes;

public class JottParseException extends RuntimeException {

    /**
     * Constructor for Jott Parse Exception
     * @param message exception message
     */
    public JottParseException(String message) {
        super(message);
    }
}
