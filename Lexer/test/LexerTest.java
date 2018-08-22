import fr.ambox.lexer.*;
import fr.ambox.lexer.tokenClassLists.AssemblerList;
import fr.ambox.lexer.tokenClassLists.CommonList;
import fr.ambox.lexer.tokenClassLists.LanguageList;
import org.junit.Assert;
import org.junit.Test;

public class LexerTest {

    @Test
    public void commonTest() throws NoTokenException {
        Lexer lexer = new Lexer("'hello' + 'world'", new CommonList());
        TokenList list = lexer.analyse();
        Token tok = null;

        tok = list.get(0);
        Assert.assertEquals(TokenClassName.String, tok.getTokenClass().getName());
        Assert.assertEquals("'hello'", tok.getLexeme());

        tok = list.get(1);
        Assert.assertEquals(TokenClassName.WhiteSpace, tok.getTokenClass().getName());

        tok = list.get(2);
        Assert.assertEquals(TokenClassName.ArithmeticOperator, tok.getTokenClass().getName());
        Assert.assertEquals("+", tok.getLexeme());

        tok = list.get(3);
        Assert.assertEquals(TokenClassName.WhiteSpace, tok.getTokenClass().getName());

        tok = list.get(4);
        Assert.assertEquals(TokenClassName.String, tok.getTokenClass().getName());
        Assert.assertEquals("'world'", tok.getLexeme());
    }

    @Test
    public void assemblerTest() throws NoTokenException {
        try {
            String code = ":buffer '' def :i 0 def { $i 10 lt } { :buffer $buffer 'a' concat def :i $i 1 + def } while $buffer";
            Lexer lexer = new Lexer(code, new AssemblerList());
            TokenList list = lexer.analyse();
            Assert.assertTrue(list.size() > 0);
        }
        catch (NoTokenException e) {
            System.out.println("no token at " + e.getIndex());
            Assert.fail();
        }
    }

    @Test
    public void scriptStringDefinitionTest() throws Exception {
        try {
            String code = "name = 'Pierre';";
            Lexer lexer = new Lexer(code, new LanguageList());
            TokenList list = lexer.analyse();

            Assert.assertTrue(list.get(0).getTokenClass().getName() == TokenClassName.Identifier);
            Assert.assertTrue(list.get(1).getTokenClass().getName() == TokenClassName.WhiteSpace);
            Assert.assertTrue(list.get(2).getTokenClass().getName() == TokenClassName.Assignator);
            Assert.assertTrue(list.get(3).getTokenClass().getName() == TokenClassName.WhiteSpace);
            Assert.assertTrue(list.get(4).getTokenClass().getName() == TokenClassName.String);
            Assert.assertTrue(list.get(5).getTokenClass().getName() == TokenClassName.Terminator);
        }
        catch (NoTokenException e) {
            System.out.println("no token at " + e.getIndex());
            Assert.fail();
        }
    }
}
