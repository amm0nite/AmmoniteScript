package fr.ambox.compiler.parser;

abstract public class NonTerminalQues extends NonTerminal {
	@Override
	public ParseNodeList test(ParserContext context) {
		ParseNodeList list = new ParseNodeList();
		
		int save = context.getIndex();
		ParseNodeList res = super.test(context);
		if (res != null) {
			list.addAll(res);
		}
		else {
			context.setIndex(save);
		}
		
		return list;
	}
}
