package fr.ambox.compiler.parser;

abstract public class NonTerminalPlus extends NonTerminal {
	@Override
	public ParseNodeList test(ParserContext context) {
		ParseNodeList list = super.test(context);
		if (list == null) {
			return null;
		}
		
		boolean found = true;
		while (found) {
			int save = context.getIndex();
			ParseNodeList res = super.test(context);
			if (res != null) {
				list.addAll(res);
			}
			else {
				found = false;
				context.setIndex(save);
			}
		}
		
		return list;
	}
}
