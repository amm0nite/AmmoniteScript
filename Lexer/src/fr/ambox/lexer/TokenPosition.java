package fr.ambox.lexer;

public class TokenPosition {
	private int line;
	private int lineIndex;
	
	public TokenPosition(int line, int column) {
		this.line = line;
		this.lineIndex = column;
	}
	
	public int getLine() {
		return this.line;
	}
	
	public int getLineIndex() {
		return this.lineIndex;
	}
	
	public String toString() {
		return "line:"+this.line+", char:"+this.lineIndex;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TokenPosition)) {
            return false;
        }
        TokenPosition other = (TokenPosition) obj;
        return (this.line == other.line && this.lineIndex == other.lineIndex);
    }

    public boolean isAfter(TokenPosition other) {
        if (this.line > other.line) {
            return true;
        }
        else if (this.line == other.line) {
            return this.lineIndex > other.lineIndex;
        }
        else {
            return false;
        }
    }
}
