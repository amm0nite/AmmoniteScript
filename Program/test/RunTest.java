
import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Trace;
import fr.ambox.assembler.datas.*;
import fr.ambox.assembler.exceptions.*;
import fr.ambox.program.Program;
import fr.ambox.program.ProgramState;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RunTest {

    @Test
    public void stringDefinitionTest() throws Exception {
        String code = "name = 'Pierre';";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data data = context.getDictStack().get("name");
        Assert.assertTrue(data instanceof StringData);
        StringData actual = (StringData) data;
        Assert.assertEquals(new StringData("Pierre"), actual);
    }

    @Test
    public void arrayDefinitionTest() throws Exception {
        String code = "tab = [11, 22, 99, 33];";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data data = context.getDictStack().get("tab");
        Assert.assertTrue(data instanceof ArrayData);
        ArrayData arrayData = (ArrayData) data;
        ArrayData expected = new ArrayData();
        expected.append(new IntegerData(11));
        expected.append(new IntegerData(22));
        expected.append(new IntegerData(99));
        expected.append(new IntegerData(33));
        Assert.assertTrue(arrayData.isLike(expected));
    }

    @Test
    public void nestedArrayAccessTest() throws Exception {
        String code = "";
        code += "arr = [[1,2], [3,4]];";
        code += "val = arr[1][1];";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data data = context.getDictStack().get("val");
        Assert.assertTrue(data instanceof IntegerData);
        IntegerData actual = (IntegerData) data;
        Assert.assertEquals(new IntegerData(4), actual);
    }

    @Test
    public void tableDefinitionTest() throws Exception {
        String code = "";
        code += "users = { 'alice':11, delphine:44 };";
        code += "users.bob = 22;";
        code += "users['cecile'] = 33;";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data data = context.getDictStack().get("users");
        Assert.assertTrue(data instanceof TableData);
        TableData table = (TableData) data;
        Assert.assertTrue(table.has("alice"));
        Assert.assertTrue(table.has("bob"));
        Assert.assertTrue(table.has("cecile"));
        Assert.assertTrue(table.has("delphine"));
        Assert.assertEquals(new IntegerData(11), table.get("alice"));
        Assert.assertEquals(new IntegerData(22), table.get("bob"));
        Assert.assertEquals(new IntegerData(33), table.get("cecile"));
        Assert.assertEquals(new IntegerData(44), table.get("delphine"));
    }

    @Test
    public void arrayIsNotTableTest() throws Exception {
        String code = "";
        code += "arr = [];";
        code += "arr['key'] = 42;";

        Program program = Program.make(code);
        program.run();

        Assert.assertEquals(ProgramState.Error, program.getState());
        FatalErrorException fatalError = program.getError();
        Assert.assertNotNull(fatalError);

        Assert.assertTrue(fatalError instanceof DictOperationErrorException);
        DictOperationErrorException error = (DictOperationErrorException) fatalError;
        DictOperationException exception = error.getException();
        Assert.assertTrue(exception instanceof ArrayKeyTypeException);
    }

    @Test
    public void maxForTest() throws Exception {
        StringBuilder code = new StringBuilder();
        code.append("tab = [11, 22, 99, 33];");
        code.append("max = 0;");
        code.append("for k, v in tab {");
        code.append("   if (v > max) {");
        code.append("       max = v;");
        code.append("   }");
        code.append("}");

        Program program = Program.make(code.toString());
        Context context = program.getContext();
        program.run();

        Data data = context.getDictStack().get("max");
        Assert.assertTrue(data instanceof IntegerData);
        IntegerData actual = (IntegerData) data;
        IntegerData expected = new IntegerData(99);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void undefinedVariableTest() throws Exception {
        String code = "trace(myvar);";

        Program program = Program.make(code);
        program.run();

        Assert.assertEquals(ProgramState.Error, program.getState());
        Assert.assertNotNull(program.getError());
        FatalErrorException fatalError = program.getError();
        Assert.assertTrue(fatalError instanceof UndefinedException);
    }

    @Test
    public void scopeTest() throws Exception {
        String code = "";
        code += "test = 11;";
        code += "print(test);";
        code += "f = function() {";
        code += "   print(test);";
        code += "   test = 22;";
        code += "   print(test);";
        code += "};";
        code += "f();";
        code += "print(test);";

        Program program = Program.make(code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        program.getContext().setStdout(out);
        program.run();

        String expected = "11\n11\n22\n22\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void nullUndefinedPropertyTest() throws Exception {
        String code = "";
        code += "user = { 'name': 'Pierre' };";
        code += "age = user.age;";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data actual = context.getDictStack().get("age");
        Assert.assertTrue(actual instanceof NullData);
    }

    @Test
    public void functionReturnTest() throws Exception {
        String code = "";
        code += "test = 11;";
        code += "f = function(i) { return (i + 1); test = 22; };";
        code += "res = f(0);";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data testData = context.getDictStack().get("test");
        Assert.assertEquals(new IntegerData(11), testData);

        Data resData = context.getDictStack().get("res");
        Assert.assertEquals(new IntegerData(1), resData);
    }

    @Test
    public void functionReturnNoDataTest() throws Exception {
        String code = "";
        code += "res = 11;";
        code += "f = function() { return; print('testing'); };";
        code += "res = f();";

        Program program = Program.make(code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        program.getContext().setStdout(out);
        program.run();

        Context context = program.getContext();
        Assert.assertTrue(out.toString().isEmpty());
        Data actual = context.getDictStack().get("res");
        Assert.assertTrue(actual instanceof NullData);
    }

    @Test
    public void methodTest() throws Exception {
        String code = "";
        code += "obj = { 'testme': function() { print('hello'); } };";
        code += "obj.testme();";

        Program program = Program.make(code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        program.getContext().setStdout(out);
        program.run();

        Assert.assertEquals("hello\n", out.toString());
    }

    @Test
    public void arrayMethodsTest() throws Exception {
        String code = "";
        code += "tab = ['orange', 'banane', 'fraise'];";
        code += "tab.append('patate');";
        code += "size = tab.length();";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data testData = context.getDictStack().get("size");
        Assert.assertEquals(new IntegerData(4), testData);
    }

    @Test
    public void stringMethodsTest() throws Exception {
        String code = "";
        code += "str = 'Hello world';";
        code += "len = str.length();";
        code += "char = str.charAt(4);";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data strData = context.getDictStack().get("str");
        Assert.assertEquals(new StringData("Hello world"), strData);
        Data lenData = context.getDictStack().get("len");
        Assert.assertEquals(new IntegerData(11), lenData);
        Data charData = context.getDictStack().get("char");
        Assert.assertEquals(new StringData("o"), charData);
    }

    @Test
    public void anonymousFunctionTest() throws Exception {
        String code = "(function() { print('Hello world'); })();";

        Program program = Program.make(code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        program.getContext().setStdout(out);
        program.run();

        Assert.assertEquals("Hello world\n", out.toString());
    }

    @Test
    public void thisTest() throws Exception {
        String code = "";
        code += "tab = { 'a':11, 'b':22, 'f':function() { return (this.a + this.b); } };";
        code += "res = tab.f();";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data resData = context.getDictStack().get("res");
        Assert.assertEquals(new IntegerData(33), resData);
    }

    @Test
    public void arithmeticAssignatorTest() throws Exception {
        String code = "";
        code += "i = 10;";
        code += "i += -2;";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data resData = context.getDictStack().get("i");
        Assert.assertEquals(new IntegerData(8), resData);
    }

    @Test
    public void arrayForeachTest() throws Exception {
        String code = "";
        code += "tab = range(10);";
        code += "sum = 0;";
        code += "for (elt in tab) {";
        code += "    sum += elt;";
        code += "}";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data sumData = context.getDictStack().get("sum");
        Assert.assertEquals(new IntegerData(45), sumData);
    }

    @Test
    public void tableForeachTest() throws Exception {
        String code = "";
        code += "myTable = { 'alice':11, 'bob':22, 'cecile':33 };";
        code += "for (name, value in myTable) {";
        code += "    print(((name + ': ') + value));";
        code += "}";

        Program program = Program.make(code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        program.getContext().setStdout(out);
        program.run();

        String expected = "alice: 11\nbob: 22\ncecile: 33\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void tryCatchTest() throws Exception {
        String code = "";
        code += "error = null;";
        code += "try {";
        code += "   print(variable);";
        code += "} catch(e) {";
        code += "   error = e;";
        code += "}";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data errorData = context.getDictStack().get("error");
        Assert.assertNotEquals(new NullData(), errorData);
        Assert.assertTrue(errorData instanceof TableData);
    }

    @Test
    public void throwTest() throws Exception {
        String code = "";
        code += "test = 11;";
        code += "try {";
        code += "   throw 22;";
        code += "   throw 33;";
        code += "} catch (e) {";
        code += "   test = e;";
        code += "}";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data testData = context.getDictStack().get("test");
        Assert.assertEquals(new IntegerData(22), testData);
    }

    @Test
    public void finallyTest() throws Exception {
        String code = "";
        code += "text = 'a';";
        code += "try {";
        code += "   test = print(text[10]);";
        code += "   text = (text + 'b');";
        code += "} catch (e) {";
        code += "   text = (text + 'c');";
        code += "} finally {";
        code += "   text = (text + 'd');";
        code += "}";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data textData = context.getDictStack().get("text");
        Assert.assertEquals(new StringData("acd"), textData);
    }

    @Test
    public void returnInTryTest() throws Exception {
        String code = "";
        code += "test = function() {";
        code += "   try {";
        code += "       return 11;";
        code += "   } catch (e) {";
        code += "       return 22;";
        code += "   }";
        code += "};";
        code += "result = test();";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data resultData = context.getDictStack().get("result");
        Assert.assertEquals(new IntegerData(11), resultData);
    }

    @Test
    public void dataThrowTest() throws Exception {
        String code = "";
        code += "//line\n";
        code += "//line\n";
        code += "throw('myexception');\n";
        code += "//line\n";
        code += "//line\n";


        Program program = Program.make(code);
        program.run();

        Assert.assertEquals(ProgramState.Error, program.getState());
        FatalErrorException fatalError = program.getError();
        Assert.assertNotNull(fatalError);

        Assert.assertTrue(fatalError instanceof WrapDataException);
        WrapDataException dataWrap = (WrapDataException) fatalError;
        Assert.assertEquals(new StringData("myexception"), dataWrap.toData());
    }

    @Test
    public void closureTest() throws Exception {
        String code = "";
        code += "isdiv = function(a) {";
        code += "   return function(x) { ";
        code += "       res = (x % a);";
        code += "       if (res == 0) {";
        code += "           return true;";
        code += "       }";
        code += "       return false;";
        code += "   };";
        code += "};";
        code += "isdiv2 = isdiv(2);";
        code += "test1 = isdiv2(11);";
        code += "test2 = isdiv2(22);";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data test1Data = context.getDictStack().get("test1");
        Data test2Data = context.getDictStack().get("test2");
        Assert.assertEquals(new BooleanData(false), test1Data);
        Assert.assertEquals(new BooleanData(true), test2Data);
    }

    @Test
    public void integerReferenceTest() throws Exception {
        String code = "";
        code += "a = 11;";
        code += "b = a;";
        code += "a = 22;";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data aData = context.getDictStack().get("a");
        Data bData = context.getDictStack().get("b");
        Assert.assertEquals(new IntegerData(22), aData);
        Assert.assertEquals(new IntegerData(11), bData);
    }

    @Test
    public void stringReferenceTest() throws Exception {
        String code = "";
        code += "a = 'Hello';";
        code += "b = a;";
        code += "a = 'World';";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data aData = context.getDictStack().get("a");
        Data bData = context.getDictStack().get("b");
        Assert.assertEquals(new StringData("World"), aData);
        Assert.assertEquals(new StringData("Hello"), bData);
    }

    @Test
    public void tableReferenceTest() throws Exception {
        String code = "";
        code += "a = {'attr':11};";
        code += "b = a;";
        code += "a.attr = 22;";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        TableData aData = (TableData) context.getDictStack().get("a");
        TableData bData = (TableData) context.getDictStack().get("b");
        Assert.assertEquals(new IntegerData(22), aData.get("attr"));
        Assert.assertEquals(new IntegerData(22), bData.get("attr"));
    }

    @Test
    public void fatalErrorTraceOriginTest() throws Exception {
        String code = "";
        code += "// comment\n";
        code += "main = function() {\n";
        code += "   tab = [11, 22, 33];\n";
        code += "   print(tab[3]);\n";
        code += "};\n";
        code += "main();\n";

        Program program = Program.make(code);
        program.run();

        Assert.assertEquals(ProgramState.Error, program.getState());
        FatalErrorException fatalError = program.getError();
        Assert.assertNotNull(fatalError);

        Trace trace = fatalError.getOrigin();
        Assert.assertEquals("main", trace.getFunctionName());
        Assert.assertEquals(4, trace.getLine());
    }

    @Test
    public void fatalErrorTraceCallsTest() throws Exception {
        String code = "";
        code += "// comment\n";
        code += "errorFunction = function() {\n";
        code += "   tab = [11, 22, 33];\n";
        code += "   return tab[3];\n";
        code += "};\n";
        code += "// comment\n";
        code += "main = function() { errorFunction(); };\n";
        code += "main();\n";

        Program program = Program.make(code);
        program.run();

        Assert.assertEquals(ProgramState.Error, program.getState());
        FatalErrorException fatalError = program.getError();
        Assert.assertNotNull(fatalError);

        ArrayList<Trace> calls = fatalError.getCalls();
        Assert.assertNotNull(calls);
        Assert.assertEquals(2, calls.size());

        Assert.assertEquals("errorFunction", calls.get(0).getFunctionName());
        Assert.assertEquals(7, calls.get(0).getLine());
        Assert.assertEquals("main", calls.get(1).getFunctionName());
        Assert.assertEquals(8, calls.get(1).getLine());
    }

    @Test
    public void elseTest() throws Exception {
        String code = "";
        code += "negative = 0;\n";
        code += "positive = 0;\n";
        code += "tab = [12, 32, -46, 52, 82, -22, 94, 28, -19];\n";
        code += "for (key, value in tab) {\n";
        code += "    if (value < 0) {\n";
        code += "        negative += 1;\n";
        code += "    }\n";
        code += "    else {\n";
        code += "        positive += 1;\n";
        code += "    }\n";
        code += "}\n";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data negativeData = context.getDictStack().get("negative");
        Data positiveData = context.getDictStack().get("positive");
        Assert.assertEquals(new IntegerData(6), positiveData);
        Assert.assertEquals(new IntegerData(3), negativeData);
    }

    @Test
    public void elifTest() throws Exception {
        String code = "";
        code += "light = 0;\n";
        code += "medium = 0;\n";
        code += "heavy = 0;\n";
        code += "tab = [12, 32, 46, 52, 82, 22, 94, 28, 19];\n";
        code += "for (key, value in tab) {\n";
        code += "    if (value < 30) {\n";
        code += "        light += 1;\n";
        code += "    }\n";
        code += "    elif (value < 60) {\n";
        code += "        medium += 1;\n";
        code += "    }\n";
        code += "    else {\n";
        code += "        heavy += 1;\n";
        code += "    }\n";
        code += "}\n";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Data lightData = context.getDictStack().get("light");
        Data mediumData = context.getDictStack().get("medium");
        Data heavyData = context.getDictStack().get("heavy");
        Assert.assertEquals(new IntegerData(4), lightData);
        Assert.assertEquals(new IntegerData(3), mediumData);
        Assert.assertEquals(new IntegerData(2), heavyData);
    }

    @Test
    public void isEqualToTest() throws Exception {
        // isEqualTo is not value based for immutables

        String code = "";

        code += "str1 = 'Hello world';";
        code += "str2 = 'Hello world';";
        code += "res0 = (str1 == str2);";

        code += "tab1 = { a:11, b:22 };";
        code += "tab2 = { a:11, b:22 };";
        code += "tab3 = tab1;";
        code += "res1 = (tab1 == tab2);";
        code += "res2 = (tab1 == tab3);";

        Program program = Program.make(code);
        Context context = program.getContext();
        program.run();

        Assert.assertEquals(new BooleanData(true), context.getDictStack().get("res0"));
        Assert.assertEquals(new BooleanData(false), context.getDictStack().get("res1"));
        Assert.assertEquals(new BooleanData(true), context.getDictStack().get("res2"));
    }
}
