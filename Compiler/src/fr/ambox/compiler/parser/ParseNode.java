package fr.ambox.compiler.parser;

import fr.ambox.assembler.Atom;
import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.assembler.atoms.InformationAtom;
import fr.ambox.lexer.Token;
import fr.ambox.lexer.TokenClassName;
import fr.ambox.lexer.TokenPosition;

import java.util.ArrayList;

public class ParseNode {

	private ParsedElement element;
	private ArrayList<ParseNode> children;
    private String label;

	public ParseNode(ParsedElement element) {
		this.element = element;
		this.children = new ArrayList<ParseNode>();
	}

	public void attachAll(ParseNodeList list) {
		for (ParseNode node: list) {
			this.attach(node);
		}
	}

	public void attach(ParseNode node) {
		if (node == null) {
            throw new RuntimeException("Can not attach null to ParseNode.");
        }
        this.children.add(node);
	}

	public ParsedElement getElement() {
		return this.element;
	}
	
	public String toString() {
		return this.toStringTree(0);
	}
	
	private String toStringTree(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<level; i++) {
			sb.append("  ");
		}
		sb.append("-> ");
        if (this.label != null) {
            sb.append("__");
            sb.append(this.label);
            sb.append("__ ");
        }
        sb.append(this.element.getClass().getSimpleName());
		if (!this.children.isEmpty()) {
			for (ParseNode child: this.children) {
				sb.append('\n');
				sb.append(child.toStringTree(level + 1));
			}
		}
		return sb.toString();
	}
	
	public ParseNode toAST() {
		ParseNode newNode = new ParseNode(this.element);
        newNode.label = this.label;
		
		for (ParseNode child: this.children) {
			ParseNode newChild = child.toAST();
			if (newChild != null) {
				newNode.attach(newChild);
			}
		}
		
		ArrayList<TokenClassName> removeList = new ArrayList<TokenClassName>();
		removeList.add(TokenClassName.ParenthesisBegin);
		removeList.add(TokenClassName.ParenthesisEnd);
		removeList.add(TokenClassName.ObjectBegin);
		removeList.add(TokenClassName.ObjectEnd);
		removeList.add(TokenClassName.ArrayBegin);
		removeList.add(TokenClassName.ArrayEnd);
		removeList.add(TokenClassName.Assignator);
		removeList.add(TokenClassName.Terminator);
		removeList.add(TokenClassName.Dot);
		removeList.add(TokenClassName.End);
		if (this.element instanceof Token) {
			Token token = (Token) this.element;
			if (removeList.contains(token.getTokenClass().getName())) {
				return null;
			}
		}
		
		return newNode;
	}

    public AtomList generate() {
        //System.out.println("generating "+this.hashCode());

        // generate children
        AtomListBag bag = new AtomListBag();
        if (!this.children.isEmpty()) {
            for (ParseNode child : this.children) {
                AtomList list = child.generate();
                if (list != null && !list.isEmpty()) {
                    String key = (child.label != null) ? child.label : child.element.getName();
                    bag.put(key, list);
                }
            }
        }

        // generate node
        AtomList generated = this.element.generate(bag);
        return this.prefixLineNumber(generated);
    }

    private AtomList prefixLineNumber(AtomList code) {
        if (code == null) {
            return null;
        }

        if (code.isEmpty()) {
            return code;
        }

        TokenPosition position = this.getPosition();
        if (position == null) {
            return code;
        }

        int positionLine = position.getLine();

        AtomList newCode = new AtomList();
        newCode.add(new InformationAtom("#" + positionLine));

        boolean filtering = true;
        for (Atom atom: code) {
            boolean skip = false;
            if (atom.isBlockBoundary()) {
                filtering = false;
            }

            if (filtering && atom instanceof InformationAtom) {
                InformationAtom infoAtom = (InformationAtom) atom;
                if (infoAtom.isLineNumber()) {
                    int lineNumber = Integer.parseInt(infoAtom.getValue());
                    if (lineNumber == positionLine) {
                        skip = true;
                    }
                }
            }

            if (!skip) {
                newCode.add(atom);
            }
        }

        return newCode;
    }

    public TokenPosition getPosition() {
        if (this.element instanceof ParsedToken) {
            ParsedToken parsedToken = (ParsedToken) this.element;
            return parsedToken.getPosition();
        }
        if (this.children.size() > 0) {
            return this.children.get(0).getPosition();
        }
        return null;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
