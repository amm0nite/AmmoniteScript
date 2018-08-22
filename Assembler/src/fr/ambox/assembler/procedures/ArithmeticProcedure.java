package fr.ambox.assembler.procedures;

import fr.ambox.assembler.ArithmeticOperation;
import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.*;
import fr.ambox.assembler.exceptions.ArithmeticTypeException;
import fr.ambox.assembler.exceptions.FatalErrorException;
import fr.ambox.assembler.exceptions.OperationNotSupportedException;

public abstract class ArithmeticProcedure implements Procedure {
	private ArithmeticOperation op;
	
	public ArithmeticProcedure(ArithmeticOperation op) {
		this.op = op;
	}
	
	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Data d2 = context.getStack().pop();
		Data d1 = context.getStack().pop();
		
		if (d1 instanceof IntegerData && d2 instanceof IntegerData) {
			long a = ((IntegerData) d1).longValue();
			long b = ((IntegerData) d2).longValue();
			Data result = new IntegerData(this.getResult(a, b));
			context.getStack().push(result);
		}
		else if (d1 instanceof DoubleData && d2 instanceof DoubleData) {
			double a = ((DoubleData) d1).doubleValue();
			double b = ((DoubleData) d2).doubleValue();
			Data result = new DecimalData(this.getResult(a, b));
			context.getStack().push(result);
		}
		else if (d1 instanceof TextData && d2 instanceof TextData) {
			String a = ((TextData) d1).stringValue();
			String b = ((TextData) d2).stringValue();
			Data result = new StringData(this.getResult(a, b));
			context.getStack().push(result);
		}
		else {
			throw new ArithmeticTypeException(d1.getClass().getName(), d2.getClass().getName());
		}

		return true;
	}
	
	private String getResult(String a, String b) throws OperationNotSupportedException {
		if (this.op == ArithmeticOperation.Moins) {
			throw new OperationNotSupportedException();
		}
		if (this.op == ArithmeticOperation.Mul) {
			throw new OperationNotSupportedException();
		}
		if (this.op == ArithmeticOperation.Div) {
			throw new OperationNotSupportedException();
		}
		if (this.op == ArithmeticOperation.Mod) {
			throw new OperationNotSupportedException();
		}
		return a + b;
	}

	private long getResult(long a, long b) {
		if (this.op == ArithmeticOperation.Moins) {
			return a - b;
		}
		if (this.op == ArithmeticOperation.Mul) {
			return a * b;
		}
		if (this.op == ArithmeticOperation.Div) {
			return a / b;
		}
		if (this.op == ArithmeticOperation.Mod) {
			return a % b;
		}
		return a + b;
	}
	
	private double getResult(double a, double b) {
		if (this.op == ArithmeticOperation.Moins) {
			return a - b;
		}
		if (this.op == ArithmeticOperation.Mul) {
			return a * b;
		}
		if (this.op == ArithmeticOperation.Div) {
			return a / b;
		}
		if (this.op == ArithmeticOperation.Mod) {
			return a % b;
		}
		return a + b;
	}
}
