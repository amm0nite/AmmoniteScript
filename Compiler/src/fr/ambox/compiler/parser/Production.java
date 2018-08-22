package fr.ambox.compiler.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class Production {

	private ArrayList<Symbol> symbols;
    private HashMap<Integer, String> labels;

	public Production() {
		this.symbols = new ArrayList<Symbol>();
        this.labels = new HashMap<Integer, String>();
	}

	public ParseNodeList test(ParserContext context) {
		ParseNodeList list = new ParseNodeList();
		for (int i=0; i<this.symbols.size(); i++) {
            Symbol symbol = this.symbols.get(i);
			ParseNodeList node = symbol.test(context);
			if (node == null) {
				return null;
			}
            node.setLabel(this.labels.get(i));
            list.addAll(node);
		}
		return list;
	}

	public Production add(Symbol symbol) {
        this.symbols.add(symbol);
		return this;
	}

    public Production add(Symbol symbol, String label) {
        this.labels.put(this.symbols.size(), label);
        return this.add(symbol);
    }

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("production");
		for (Symbol symbol: this.symbols) {
			sb.append(' ');
			sb.append(symbol.getClass().getSimpleName());
		}
		return sb.toString();
	}
}
