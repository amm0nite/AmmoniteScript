package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;

import java.io.PrintStream;

public class TraceProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		Data d = context.getStack().pop();
        PrintStream out = new PrintStream(context.getStdout());
		out.println(d.getClass().getSimpleName() + " " + d.toString());
		return true;
	}

}
