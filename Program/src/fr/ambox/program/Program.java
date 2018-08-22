package fr.ambox.program;

import fr.ambox.assembler.Assembler;
import fr.ambox.assembler.Context;
import fr.ambox.assembler.SkipState;
import fr.ambox.assembler.exceptions.*;
import fr.ambox.compiler.Assembly;
import fr.ambox.compiler.Compiler;
import fr.ambox.compiler.exceptions.MultipleParseTreeException;
import fr.ambox.compiler.exceptions.SyntaxErrorException;
import fr.ambox.lexer.NoTokenException;

public class Program {

    private Context context;
    private ProgramState state;
    private InterruptionException interruption;

    public static Program make(String code) throws SyntaxErrorException, NoTokenException, MultipleParseTreeException, TokenParsingException {
        Compiler compiler = new Compiler();
        Assembly assembly = compiler.compile(code);

        Context context = new Context();
        context.init(assembly.getCode());

        return new Program(context, ProgramState.Init);
    }

    public Program(Context context, ProgramState state)  {
        this.context = context;
        this.state = state;
        this.interruption = null;
    }

    public void run() {
        if (this.state == ProgramState.Paused) {
            this.state = ProgramState.Init;
        }

        if (this.state == ProgramState.Error || this.state == ProgramState.Done) {
            throw new IllegalStateException();
        }

        try {
            Assembler assembler = new Assembler(this.context);
            assembler.execute();
        }
        catch (InterruptionException e) {
            this.interruption = e;
            this.state = ProgramState.Paused;
            this.context.reboot();
            return;
        }

        this.state = ProgramState.Done;
        if (this.context.getSkipState() == SkipState.Error) {
            this.state = ProgramState.Error;
        }
    }

    public Context getContext() {
        return this.context;
    }

    public ProgramState getState() {
        return this.state;
    }

    public FatalErrorException getError() {
        return this.context.getError();
    }

    public InterruptionException getInterruption() {
        return this.interruption;
    }
}
