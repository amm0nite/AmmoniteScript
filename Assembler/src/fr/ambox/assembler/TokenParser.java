package fr.ambox.assembler;

import fr.ambox.assembler.atoms.*;
import fr.ambox.assembler.exceptions.FatalErrorException;
import fr.ambox.assembler.exceptions.TokenParsingException;
import fr.ambox.lexer.Token;

public class TokenParser {
	public static Atom parse(Token tok) throws TokenParsingException {
        Atom result = TokenParser.privateParse(tok);
        result.setTokenPosition(tok.getPosition());
        return result;
	}

	private static Atom privateParse(Token tok) throws TokenParsingException {
        String lexeme = tok.getLexeme();

        switch (tok.getTokenClass().getName()) {
            case WhiteSpace:
                return new NoopAtom();
            case String:
                return new StringAtom(lexeme);
            case IntegerNumber:
                return new IntegerAtom(lexeme);
            case DecimalNumber:
                return new DecimalAtom(lexeme);
            case ArithmeticOperator:
            case ComparisonOperator:
            case ObjectBegin:
            case ObjectEnd:
                return new CallAtom(CallAtom.translateOperator(lexeme));
            case AssemblerProcedure:
                return new CallAtom(lexeme);
            case AssemblerName:
                return new NameAtom(lexeme);
            case AssemblerVariable:
                return new VariableAtom(lexeme);
            case AssemblerInformation:
                return new InformationAtom(lexeme);
            case End:
                return new NoopAtom();
        }

        throw new TokenParsingException();
    }
}
