package fr.ambox.assembler.exceptions;

import fr.ambox.assembler.Atom;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Step;
import fr.ambox.assembler.Trace;
import fr.ambox.assembler.datas.StringData;
import fr.ambox.assembler.datas.TableData;

import java.util.ArrayDeque;
import java.util.ArrayList;

abstract public class FatalErrorException extends Exception {

    private Atom atom;
    private Trace origin;
    private ArrayList<Step> steps;
    private ArrayList<Trace> calls;

    public Data toData() {
        TableData exceptionTable = new TableData();
        exceptionTable.safePut("type", new StringData("" + this.getClass().getName()));
        exceptionTable.safePut("message", new StringData(this.getMessage()));
        return exceptionTable;
    }

    public void setSteps(ArrayDeque<Step> stepsStack) {
        if (this.steps == null) {
            this.steps = new ArrayList<Step>();
            for (Step step: stepsStack) {
                this.steps.add(step);
            }
        }
    }
    public void setCalls(ArrayDeque<Trace> callStack) {
        if (this.calls == null) {
            this.calls = new ArrayList<Trace>();
            for (Trace call: callStack) {
                this.calls.add(call);
            }
        }
    }

    public ArrayList<Step> getSteps() {
        return this.steps;
    }
    public ArrayList<Trace> getCalls() {
        return this.calls;
    }

    public void setAtom(Atom atom) {
        if (this.atom == null) {
            this.atom = atom;
        }
    }
    public void setOrigin(Trace trace) {
        if (this.origin == null) {
            this.origin = trace;
        }
    }

    public Atom getAtom() {
        return this.atom;
    }
    public Trace getOrigin() {
        return this.origin;
    }
}
