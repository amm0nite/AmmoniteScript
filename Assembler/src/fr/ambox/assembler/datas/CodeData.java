package fr.ambox.assembler.datas;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;

public interface CodeData {
    AtomList assemble(Context context);
}
