import fr.ambox.compiler.Assembly;
import fr.ambox.compiler.Compiler;
import fr.ambox.compiler.exceptions.SyntaxErrorException;
import fr.ambox.lexer.Token;
import fr.ambox.lexer.TokenClassName;
import fr.ambox.lexer.TokenPosition;
import org.junit.Assert;
import org.junit.Test;

public class CompileTest {

    @Test
    public void syntaxErrorTest() throws Exception {
        String code = "";
        code += "test = 11;\n";
        code += "test = 22\n";
        code += "test = 33;\n";

        try {
            (new Compiler()).compile(code);
            Assert.fail("Should throw SyntaxErrorException");
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof SyntaxErrorException);
            SyntaxErrorException syntaxError = (SyntaxErrorException) e;

            Token unexpected = syntaxError.getUnexpectedToken();
            Token after = syntaxError.getAfterToken();

            Assert.assertEquals(unexpected.getTokenClass().getName(), TokenClassName.Identifier);
            Assert.assertEquals(unexpected.getPosition(), new TokenPosition(3, 1));
            Assert.assertEquals(after.getTokenClass().getName(), TokenClassName.Number);
            Assert.assertEquals(after.getPosition(), new TokenPosition(2, 8));

            Assert.assertTrue(syntaxError.getAlternatives().contains(TokenClassName.Terminator));
        }
    }

    @Test
    public void lineNumberTest() throws Exception {
        String code = "";
        code += "test = 11;\n";
        code += "test = 22;\n";
        code += "test = 33;\n";

        Assembly assembly = (new Compiler()).compile(code);
        String assemblyCode = assembly.toString();

        Assert.assertTrue(assemblyCode.contains("#1"));
        Assert.assertTrue(assemblyCode.contains("#2"));
        Assert.assertTrue(assemblyCode.contains("#3"));
    }
}
