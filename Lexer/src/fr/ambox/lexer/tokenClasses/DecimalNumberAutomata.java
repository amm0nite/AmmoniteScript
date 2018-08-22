package fr.ambox.lexer.tokenClasses;

import fr.ambox.lexer.TokenClassName;

public class DecimalNumberAutomata extends NumberAutomata {
    @Override
    public TokenClassName getName() {
        return TokenClassName.DecimalNumber;
    }

    @Override
    public boolean isComplete() {
        return this.state == NumberAutomataState.Decimal;
    }
}
