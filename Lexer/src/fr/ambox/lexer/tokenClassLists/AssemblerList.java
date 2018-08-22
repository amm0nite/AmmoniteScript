package fr.ambox.lexer.tokenClassLists;

import fr.ambox.lexer.TokenClassName;
import fr.ambox.lexer.tokenClasses.DecimalNumberAutomata;
import fr.ambox.lexer.tokenClasses.IdentifierAutomata;
import fr.ambox.lexer.tokenClasses.IntegerNumberAutomata;

public class AssemblerList extends CommonList {
    public AssemblerList() {
        super();

        this.addTokenClass(new IntegerNumberAutomata());
        this.addTokenClass(new DecimalNumberAutomata());

        this.addTokenClass(new IdentifierAutomata(TokenClassName.AssemblerProcedure));
        this.addTokenClass(new IdentifierAutomata(':', TokenClassName.AssemblerName));
        this.addTokenClass(new IdentifierAutomata('$', TokenClassName.AssemblerVariable));
        this.addTokenClass(new IdentifierAutomata('#', TokenClassName.AssemblerInformation));
    }
}
