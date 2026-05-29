package provided;

/**
 * This class is responsible for tokenizing Jott code.
 *
 * @author
 * Kifekachukwu Nwosu
 * Jiayi Huang
 * Matt Gleich
 * Alvin Jiang
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

					// = or ==
					else if (ch == '=') {
						if (i + 1 < line.length() && line.charAt(i + 1) == '=') {
							tokens.add(new Token("==", filename, lineNum, TokenType.REL_OP));
							i++;
						} else {
							addAssign(tokens, filename, lineNum);
						}
					}

					// math operators
					else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
						tokens.add(new Token(
								String.valueOf(ch),
								filename,
								lineNum,
								TokenType.MATH_OP));
					}

					// relational operators: < <= > >=
					else if (ch == '<' || ch == '>') {
						if (i + 1 < line.length() && line.charAt(i + 1) == '=') {
							tokens.add(new Token(ch + "=", filename, lineNum, TokenType.REL_OP));
							i++;
						} else {
							tokens.add(new Token(String.valueOf(ch), filename, lineNum, TokenType.REL_OP));
						}
					}

					// != or invalid !
					else if (ch == '!') {
						if (i + 1 < line.length() && line.charAt(i + 1) == '=') {
							tokens.add(new Token("!=", filename, lineNum, TokenType.REL_OP));
							i++;
						} else {
							logError("Invalid token \"!\". \"!\" expects following \"=\"", filename, lineNum);
							return null;
						}
					}

					// . or digit
					else if (ch == '.' || Character.isDigit(ch)){
						i = identifyNumber(tokens, filename, lineNum, i, line);

						if (i == -1){
							return null;
						}
					}

					// string literal
					else if (ch == '"') {
						i = tokenizeString(line, i, filename, lineNum, tokens);
						if (i == -1) {
							return null;
						}
					}

					else {
						logError("Invalid token \"" + ch + "\"", filename, lineNum);
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
	 * Helper method to tokenize number
	 * @author
	 * Jiayi Huang
	 */

	private static int identifyNumber(
			ArrayList<Token> tokens,
			String filename,
			int lineNum,
			int index,
			String line
	){
		StringBuilder number = new StringBuilder();

		boolean dotPresent = false;

		while (index < line.length()) {
			char ch = line.charAt(index);
			if (Character.isDigit(ch)) {
				number.append(ch);
				index++;
			}
			else if (ch == '.'){
				if (
						!dotPresent
						&& (!number.isEmpty() || (index + 1 < line.length() && Character.isDigit(line.charAt(index+1))))){
					number.append(ch);
					dotPresent = true;
					index++;
				}
				else{
					logError("Invalid number format near '.'", filename, lineNum);
					return -1;
				}
			}
			else{
				break;
			}
		}

		tokens.add(new Token (
				number.toString(),
				filename,
				lineNum,
				TokenType.NUMBER
		));

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
		/**
		 * Helper method to tokenize a string literal.
		 * Strings must be enclosed in double quotes, cannot span lines, and may only
		 * contain letters, digits, or spaces.
		 * @author Matt Gleich
		 */
		private static int tokenizeString(
				String line,
				int index,
				String filename,
				int lineNum,
				ArrayList<Token> tokens
		) {
			StringBuilder str = new StringBuilder();
			str.append('"');
			index++; // skip opening quote

			while (index < line.length()) {
				char ch = line.charAt(index);
				if (ch == '"') {
					str.append('"');
					tokens.add(new Token(str.toString(), filename, lineNum, TokenType.STRING));
					return index;
				}
				if (!Character.isLetterOrDigit(ch) && ch != ' ') {
					logError("Invalid character '" + ch + "' in string", filename, lineNum);
					return -1;
				}
				str.append(ch);
				index++;
			}

			logError("Unterminated string literal", filename, lineNum);
			return -1;
		}

		/**
		 * Error logging utility function
		 * @param err_message
		 * @param filename
		 * @param lineNum
		 */
		private static void logError(String err_message, String filename, int lineNum){
			System.err.println("Syntax Error\n" + err_message + "\n" + filename + ":" + lineNum);
		}

	}
