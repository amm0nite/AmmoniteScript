package fr.ambox.lexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import fr.ambox.lexer.tokenClasses.End;

public class Lexer {
	private String code;
	private TokenClassList tokenClassList;
	private ArrayList<Integer> lineStarts;

    public Lexer(String code, TokenClassList list) {
        this.tokenClassList = list;
        this.code = code;
        this.readLines();
    }

	public Lexer(File file, TokenClassList list) throws IOException {
		this(new String(Files.readAllBytes(file.toPath()), "UTF-8"), list);
	}

    private void readLines() {
		this.lineStarts = new ArrayList<Integer>();
		this.lineStarts.add(0);

		char[] chars = code.toCharArray();
		boolean newLine = false;
		for (int i=0; i<chars.length; i++) {
			char c = chars[i];
			if (c == '\n' || c == '\r') {
				newLine = true;
			}
			else {
				if (newLine) {
					newLine = false;
					this.lineStarts.add(i);
				}
			}
		}
	}

	private TokenPosition findTokenPosition(int index) {
		int line = 0;
		int lineIndex = 0;

		for (int lineStart: this.lineStarts) {
			if (index >= lineStart) {
				line += 1;
				lineIndex = index - lineStart;
			}
			else {
				break;
			}
		}

		return new TokenPosition(line, lineIndex + 1);
	}

	public TokenList analyse() throws NoTokenException {
		TokenList tokens = new TokenList();
		char[] chars = code.toCharArray();

		int index = 0;
		while (index < chars.length) {
			int max = 0;
			Token token = null;
			Token best = null;

			String current = "";
			this.tokenClassList.reset();
			for (int i=index; i<chars.length; i++) {
				current += chars[i];
				token = this.tokenClassList.feed(chars[i]);
				if (token != null) {
					if (current.length() > max) {
						token.setPosition(this.findTokenPosition(index));
						best = token;
						max = current.length();
					}
				}

				if (!this.tokenClassList.oneAvailable()) {
					break;
				}
			}

			if (best == null) {
				throw new NoTokenException(index, code.substring(index));
			}

			tokens.add(best);
			index += max;
		}

		Token endToken = new Token(new End(), "");
		endToken.setPosition(this.findTokenPosition(chars.length));
		tokens.add(endToken);

		this.tokenClassList.reset();
		return tokens;
	}

	public void setTokenClassList(TokenClassList list) {
		this.tokenClassList = list;
	}

	public ArrayList<Integer> getLineStarts() {
        return this.lineStarts;
    }
}
