
import fr.ambox.assembler.*;
import fr.ambox.assembler.datas.*;
import fr.ambox.assembler.exceptions.FatalErrorException;
import fr.ambox.lexer.TokenPosition;
import org.junit.Assert;
import org.junit.Test;

public class ExecutionTest {

    private void expectData(String code, Data expectedStackTop, boolean allowError) throws Exception {
        try {
            Context context = new Context();
            context.init(code);

            Assembler assembler = new Assembler(context);
            assembler.execute();

            if (!allowError) {
                Assert.assertNull(context.getError());
            }

            Data data = assembler.getContext().getStack().pop();
            Assert.assertTrue(expectedStackTop.isLike(data));
        }
        catch (Exception e) {
            System.out.println("=== Exception ===");
            System.out.println(e.getMessage());
            System.out.println("=================");
            Assert.fail();
        }
    }

    private void expectData(String code, Data expectedStackTop) throws Exception {
        this.expectData(code, expectedStackTop, false);
    }

    private void expectException(String code, String exceptionName) throws Exception {
        Context context = new Context();
        context.init(code);

        Assembler assembler = new Assembler(context);
        assembler.execute();

        Assert.assertNotNull(context.getError());
        FatalErrorException fatalError = context.getError();
        Assert.assertTrue(fatalError.getClass().getSimpleName().equals(exceptionName));
    }

    private ArrayData simpleArray() {
        ArrayData a = new ArrayData();
        a.append(new IntegerData(1));
        a.append(new IntegerData(2));
        a.append(new IntegerData(3));
        a.append(new IntegerData(4));
        return a;
    }

    private ArrayData nestedArray() {
        ArrayData a0 = new ArrayData();
        ArrayData a1 = new ArrayData();
        a1.append(new IntegerData(1));
        a1.append(new IntegerData(2));
        a0.append(a1);
        ArrayData a2 = new ArrayData();
        a2.append(new IntegerData(3));
        a2.append(new IntegerData(4));
        a0.append(a2);
        return a0;
    }

    private TableData dictTable() {
        TableData t1 = new TableData();
        t1.safePut("a", new IntegerData(5));
        t1.safePut("b", new IntegerData(10));
        return t1;
    }

    @Test
    public void arithmeticTest() throws Exception {
        this.expectData("117 17 /", new IntegerData(6));
        this.expectData("117 17 %", new IntegerData(15));
        this.expectData("10 5 * 100 10 / + 5 -", new IntegerData(55));
    }

    @Test
    public void tableTest() throws Exception {
        this.expectData("table", new TableData());
    }

    @Test
    public void appendTest() throws Exception {
        this.expectData("array 1 append 2 append 3 append 4 append", this.simpleArray());
        this.expectData("array array 1 append 2 append append array 3 append 4 append append", this.nestedArray());
    }

    @Test
    public void stackTest() throws Exception {
        this.expectData("4 3 2 1 stack", this.simpleArray());
    }

    @Test
    public void getTest() throws Exception {
        this.expectData("array 1 append 2 append 3 append 4 append 1 get", new IntegerData(2));
        this.expectData("array 1.1 append 2.2 append 3.3 append 4.4 append 2 get", new DecimalData(3.3));
    }

    @Test
    public void definitionTest() throws Exception {
        this.expectData(":plop 11 def $plop", new IntegerData(11));
        this.expectData(":code { 'plop' } def code", new StringData("plop"));
    }

    @Test
    public void stringTest() throws Exception {
        this.expectData("'mip mop'", new StringData("mip mop"));
        this.expectData("'L\\'arbre est tombé'", new StringData("L'arbre est tombé"));
    }

    @Test
    public void concatTest() throws Exception {
        this.expectData("'Hello' 'world' concat", new StringData("Helloworld"));
    }

    @Test
    public void comparisonTest() throws Exception {
        this.expectData("11 22 ==", new BooleanData(false));
        this.expectData("2 2.0 ==", new BooleanData(true));
        this.expectData("false false ==", new BooleanData(true));
        this.expectData("24 18 >", new BooleanData(true));
    }

    @Test
    public void conditionTest() throws Exception {
        this.expectData("24 18 > { 'majeur' } if", new StringData("majeur"));
        this.expectData("16 18 > { 'majeur' } { 'mineur' } ifelse", new StringData("mineur"));
    }

    @Test
    public void loopTest() throws Exception {
        this.expectData("10 { 1 - dup 0 eq { break } if } loop", new IntegerData(0));
        this.expectData("'' 4 { 'a' concat } repeat", new StringData("aaaa"));
        this.expectData("'' 5 range { pop pop 'a' concat } for", new StringData("aaaaa"));
        this.expectData("array 'a' append 'b' append 'c' append { 'a' concat } for", new StringData("ca"));
        this.expectData("'' table 'a' 5 set 'b' 10 set { pop concat } for", new StringData("ab"));
        this.expectData(":myvalue array 'a' append 'b' append 'c' append { :mylast $myvalue def } forv $mylast", new StringData("c"));
        this.expectData(":mykey :myvalue array 'a' append 'b' append 'c' append { :mylast $mykey $myvalue concat def } forkv $mylast", new StringData("2c"));
    }

    @Test
    public void lengthTest() throws Exception {
        this.expectData("array 1.1 append 2.2 append 3.3 append 4.4 append length", new IntegerData(4));
        this.expectData("array 1 append 2 append 3 append length", new IntegerData(3));
        this.expectData("'pouet' length", new IntegerData(5));
    }

    @Test
    public void dictTest() throws Exception {
        this.expectData("table 'a' 5 set 'b' 10 set", this.dictTable());

        this.expectData("table 'a' 5 set 'b' 10 set keys length", new IntegerData(2));
        this.expectData("table 'a' 5 set 'b' 10 set 'c' 15 set keys 1 get", new StringData("b"));

        this.expectData("table 'a' 5 set 'b' 10 set 'c' 15 set 'a' get", new IntegerData(5));
        this.expectData("table 'a' 5 set 'b' 10 set 'c' 15 set 'd' get", new NullData());
    }

    @Test
    public void isnullTest() throws Exception {
        this.expectData("null isnull", new BooleanData(true));
        this.expectData("0 isnull", new BooleanData(false));
    }

    @Test
    public void countTest() throws Exception {
        this.expectData("'a' 'b' 'c' count", new IntegerData(3));
    }

    @Test
    public void maxTest() throws Exception {
        this.expectData("11 22 2 copy < { exch } if pop", new IntegerData(22));
        this.expectData("22 11 2 copy < { exch } if pop", new IntegerData(22));
        this.expectData(":max { 2 copy < { exch } if pop } def 0 array 11 append 22 append 99 append 44 append 33 append { exch pop max } for", new IntegerData(99));
    }

    @Test
    public void baseScopeTest() throws Exception {
        // base global scope
        this.expectData(":var 11 def { :var 22 def } do $var", new IntegerData(22));
        this.expectData(":test { 11 } def :test { 22 } def test test eq", new BooleanData(true));
    }

    @Test
    public void advancedScopeTest() throws Exception {
        // javascript like scope

        // can read an higher defined value
        this.expectData("begin :a 22 def begin $a end end", new IntegerData(22));
        // can not read a lower defined value
        this.expectException("begin begin :a 22 def end $a end", "UndefinedException");
        // can overwrite an higher defined value
        this.expectData("begin :var 11 def begin :var 22 def end $var end", new IntegerData(22));
    }

    @Test
    public void dictstackTest() throws Exception {
        this.expectData(":table load do", new TableData());
        this.expectData(":table { array 1 append 2 append 3 append 4 append } def table", this.simpleArray());
    }

    @Test
    public void timeTest() throws Exception {
        this.expectData("time time sub 0 gt", new BooleanData(false));
    }

    @Test
    public void mathTest() throws Exception {
        this.expectData("-0.3 floor", new IntegerData(-1));
        this.expectData("-0.3 ceil", new IntegerData(0));
    }

    @Test
    public void notTest() throws Exception {
        this.expectData("false not", new BooleanData(true));
        this.expectData("true not", new BooleanData(false));
    }

    @Test
    public void blockTest() throws Exception {
        this.expectData("{ { { 'plop' } do } do } do", new StringData("plop"));
    }

    @Test
    public void stackManagementTest() throws Exception {
        this.expectData("0 11 save clear restore", new IntegerData(11));
        this.expectData("0 11 save clear 0 22 save clear restore", new IntegerData(22));
        this.expectData("0 11 save clear 0 22 save clear restore restore", new IntegerData(11));
    }

    @Test
    public void codeTest() throws Exception {
        this.expectData(":buffer '' def :i 0 def { $i 10 lt } { :buffer $buffer 'a' concat def :i $i 1 + def } while $buffer", new StringData("aaaaaaaaaa"));
    }

    @Test
    public void additionTest() throws Exception {
        this.expectData("11 '22' +", new StringData("1122"));
        this.expectData("'11' '22' +", new StringData("1122"));
        this.expectData("'11.0' 22 +", new StringData("11.022"));
        this.expectData("11 '22' int +", new IntegerData(33));
        this.expectData("11 '22.5' float +", new DecimalData(33.5));
    }

    @Test
    public void arrayMethodTest() throws Exception {
        this.expectData("array 'append' get 22 exch do 0 get", new IntegerData(22));
    }

    @Test
    public void trycatchTest() throws Exception {
        TableData error = new TableData();
        error.put("type", new StringData("fr.ambox.assembler.exceptions.WrapException"));
        error.put("message", new StringData("java.util.NoSuchElementException"));
        this.expectData("{ pop } :e { $e } tryc", error, true);
        this.expectData("{ pop } :e { $e } { 22 } trycf", new IntegerData(22), true);
        this.expectData("{ pop } { 22 } tryf", new IntegerData(22), true);
    }

    @Test
    public void functionTest() throws Exception {
        this.expectData("table 'args' array :n append set 'code' { $n 1 + } set fcreate 1121 exch do", new IntegerData(1122));
    }

    @Test
    public void fatalErrorTest() throws Exception {
        String code = "array 1 append 2 append 3 append 3 get";

        Context context = new Context();
        context.init(code);

        Assembler assembler = new Assembler(context);
        assembler.execute();

        Assert.assertNotNull(context.getError());
        FatalErrorException fatalError = context.getError();
        Trace trace = fatalError.getOrigin();
        Assert.assertNotNull(trace);
        Assert.assertEquals(new TokenPosition(1, 36), trace.getTokenPosition());
    }

    @Test
    public void continueTest() throws Exception {
        String code = ":test 0 def :stop false def { $stop not } { :stop true def continue :test 1 def } while";

        Context context = new Context();
        context.init(code);

        Assembler assembler = new Assembler(context);
        assembler.execute();

        Assert.assertNull(context.getError());
        Data testData = context.getDictStack().get("test");
        Assert.assertEquals(new IntegerData(0), testData);
    }
}
