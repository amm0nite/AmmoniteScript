package fr.ambox.assembler.procedures;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.*;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.datas.StateData;
import fr.ambox.assembler.exceptions.ExpectedBlockDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;
import fr.ambox.assembler.exceptions.UnexpectedProcedureStateException;

public class WhileProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
        Data sd = context.getStack().pop();

        StateData stateData;
        if (sd instanceof StateData) {
            stateData = (StateData) sd;
        }
        else {
            context.getStack().push(sd);
            stateData = new StateData(ProcedureState.WhileInit);
        }

        Data d2 = context.getStack().pop();
		Data d1 = context.getStack().pop();

		if (!(d1 instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		
		if (!(d2 instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		
		BlockData condition = (BlockData) d1;
		BlockData action = (BlockData) d2;
        int conditionId = context.getStore().add(d1);
        int actionId = context.getStore().add(d2);

        AtomList code = new AtomList();

        if (stateData.getState() == ProcedureState.WhileInit) {
            code.add(new ReferenceAtom("@" + conditionId));
            code.add(new ReferenceAtom("@" + actionId));
            code.add(new InformationAtom("#" + ProcedureState.WhileCheck));
            code.add(new CallAtom("while"));

            code.add(new CallAtom("unbreak"));

            code.add(new IntegerAtom(String.valueOf(conditionId)));
            code.add(new CallAtom("free"));
            code.add(new IntegerAtom(String.valueOf(actionId)));
            code.add(new CallAtom("free"));

            context.getBootstrap().push(new Step(code));
            return true;
        }

        if (stateData.getState() == ProcedureState.WhileCheck) {
            code.append(condition.assemble(context));

            code.add(new CallAtom("blstart"));
            code.add(new ReferenceAtom("@" + conditionId));
            code.add(new ReferenceAtom("@" + actionId));
            code.add(new InformationAtom("#" + ProcedureState.WhileContinue.name()));
            code.add(new CallAtom("blstop"));

            code.add(new CallAtom("blstart"));
            code.add(new ReferenceAtom("@" + conditionId));
            code.add(new ReferenceAtom("@" + actionId));
            code.add(new InformationAtom("#" + ProcedureState.WhileStop.name()));
            code.add(new CallAtom("blstop"));

            code.add(new CallAtom("ifelse"));

            context.getBootstrap().push(new Step(code));
            return false;
        }

        if (stateData.getState() == ProcedureState.WhileContinue) {
            code.append(action.assemble(context));
            code.add(new CallAtom("uncontinue"));
            code.add(new ReferenceAtom("@" + conditionId));
            code.add(new ReferenceAtom("@" + actionId));
            code.add(new InformationAtom("#" + ProcedureState.WhileCheck));

            context.getBootstrap().push(new Step(code));
            return false;
        }

        if (stateData.getState() == ProcedureState.WhileStop) {
            return true;
        }

        throw new UnexpectedProcedureStateException();
	}

}
