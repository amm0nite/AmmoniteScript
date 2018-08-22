package fr.ambox.lexer.tokenClasses;

import fr.ambox.lexer.TokenClassName;

public class IntegerNumberAutomata extends NumberAutomata {
    @Override
    public TokenClassName getName() {
        return TokenClassName.IntegerNumber;
    }

    @Override
    public boolean isComplete() {
        return this.state == NumberAutomataState.Integer;
    }
}
