package fr.ambox.compiler.parser;

import fr.ambox.compiler.exceptions.MultipleParseTreeException;

import java.util.ArrayList;
import java.util.Iterator;

public class ParseNodeList implements Iterable<ParseNode> {
	private ArrayList<ParseNode> list;
	private String label;

	public ParseNodeList() {
		this.list = new ArrayList<ParseNode>();
	}
	
	public ParseNodeList(ParseNode node) {
		this();
		this.list.add(node);
	}

	public void addAll(ParseNodeList list) {
		for (ParseNode node: list) {
            node.setLabel(list.label);
			this.list.add(node);
		}
	}

	@Override
	public Iterator<ParseNode> iterator() {
		return this.list.iterator();
	}

    public ParseNode toParseNode() throws MultipleParseTreeException {
        if (this.list.size() != 1) {
            throw new MultipleParseTreeException();
        }
        return this.list.get(0);
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
