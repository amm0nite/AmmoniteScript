package fr.ambox.compiler;

import fr.ambox.compiler.exceptions.MultipleParseTreeException;
import fr.ambox.compiler.exceptions.SyntaxErrorException;
import fr.ambox.compiler.parser.AmGrammar;
import fr.ambox.compiler.parser.Parser;
import fr.ambox.lexer.Lexer;
import fr.ambox.lexer.NoTokenException;
import fr.ambox.lexer.TokenList;
import fr.ambox.lexer.tokenClassLists.LanguageList;

public class Compiler {
    public Assembly compile(String code) throws NoTokenException, SyntaxErrorException, MultipleParseTreeException {
        Lexer lexer = new Lexer(code, new LanguageList());
        TokenList tokens = lexer.analyse();

        Parser parser = new Parser(tokens, new AmGrammar());
        Assembly assembly = parser.analyse();

        assembly.prepareMeta();
        return assembly;
    }
}
