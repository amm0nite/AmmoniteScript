package fr.ambox.compiler.parser;

import fr.ambox.assembler.Atom;
import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.assembler.atoms.*;
import fr.ambox.lexer.Token;
import fr.ambox.lexer.TokenClassName;
import fr.ambox.lexer.TokenPosition;

public class ParsedToken implements ParsedElement {
    private Token token;

    public ParsedToken(Token token) {
        this.token = token;
    }

    public Atom generateAtom() {
        String lexeme = this.token.getLexeme();
        TokenClassName tokenClassName = this.token.getTokenClass().getName();

        switch (tokenClassName) {
            case Identifier:
                return new NameAtom(":" + lexeme);
            case KeywordTrue:
            case KeywordFalse:
            case KeywordNull:
                return new CallAtom(lexeme);
            case Number:
                if (lexeme.contains(".")) {
                    return new DecimalAtom(lexeme);
                }
                return new IntegerAtom(lexeme);
            case String:
                return new StringAtom(lexeme);
            case ArithmeticOperator:
            case ComparisonOperator:
                return new NameAtom(":" + CallAtom.translateOperator(lexeme));
        }

        return null;
    }

    public TokenPosition getPosition() {
        return this.token.getPosition();
    }

    @Override
    public AtomList generate(AtomListBag bag) {
        AtomList list = new AtomList();
        Atom atom = this.generateAtom();
        if (atom != null) {
            list.add(atom);
        }
        return list;
    }

    @Override
    public String getName() {
        return this.token.getTokenClass().getName() + "";
    }
}
