import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.StringAtom;
import fr.ambox.assembler.datas.*;
import fr.ambox.lexer.TokenPosition;
import fr.ambox.program.Program;
import fr.ambox.program.ProgramState;
import fr.ambox.program.serialization.adapters.ContextAdapter;
import fr.ambox.program.serialization.adapters.ProgramAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;

public class SerializationTest {

    private Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Context.class, new ContextAdapter());
        return gsonBuilder.create();
    }

    @Test
    public void defaultContextTest() throws Exception {
        Gson gson = this.buildGson();

        Context context = new Context();

        String serialized1 = gson.toJson(context, Context.class);
        Context deserialized = gson.fromJson(serialized1, Context.class);
        String serialized2 = gson.toJson(deserialized, Context.class);

        Assert.assertEquals(serialized1, serialized2);
    }

    @Test
    public void callStackTest() throws Exception {
        Gson gson = this.buildGson();

        Context context = new Context();
        context.setDictStack(new DictStack());

        context.getCallStack().push(new Trace(new TokenPosition(11,22), new IntegerData(33), new StringData("foo")));
        context.getCallStack().push(new Trace(new TokenPosition(44,55), new IntegerData(66), new StringData("bar")));

        String serialized1 = gson.toJson(context, Context.class);
        Context deserialized = gson.fromJson(serialized1, Context.class);
        String serialized2 = gson.toJson(deserialized, Context.class);

        Assert.assertEquals(serialized1, serialized2);

        Trace secondTrace1 = context.getCallStack().peek();
        Trace secondTrace2 = deserialized.getCallStack().peek();
        Assert.assertEquals(secondTrace1.getFunctionName(), secondTrace2.getFunctionName());
        Assert.assertEquals(secondTrace1.getLine(), secondTrace2.getLine());
        Assert.assertEquals(secondTrace1.getTokenPosition().getLine(), secondTrace2.getTokenPosition().getLine());
        Assert.assertEquals(secondTrace1.getTokenPosition().getLineIndex(), secondTrace2.getTokenPosition().getLineIndex());
    }

    @Test
    public void arrayDataTest() throws Exception {
        Gson gson = this.buildGson();

        Context context = new Context();
        context.setDictStack(new DictStack());

        ArrayData arrayData = new ArrayData();
        arrayData.append(new StringData("alice"));
        arrayData.append(new StringData("bob"));
        arrayData.append(new StringData("cecile"));

        context.getStack().push(arrayData);

        String serialized1 = gson.toJson(context, Context.class);
        Context deserialized = gson.fromJson(serialized1, Context.class);
        String serialized2 = gson.toJson(deserialized, Context.class);

        Assert.assertEquals(serialized1, serialized2);

        Data data = deserialized.getStack().pop();
        Assert.assertTrue(data instanceof ArrayData);
        ArrayData deserializedArray = (ArrayData) data;

        Assert.assertEquals(arrayData.get(0), deserializedArray.get(0));
        Assert.assertEquals(arrayData.get(1), deserializedArray.get(1));
        Assert.assertEquals(arrayData.get(2), deserializedArray.get(2));
    }

    @Test
    public void tableDataTest() throws Exception {
        Gson gson = this.buildGson();

        Context context = new Context();
        context.setDictStack(new DictStack());

        TableData tableData = new TableData();
        tableData.put("true", new BooleanData(true));
        tableData.put("false", new BooleanData(false));
        tableData.put("pi", new DecimalData(3.14));
        tableData.put("name", new NameData("love"));
        tableData.put("null", new NullData());
        tableData.put("state", new StateData(ProcedureState.WhileInit));

        context.getStack().push(tableData);

        String serialized1 = gson.toJson(context, Context.class);
        Context deserialized = gson.fromJson(serialized1, Context.class);
        String serialized2 = gson.toJson(deserialized, Context.class);

        Assert.assertEquals(serialized1, serialized2);

        Data data = deserialized.getStack().pop();
        Assert.assertTrue(data instanceof TableData);
        TableData deserializedTable = (TableData) data;

        Assert.assertEquals(tableData.get("true"), deserializedTable.get("true"));
        Assert.assertEquals(tableData.get("false"), deserializedTable.get("false"));
        Assert.assertEquals(tableData.get("char"), deserializedTable.get("char"));
        Assert.assertEquals(tableData.get("pi"), deserializedTable.get("pi"));
        Assert.assertEquals(tableData.get("name"), deserializedTable.get("name"));
        Assert.assertEquals(tableData.get("null"), deserializedTable.get("null"));
        Assert.assertEquals(tableData.get("state"), deserializedTable.get("state"));
    }

    @Test
    public void nestedTableDataTest() throws Exception {
        Gson gson = this.buildGson();

        Context context = new Context();
        context.setDictStack(new DictStack());

        TableData t1 = new TableData();
        context.getDictStack().put("t1", t1);
        TableData t2 = new TableData();
        t1.put("t2", t2);
        TableData t3 = new TableData();
        t2.put("t3", t3);
        t3.put("hello", new StringData("Hello World"));
        t3.put("t1", t1);

        String serialized1 = gson.toJson(context, Context.class);
        Context deserialized = gson.fromJson(serialized1, Context.class);
        String serialized2 = gson.toJson(deserialized, Context.class);

        Assert.assertEquals(serialized1, serialized2);

        Data t1Data = deserialized.getDictStack().get("t1");
        Assert.assertTrue(t1Data instanceof TableData);
        TableData dt1 = (TableData) t1Data;
        Assert.assertTrue(dt1.has("t2"));
        Data t2Data = dt1.get("t2");
        Assert.assertTrue(t2Data instanceof TableData);
        TableData dt2 = (TableData) t2Data;
        Assert.assertTrue(dt2.has("t3"));
        Data t3Data = dt2.get("t3");
        Assert.assertTrue(t3Data instanceof TableData);
        TableData dt3 = (TableData) t3Data;
        Assert.assertEquals(dt3.get("hello"), new StringData("Hello World"));
    }

    @Test
    public void functionDataTest() throws Exception {
        Gson gson = this.buildGson();

        Context context = new Context();
        context.setDictStack(new DictStack());

        ArrayData args = new ArrayData();
        args.append(new NameData("a"));
        args.append(new NameData("b"));

        AtomList atoms = new AtomList();
        atoms.add(new StringAtom("'test1'"));
        atoms.add(new CallAtom("print"));
        BlockData code = new BlockData(atoms);

        TableData locals = new TableData();
        locals.put("foo", new StringData("bar"));

        FunctionData function = new FunctionData(args, code, locals);
        context.getDictStack().put("test", function);

        String name = "test";
        function.setName(name);

        String serialized1 = gson.toJson(context, Context.class);
        Context deserialized = gson.fromJson(serialized1, Context.class);
        String serialized2 = gson.toJson(deserialized, Context.class);

        Assert.assertEquals(serialized1, serialized2);

        Data data = deserialized.getDictStack().get("test");
        Assert.assertTrue(data instanceof FunctionData);
        FunctionData deserializedFunction = (FunctionData) data;

        Assert.assertEquals(function.getName(), deserializedFunction.getName());
        Assert.assertTrue(function.isLike(deserializedFunction));
    }

    @Test
    public void programTest() throws Exception {
        String code = "";
        code += "a = 11;\n";
        code += "b = 22;\n";
        code += "c = (a + b);\n";
        code += "if (c == 33) {\n";
        code += "   exit();\n";
        code += "}\n";
        code += "print('end');\n";

        Program program = Program.make(code);
        program.run();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Program.class, new ProgramAdapter());
        Gson gson = gsonBuilder.create();

        String serialized1 = gson.toJson(program);
        Program deserialized = gson.fromJson(serialized1, Program.class);
        String serialized2 = gson.toJson(deserialized);

        Assert.assertEquals(serialized1, serialized2);
    }

    @Test
    public void metadataTest() throws Exception {
        String code = "print('Hello World');";

        Program program = Program.make(code);
        program.getContext().getMeta().jobId = 42;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Program.class, new ProgramAdapter());
        Gson gson = gsonBuilder.create();

        String serialized1 = gson.toJson(program);
        Program deserialized = gson.fromJson(serialized1, Program.class);

        MetaData expected = program.getContext().getMeta();
        MetaData actual = deserialized.getContext().getMeta();
        Assert.assertEquals(expected.jobId, actual.jobId);
    }

    @Test
    public void referenceAndSerializationTest() throws Exception {
        TableData tab2;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Program.class, new ProgramAdapter());
        Gson gson = gsonBuilder.create();

        String code = "";
        // two separate but equals tables
        code += "tab1 = { a:11, b:22 };";
        code += "tab2 = { a:11, b:22 };";
        code += "exit();";
        code += "tab1.a = 22;";

        // without serialization
        Program program1 = Program.make(code);
        Context context1 = program1.getContext();

        program1.run();
        program1.run();

        tab2 = (TableData) context1.getDictStack().get("tab2");
        Assert.assertEquals(new IntegerData(11), tab2.get("a"));

        // with serialization
        Program program2 = Program.make(code);
        Context context2 = program2.getContext();

        program2.run();
        String serialized = gson.toJson(program2, Program.class);
        program2 = gson.fromJson(serialized, Program.class);
        program2.run();

        tab2 = (TableData) context2.getDictStack().get("tab2");
        Assert.assertEquals(new IntegerData(11), tab2.get("a"));
    }

    @Test
    public void savedStackTest() throws Exception {
        String code = "print('Hello World');";

        Program program = Program.make(code);

        Context context = program.getContext();
        context.setDictStack(new DictStack());

        Data[] empty = new Data[0];
        ArrayDeque<Data[]> stack = new ArrayDeque<Data[]>();
        stack.push(empty);
        context.setSavedStacks(stack);
        Assert.assertFalse(context.getSavedStacks().isEmpty());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Program.class, new ProgramAdapter());
        Gson gson = gsonBuilder.create();

        String serialized = gson.toJson(program, Program.class);
        program = gson.fromJson(serialized, Program.class);
        context = program.getContext();
        Assert.assertFalse(context.getSavedStacks().isEmpty());
    }
}
