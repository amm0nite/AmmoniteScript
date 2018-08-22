package fr.ambox.assembler.procedures;

import fr.ambox.assembler.ComparisonOperation;
import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.BooleanData;
import fr.ambox.assembler.datas.DecimalData;
import fr.ambox.assembler.datas.DoubleData;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.exceptions.ArithmeticTypeException;

public abstract class ComparisonProcedure implements Procedure {

	private ComparisonOperation op;
	
	public ComparisonProcedure(ComparisonOperation op) {
		this.op = op;
	}
	
	@Override
	public boolean execute(Context context) throws ArithmeticTypeException {
		Data d2 = context.getStack().pop();
		Data d1 = context.getStack().pop();
		
		if (!(d1 instanceof DecimalData) && !(d1 instanceof IntegerData)) {
			throw new ArithmeticTypeException(d1.getClass().getName(), d2.getClass().getName());
		}
		if (!(d2 instanceof DecimalData) && !(d2 instanceof IntegerData)) {
			throw new ArithmeticTypeException(d1.getClass().getName(), d2.getClass().getName());
		}
		
		double a = ((DoubleData) d1).doubleValue();
		double b = ((DoubleData) d2).doubleValue();
		boolean result = this.getResult(a, b);
		context.getStack().push(new BooleanData(result));

		return true;
	}

	private boolean getResult(double a, double b) {
		if (this.op == ComparisonOperation.Greater) {
			return a > b;
		}
		if (this.op == ComparisonOperation.GreaterEquals) {
			return a >= b;
		}
		if (this.op == ComparisonOperation.Lesser) {
			return a < b;
		}
		if (this.op == ComparisonOperation.LesserEquals) {
			return a <= b;
		}
		return false;
	}
	
}
