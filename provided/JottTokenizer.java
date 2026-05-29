package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
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
					if (Character.isLetter(ch)) {

						i = identifyKeywordsAndTokens(
                    line,
                    i,
                    filename,
                    lineNum,
                    tokens);
        }
    if (ch == '#') {
        break;
    }

    // whitespace
    else if (Character.isWhitespace(ch)) {
        continue;
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
				}

			}
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
		return tokens;
	}
	
	
	
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

	private static int addColonorFCHeader(
        String line,
        int index,
        ArrayList<Token> tokens,
        String filename,
        int lineNum) {

    // check if next character is also :
    if (index + 1 < line.length()
            && line.charAt(index + 1) == ':') {

        // add :: token
        tokens.add(new Token(
                "::",
                filename,
                lineNum,
                TokenType.FC_HEADER));

        // skip over second :
        index++;
    }

    // otherwise just regular :
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
