
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.datas.BooleanData;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.datas.TableData;
import fr.ambox.assembler.exceptions.BytesWrittenLimitException;
import fr.ambox.assembler.exceptions.MemsizeLimitFatalException;
import fr.ambox.program.Program;
import fr.ambox.program.ProgramState;
import fr.ambox.program.serialization.adapters.ProgramAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class InterruptionTest {

    @Test
    public void interruptTest() throws Exception {
        String code = "";
        code += "z = 1;\n";
        code += "i = 1;\n";
        code += "while (i < 9999) {\n";
        code += "   z = (z + (1.0 / i));\n";
        code += "   i += 1;\n";
        code += "}\n";

        Program program = Program.make(code);
        program.run();

        Data data = program.getContext().getDictStack().get("i");
        System.out.println(data);

        Assert.assertEquals(ProgramState.Paused, program.getState());
    }

    private String infiniteLoop() {
        String code = "";
        code += "i = 1;\n";
        code += "while (true) {\n";
        code += "   i += 1;\n";
        code += "}\n";
        return code;
    }

    @Test
    public void interruptAndResumeTest() throws Exception {
        Program program = Program.make(this.infiniteLoop());
        Context context = program.getContext();

        program.run();

        Assert.assertEquals(program.getState(), ProgramState.Paused);
        Data firstCounterData = context.getDictStack().get("i");
        Assert.assertTrue(firstCounterData instanceof IntegerData);
        IntegerData firstCounter = (IntegerData) firstCounterData;
        long firstValue = firstCounter.longValue();

        program.run();

        Assert.assertEquals(program.getState(), ProgramState.Paused);
        Data secondCounterData = context.getDictStack().get("i");
        Assert.assertTrue(secondCounterData instanceof IntegerData);
        IntegerData secondCounter = (IntegerData) secondCounterData;
        long secondValue = secondCounter.longValue();

        Assert.assertTrue(secondValue > firstValue);
    }

    @Test
    public void interruptAndResumeWithSerializationTest() throws Exception {
        Program program1 = Program.make(this.infiniteLoop());

        program1.run();

        Assert.assertEquals(program1.getState(), ProgramState.Paused);
        Context context1 = program1.getContext();
        Data firstCounterData = context1.getDictStack().get("i");
        Assert.assertTrue(firstCounterData instanceof IntegerData);
        IntegerData firstCounter = (IntegerData) firstCounterData;
        long firstValue = firstCounter.longValue();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Program.class, new ProgramAdapter());
        Gson gson = gsonBuilder.create();

        String json = gson.toJson(program1, Program.class);
        Program program2 = gson.fromJson(json, Program.class);
        String jsonTest = gson.toJson(program2, Program.class);
        Assert.assertEquals(json, jsonTest);
        Assert.assertEquals(firstCounter, program2.getContext().getDictStack().get("i"));

        program2.run();

        Assert.assertEquals(program2.getState(), ProgramState.Paused);
        Context context2 = program2.getContext();
        Data secondCounterData = context2.getDictStack().get("i");
        Assert.assertTrue(secondCounterData instanceof IntegerData);
        IntegerData secondCounter = (IntegerData) secondCounterData;
        long secondValue = secondCounter.longValue();

        Assert.assertTrue(secondValue > firstValue);
    }

    @Test
    public void exitTest() throws Exception {
        String code = "print('start'); exit(); print('stop');";

        Program program = Program.make(code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        program.getContext().setStdout(out);

        program.run();
        Assert.assertEquals(ProgramState.Paused, program.getState());

        program.run();
        Assert.assertEquals(ProgramState.Done, program.getState());

        Assert.assertEquals("start\nstop\n", new String(out.toByteArray(), "UTF-8"));
    }

    @Test
    public void bytesWrittenTest() throws Exception {
        String code = "";
        code += "while (true) {";
        code += "   print('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa');";
        code += "}";

        Program program = Program.make(code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Context context = program.getContext();
        context.setStdout(out);

        program.run();
        Assert.assertEquals(ProgramState.Paused, program.getState());
        Assert.assertTrue(program.getInterruption() instanceof BytesWrittenLimitException);
        int firstSize = out.size();
        Assert.assertTrue(firstSize > context.getLimits().getBytesWrittenLimit());

        program.run();
        Assert.assertEquals(ProgramState.Paused, program.getState());
        Assert.assertTrue(program.getInterruption() instanceof BytesWrittenLimitException);
        int secondSize = out.size();
        Assert.assertTrue(secondSize > (2 * context.getLimits().getBytesWrittenLimit()));

        Assert.assertTrue(secondSize > firstSize);
    }

    @Test
    public void memsizeTest() throws Exception {
        String code = "";
        code += "i = 0;";
        code += "tab = {};";
        code += "while (true) {";
        code += "   key = ('test' + i);";
        code += "   tab[key] = time();";
        code += "   i += 1;";
        code += "}";

        Program program = Program.make(code);
        program.getContext().getLimits().setMemsizeLimit(1024);
        program.run();

        //Assert.assertEquals(ProgramState.Error, program.getState());
        //Assert.assertTrue(program.getError() instanceof MemsizeLimitFatalException);
    }
}