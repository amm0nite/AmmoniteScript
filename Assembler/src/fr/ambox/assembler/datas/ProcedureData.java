package fr.ambox.assembler.datas;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.exceptions.FatalErrorException;

public final class ProcedureData extends Data {

	private final Procedure procedure;
	
	public ProcedureData(Procedure procedure) {
        this.procedure = procedure;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { 
			return false; 
		}
		if (obj == this) { 
			return true; 
		}
		if (!(obj instanceof ProcedureData)) {
			return false;
		}
		ProcedureData other = (ProcedureData) obj;
		String className = this.procedure.getClass().getCanonicalName();
		String otherClassName = other.procedure.getClass().getCanonicalName();
		return className.equals(otherClassName);
	}

	@Override
	public int hashCode() {
		return this.procedure.getClass().getCanonicalName().hashCode();
	}

    @Override
    public boolean isEqualTo(Data data) {
        return this.equals(data);
    }

	@Override
	public boolean isLike(Data data) {
		return this.equals(data);
	}

    public boolean execute(Context context) throws FatalErrorException {
        return this.procedure.execute(context);
	}

	public Procedure getProcedure() {
		return this.procedure;
	}
}
