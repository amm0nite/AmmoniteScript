package fr.ambox.compiler.parser;

import fr.ambox.compiler.parser.nonTerminals.*;
import java.util.HashMap;

public class AmGrammar implements Grammar {

	private HashMap<String, NonTerminal> nonTerminals;

    public AmGrammar() {
        this.nonTerminals = new HashMap<String, NonTerminal>();

		this.nonTerminals.put("start", new NtStart());
        this.nonTerminals.put("statementStar", new NtStatementStar());
		this.nonTerminals.put("block", new NtBlock());
        this.nonTerminals.put("expression", new NtExpression());
        this.nonTerminals.put("functionCallExpression", new NtFunctionCallExpression());
        this.nonTerminals.put("expressionCommaStar", new NtExpressionCommaStar());
        this.nonTerminals.put("callableExpression", new NtCallableExpression());
        this.nonTerminals.put("functionCreateExpression", new NtFunctionCreateExpression());
        this.nonTerminals.put("functionParameterStar", new NtFunctionParameterStar());
        this.nonTerminals.put("functionParameter", new NtFunctionParameter());
        this.nonTerminals.put("accessExpression", new NtAccessExpression());
        this.nonTerminals.put("accessStar", new NtAccessStar());
        this.nonTerminals.put("operationExpression", new NtOperationExpression());
        this.nonTerminals.put("assignment", new NtAssignment());
        this.nonTerminals.put("assignmentStandard", new NtAssignmentStandard());
        this.nonTerminals.put("assignmentCombinedOperator", new NtAssignmentCombinedOperator());
        this.nonTerminals.put("assignmentAccessBlob", new NtAssignmentAccessBlob());
        this.nonTerminals.put("assignmentAccessStar", new NtAssignmentAccessStar());
        this.nonTerminals.put("valueExpression", new NtValueExpression());
        this.nonTerminals.put("array", new NtArray());
        this.nonTerminals.put("arrayMemberStar", new NtArrayMemberStar());
        this.nonTerminals.put("arrayMember", new NtArrayMember());
        this.nonTerminals.put("object", new NtObject());
        this.nonTerminals.put("objectMemberStar", new NtObjectMemberStar());
        this.nonTerminals.put("objectMember", new NtObjectMember());
        this.nonTerminals.put("returnExpression", new NtReturnExpression());
        this.nonTerminals.put("returnEmpty", new NtReturnEmpty());
        this.nonTerminals.put("assignmentStatement", new NtAssignmentStatement());
        this.nonTerminals.put("procedureStatement", new NtProcedureStatement());
        this.nonTerminals.put("ifStatement", new NtIfStatement());
        this.nonTerminals.put("ifElseStatement", new NtIfElseStatement());
        this.nonTerminals.put("ifElifElseStatement", new NtIfElifElseStatement());
        this.nonTerminals.put("forValueStatement", new NtForValueStatement());
        this.nonTerminals.put("forKeyValueStatement", new NtForKeyValueStatement());
        this.nonTerminals.put("returnStatement", new NtReturnStatement());
        this.nonTerminals.put("whileStatement", new NtWhileStatement());
        this.nonTerminals.put("tryCatchFinallyStatement", new NtTryCatchFinallyStatement());
        this.nonTerminals.put("throwStatement", new NtThrowStatement());
        this.nonTerminals.put("nameExpression", new NtNameExpression());
        this.nonTerminals.put("ifMember", new NtIfMember());
        this.nonTerminals.put("elifMember", new NtElifMember());
        this.nonTerminals.put("elifMemberStar", new NtElifMemberStar());
        this.nonTerminals.put("elseMember", new NtElseMember());

        for (NonTerminal nt: this.nonTerminals.values()) {
            nt.defineProductions(this);
        }
        for (NonTerminal nt: this.nonTerminals.values()) {
            nt.defineTemplate(this);
        }

        /*
        When a non terminal starts with exactly the sames symbols as a simpler one,
        when parsing the code of an instance of the more complex non terminal,
        if the parser tries the simple one first, it will accept it and not be able to go back
        to accept the longer one.
        It results with a syntax error and the complex non terminal is never tried.
        That's why order of productions is important.

        http://stackoverflow.com/questions/9814528/recursive-descent-parser-implementation
        S -> if E then S
        S -> if E then S else S
        So this begs the question if your parser sees an 'if' token,
        which production should it choose to process the input?
        The answer is it has no idea which one to choose because unlike humans
        the compiler can't look ahead into the input stream to search for an 'else' token.
        This is a simple problem to fix by applying a rule known as Left-Factoring,
        very similar to how you would factor an algebra problem.

        All you have to do is create a new non-terminal symbol S' (S-prime)
        whose right hand side will hold the pieces of the productions that aren't common,
        so your S productions no becomes:

        S  -> if E then S S'
        S' -> else S
        S' -> e
        (e is used here to denote the empty string, which basically means there is no
         input seen)
        */
	}
	
	@Override
	public NonTerminal getStart() {
		return this.nt("start");
	}

    @Override
    public NonTerminal nt(String name) {
        NonTerminal nt = this.nonTerminals.get(name);
        if (nt == null) {
            throw new RuntimeException("Grammar is wrong: " + name + " does not exist.");
        }
        return nt;
    }
}
