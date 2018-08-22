package fr.ambox.lexer.tokenClassLists;

import fr.ambox.lexer.TokenClassName;
import fr.ambox.lexer.tokenClasses.*;

public class LanguageList extends CommonList {
    public LanguageList() {
        super();

        this.addTokenClass(new KeywordAutomata("if", TokenClassName.KeywordIf));
        this.addTokenClass(new KeywordAutomata("elif", TokenClassName.KeywordElif));
        this.addTokenClass(new KeywordAutomata("else", TokenClassName.KeywordElse));
        this.addTokenClass(new KeywordAutomata("for", TokenClassName.KeywordFor));
        this.addTokenClass(new KeywordAutomata("while", TokenClassName.KeywordWhile));
        this.addTokenClass(new KeywordAutomata("function", TokenClassName.KeywordFunction));
        this.addTokenClass(new KeywordAutomata("return", TokenClassName.KeywordReturn));
        this.addTokenClass(new KeywordAutomata("in", TokenClassName.KeywordIn));
        this.addTokenClass(new KeywordAutomata("true", TokenClassName.KeywordTrue));
        this.addTokenClass(new KeywordAutomata("false", TokenClassName.KeywordFalse));
        this.addTokenClass(new KeywordAutomata("null", TokenClassName.KeywordNull));
        this.addTokenClass(new KeywordAutomata("try", TokenClassName.KeywordTry));
        this.addTokenClass(new KeywordAutomata("catch", TokenClassName.KeywordCatch));
        this.addTokenClass(new KeywordAutomata("finally", TokenClassName.KeywordFinally));
        this.addTokenClass(new KeywordAutomata("throw", TokenClassName.KeywordThrow));

        this.addTokenClass(new NumberAutomata());
        this.addTokenClass(new IdentifierAutomata());

        this.addTokenClass(new CommentAutomata());
        this.addTokenClass(new CommentBlockAutomata());

        this.addTokenClass(new KeywordAutomata(",", TokenClassName.ValueSeparator));
        this.addTokenClass(new KeywordAutomata(":", TokenClassName.KeyValueSeparator));

        this.addTokenClass(new KeywordAutomata("(", TokenClassName.ParenthesisBegin));
        this.addTokenClass(new KeywordAutomata(")", TokenClassName.ParenthesisEnd));
        this.addTokenClass(new KeywordAutomata("[", TokenClassName.ArrayBegin));
        this.addTokenClass(new KeywordAutomata("]", TokenClassName.ArrayEnd));

        this.addTokenClass(new KeywordAutomata(".", TokenClassName.Dot));
        this.addTokenClass(new KeywordAutomata("=", TokenClassName.Assignator));
        this.addTokenClass(new KeywordAutomata(";", TokenClassName.Terminator));
    }
}
