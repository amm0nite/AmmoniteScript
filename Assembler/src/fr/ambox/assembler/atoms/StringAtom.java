package fr.ambox.assembler.atoms;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Atom;
import fr.ambox.assembler.datas.StringData;

public class StringAtom extends Atom {

	private String value;
	
	public StringAtom(String value) {
		this.value = StringAtom.cleanString(value);
	}

	public static String cleanString(String input) {
        int index = 0;
        int position = 0;
        char delimiter = input.charAt(0);

        String str = input.substring(1, input.length() - 1);
        StringBuilder output = new StringBuilder();

        while ((position = str.indexOf(delimiter, index)) != -1) {
            output.append(str.substring(index, position - 1));
            output.append(delimiter);
            index = position + 1;
        }
        output.append(str.substring(index, str.length()));

        return output.toString();
    }

    public static String prepareString(String input) {
        return "'" + input.replaceAll("'", "\\'") + "'";
    }

	@Override
	public boolean execute(Context context) {
		context.getStack().push(new StringData(this.value));
        return true;
	}

    @Override
    public String toTokenString() {
        return StringAtom.prepareString(this.value);
    }

	@Override
	public boolean isBlockBoundary() {
		return false;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StringAtom)) {
            return false;
        }
        StringAtom other = (StringAtom) obj;
        return this.value.equals(other.value);
    }
}
