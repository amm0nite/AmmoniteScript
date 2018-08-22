package fr.ambox.assembler.datas;

import fr.ambox.assembler.Data;
import fr.ambox.assembler.ProcedureState;

public final class StateData extends Data {

    private final ProcedureState state;

    public StateData(ProcedureState state) {
        this.state = state;
    }

    public ProcedureState getState() {
        return this.state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StateData)) {
            return false;
        }
        StateData other = (StateData) obj;
        return this.state.equals(other.state);
    }

    @Override
    public int hashCode() {
        return this.state.hashCode();
    }

    @Override
    public boolean isEqualTo(Data data) {
        return this.equals(data);
    }

    @Override
    public boolean isLike(Data data) {
        return this.equals(data);
    }
}
