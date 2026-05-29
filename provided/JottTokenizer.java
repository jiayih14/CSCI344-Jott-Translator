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
				TokenType type = null;
				for (int i = 0; i < line.length(); i++ ){
					char ch = line.charAt(i);

					// if char is a digit, append to current ongoing token
					if (Character.isDigit(ch)){
						if(type ==  null){
							type = TokenType.NUMBER;
						}
						incompleteToken.append(ch);
					}
					else if (ch == '.'){
						// if dot encountered
						// if next char is digit, we append to current ongoing token and set dot to present
						if ( i+1< line.length()
								&& Character.isDigit(line.charAt(i+1))
								&& !dotPresent
								&& (type == null || type == TokenType.NUMBER)){

							type = TokenType.NUMBER;
							incompleteToken.append(ch);
							dotPresent = true;
						}
						// else if the previous char is digit, we append the dot to ongoing token
						// and announce token type Number.
						else if (type == TokenType.NUMBER){
							incompleteToken.append(ch);
							Token new_token = new Token(incompleteToken.toString(), filename, lineNum, TokenType.NUMBER);
							tokens.add(new_token);
							incompleteToken.setLength(0);
							dotPresent = false;
							type = null;
						}
						// if neither of above are true, we have encountered an error
						else{
							//error
						}
					}
				}

			}
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
		return null;
	}
}