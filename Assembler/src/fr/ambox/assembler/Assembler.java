package fr.ambox.assembler;

import fr.ambox.assembler.exceptions.*;
import fr.ambox.lexer.*;

import java.util.ArrayDeque;

public class Assembler {
    private Context context;

    public Assembler(Context context) {
        this.context = context;
    }

    public void execute() throws InterruptionException {
        ArrayDeque<Step> bootstrap = this.context.getBootstrap();
        Step step = bootstrap.pop();
        this.context.getStepStack().push(step);

        AtomList atoms = step.getAtoms();
        int ip = step.getOffset();

        // resume execution
        //System.out.println("s> " + ip + "> " + atoms);
        while (ip < atoms.size()) {
            // go deeper
            boolean flag = false;
            while (!bootstrap.isEmpty()) {
                this.execute();
                flag = true;
            }
            //if (flag) { System.out.println("r> " + ip + "> " + atoms); }

            Atom atom = atoms.get(ip);
            boolean next = true;

            if (this.context.getBlockFlag() && !atom.isBlockBoundary()) {
                this.context.getBlockBuffer().add(atom);
            }
            else if (!context.shouldSkip(atom)) {
                next = this.execute(atom);
            }

            if (next) {
                ip++;
                step.setOffset(ip);
            }

            if (context.getStopFlag()) {
                throw new StopException();
            }
        }

        this.context.getStepStack().pop();
    }

    private boolean execute(Atom atom) throws InterruptionException {
        try {
            this.context.checkLimits();
            boolean next = atom.execute(this.context);
            this.context.incrementAtomCounter();
            return next;
        }
        catch (Exception exception) {
            if (exception instanceof InterruptionException) {
                throw (InterruptionException) exception;
            }

            FatalErrorException fatalError = null;
            if (exception instanceof FatalErrorException) {
                fatalError = (FatalErrorException) exception;
            }
            else {
                fatalError = new WrapException(exception);
            }
            fatalError.setAtom(atom);
            TokenPosition position = atom.getTokenPosition();
            Data lineData = this.context.getDictStack().get("_line");
            Data callSignData = this.context.getDictStack().get("_function");
            fatalError.setOrigin(new Trace(position, lineData, callSignData));
            fatalError.setSteps(context.getStepStack());
            fatalError.setCalls(context.getCallStack());
            this.context.setSkipState(SkipState.Error);
            this.context.setError(fatalError);
            return true;
        }
    }

    public Context getContext() {
        return this.context;
    }
}
