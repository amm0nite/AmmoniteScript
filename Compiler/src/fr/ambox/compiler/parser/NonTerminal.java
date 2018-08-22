package fr.ambox.compiler.parser;

import java.util.ArrayList;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.compiler.generator.Renderable;
import fr.ambox.compiler.generator.Template;

abstract public class NonTerminal implements Symbol, ParsedElement {

	private ArrayList<Production> productions;
	private Renderable renderer;

	public NonTerminal() {
		this.productions = new ArrayList<Production>();
	}
	
	public Production newProduction() {
		Production prod = new Production();
		this.addProduction(prod);
		return prod;
	}

	private void addProduction(Production prod) {
		this.productions.add(prod);
	}

	@Override
	public ParseNodeList test(ParserContext context) {
		int save = context.getIndex();
		for (Production prod: this.productions) {
			ParseNodeList list = prod.test(context);
			if (list != null) {
				ParseNode node = new ParseNode(this);
				node.attachAll(list);
                return new ParseNodeList(node);
			}
			else {
				context.setIndex(save);
			}
		}
		return null;
	}

	public Template makeTemplate() {
		Template template = new Template();
        this.renderer = template;
        return template;
	}

	public void setRenderer(Renderable renderer) {
		this.renderer = renderer;
	}

	public abstract void defineProductions(Grammar grammar);

	public abstract void defineTemplate(Grammar grammar);

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

    @Override
    public AtomList generate(AtomListBag bag) {
        return this.renderer.render(bag);
    }
}
