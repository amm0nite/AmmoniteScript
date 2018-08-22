package fr.ambox.lexer.tokenClassLists;

import fr.ambox.lexer.TokenClassList;
import fr.ambox.lexer.TokenClassName;
import fr.ambox.lexer.tokenClasses.*;

public class CommonList extends TokenClassList {
    public CommonList() {
        super();

        this.addTokenClass(new SpaceAutomata());
        this.addTokenClass(new StringAutomata());

        this.addTokenClass(new OperatorsAutomata(new String[] { "+", "-", "*", "/", "%" }, TokenClassName.ArithmeticOperator));
        this.addTokenClass(new OperatorsAutomata(new String[] { "==", "<", ">", "<=", ">=" }, TokenClassName.ComparisonOperator));

        this.addTokenClass(new KeywordAutomata("{", TokenClassName.ObjectBegin));
        this.addTokenClass(new KeywordAutomata("}", TokenClassName.ObjectEnd));
    }
}
