package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author  
 * Kifekachukwu Nwosu
 **/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JottTokenizer {

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename){

		// reading the file
		ArrayList<Token> tokens = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))){

			StringBuilder incompleteToken = new StringBuilder();
			boolean dotPresent = false;
			String line;
			int lineNum = 0;

			while ((line = br.readLine()) != null){
				lineNum++;
				for (int i = 0; i < line.length(); i++ ){
					char ch = line.charAt(i);

    if (ch == '#') {
        break;
    }

    // whitespace
    else if (Character.isWhitespace(ch)) {
        continue;
    }
	else if (Character.isLetter(ch)) {

						i = identifyKeywordsAndTokens(
                    line,
                    i,
                    filename,
                    lineNum,
                    tokens);
        }
	    // [
    else if (ch == '[') {

        addLeftBracket(
                tokens,
                filename,
                lineNum);
    }

    // ]
    else if (ch == ']') {

        addRightBracket(
                tokens,
                filename,
                lineNum);
    }

    // {
    else if (ch == '{') {

        addLeftBrace(
                tokens,
                filename,
                lineNum);
    }

    // }
    else if (ch == '}') {

        addRightBrace(
                tokens,
                filename,
                lineNum);
    }

    // ;
    else if (ch == ';') {

        addSemicolon(
                tokens,
                filename,
                lineNum);
    }

    // ,
    else if (ch == ',') {

        addComma(
                tokens,
                filename,
               lineNum);
    }

    // :
    else if (ch == ':') {

        i = addColonorFCHeader(
                line,
                i,
                tokens,
                filename,
                lineNum);
    }

    // =
    else if (ch == '=') {

        addAssign(
                tokens,
                filename,
                lineNum);
    }
					// // if char is a digit, append to current ongoing token
					// if (Character.isDigit(ch)){
					// 	incompleteToken.append(ch);
					// }
					
					// else if (ch == '.'){
					// 	// if dot encountered and do is not present
					// 	// if next char is digit, we append to current ongoing token and set dot to present
					// 	if ( i+1< line.length() && Character.isDigit(line.charAt(i+1))){
					// 		incompleteToken.append(ch);
					// 		dotPresent = true;
					// 	}
					// 	else{
					// 		if (!incompleteToken.isEmpty()){
					// 			Token newToken = new Token(incompleteToken.toString(), filename, lineNum, TokenType.NUMBER);
					// 			incompleteToken.setLength(0);
					// 			dotPresent = false;
					// 		}
					// 		incompleteToken.append(ch);
					// 	}
					// }
					    else {

        System.err.println("Syntax Error:");
        System.err.println(
                "Invalid token \"" + ch + "\"");
        System.err.println(
                filename + ":" + lineNum);

        return null;
    }
				}
				

			}
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
		return tokens;
	}
	
	
	
/**
 * 
 * Helper method to identify keywords and identifiers in Jott. If the current character is a letter, we read in characters until we reach a non-letter/digit character. We then check if the resulting string is a keyword or identifier and create the appropriate token.
 * @author   Kifekachukwu Nwosu
 * @param line
 * @param index
 * @param filename
 * @param lineNum
 * @param tokens
 * @return
 */
private static int identifyKeywordsAndTokens(
	
        String line,
        int index,
        String filename,
        int lineNum,
        ArrayList<Token> tokens
) {

    StringBuilder word = new StringBuilder();

    // keep reading letters/numbers
    while (index < line.length() &&
            Character.isLetterOrDigit(line.charAt(index))) {

        word.append(line.charAt(index));
        index++;
    }

    // convert StringBuilder into String
    String identifier = word.toString();

    // create token and add to token list
    tokens.add(new Token(
            identifier,
            filename,
            lineNum,
            TokenType.ID_KEYWORD));


    return index - 1;
}
/**
 * Helper method to add a left bracket token to the token list
 *  @author  
 * Kifekachukwu Nwosu
 * @param tokens
 * @param filename
 * @param lineNum
 */
private static void addLeftBracket(
        ArrayList<Token> tokens,
        String filename,
        int lineNum) {

    tokens.add(new Token(
            "[",
            filename,
            lineNum,
            TokenType.L_BRACKET));
}

/**
 * Helper method to add a right bracket token to the token list
 *  @author  
 * Kifekachukwu Nwosu
 * @param tokens
 * @param filename
 * @param lineNum
 */
private static void addRightBracket(
		ArrayList<Token> tokens,
		String filename,
		int lineNum) {

	tokens.add(new Token(
			"]",
			filename,
			lineNum,
			TokenType.R_BRACKET));
}
/**
 * Helper method to add a left brace token to the token list
 *  @author  
 * Kifekachukwu Nwosu
 * @param tokens
 * @param filename
 * @param lineNum
 */
private static void addComma(
		ArrayList<Token> tokens,
		String filename,
		int lineNum) {

	tokens.add(new Token(
			",",
			filename,
			lineNum,
			TokenType.COMMA));


	}
/**
 * Helper method to add a right brace token to the token list
 * @author	Kifekachukwu Nwosu
 * @param tokens
 * @param filename
 * @param lineNum
 */
	private static void addSemicolon(
			ArrayList<Token> tokens,
			String filename,
			int lineNum) {

		tokens.add(new Token(
				";",
				filename,
				lineNum,
				TokenType.SEMICOLON));	


		}
/**
 * Helper method to add a right brace token to the token list
 *  @author	Kifekachukwu Nwosu	
 * @param tokens
 * @param filename
 * @param lineNum
 */
		private static void addColon(
				ArrayList<Token> tokens,
				String filename,
				int lineNum) {

			tokens.add(new Token(
					":",
					filename,
					lineNum,
					TokenType.COLON));	


			}
/**
 * Helper method to add a right brace token to the token list
 *  @author	Kifekachukwu Nwosu
 * @param tokens
 * @param filename
 * @param lineNum
 */
private static void addAssign(
				ArrayList<Token> tokens,
				String filename,
				int lineNum) {

			tokens.add(new Token(
					"=",
					filename,
					lineNum,
					TokenType.ASSIGN));
			}


/**
 * Helper method to add a right brace token to the token list
 *  @author	Kifekachukwu Nwosu
 * @param tokens
 * @param filename
 * @param lineNum
 */
	private static void addRightBrace(
        ArrayList<Token> tokens,
        String filename,
        int lineNum) {

    tokens.add(new Token(
            "}",
            filename,
            lineNum,
            TokenType.R_BRACE));

}
/**
 * Helper method to add a right brace token to the token list
 *  @author	Kifekachukwu Nwosu
 * @param tokens
 * @param filename
 * @param lineNum
 */
private static void addLeftBrace(
		ArrayList<Token> tokens,
		String filename,
		int lineNum) {

	tokens.add(new Token(
			"{",
			filename,
			lineNum,
			TokenType.L_BRACE));

	}

	/**
	 * Helper method to add a right brace token to the token list	
	 *  @author	Kifekachukwu Nwosu
	 * @param line
	 * @param index
	 * @param tokens
	 * @param filename
	 * @param lineNum
	 * @return
	 */
	private static int addColonorFCHeader(
        String line,
        int index,
        ArrayList<Token> tokens,
        String filename,
        int lineNum) {

    // check if next character is also :
    if (index + 1 < line.length()
            && line.charAt(index + 1) == ':') {

        tokens.add(new Token(
                "::",
                filename,
                lineNum,
                TokenType.FC_HEADER));

        index++;
    }

    else {

        tokens.add(new Token(
                ":",
                filename,
                lineNum,
                TokenType.COLON));
    }

    return index;
}
}
