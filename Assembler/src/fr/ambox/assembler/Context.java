package fr.ambox.assembler;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;

import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.datas.*;
import fr.ambox.assembler.exceptions.*;
import fr.ambox.assembler.procedures.*;
import fr.ambox.lexer.Lexer;
import fr.ambox.lexer.NoTokenException;
import fr.ambox.lexer.Token;
import fr.ambox.lexer.TokenList;
import fr.ambox.lexer.tokenClassLists.AssemblerList;

public class Context {
    private ArrayDeque<Step> bootstrap;
    private DataStore store;
    private SkipState skipState;

    private boolean stopFlag;
	private Stack stack;
	private DictStack dictStack;
	private ArrayDeque<Data[]> savedStacks;

	private AtomList blockBuffer;
	private int blockCounter;
	private boolean blockFlag;
	private int atomCounter;

	private FlexibleInputStream stdin;
	private FlexibleOutputStream stdout;
    private FlexibleOutputStream stderr;

	private ArrayDeque<Step> stepStack;
	private ArrayDeque<Trace> callStack;
    private FatalErrorException error;
    private MetaData meta;
    private Limits limits;

    public Context() {
        this.bootstrap = new ArrayDeque<Step>();
		this.store = new DataStore();
		this.skipState = SkipState.None;

		this.stopFlag = false;
		this.stack = new Stack();
		this.dictStack = new DictStack();
		this.savedStacks = new ArrayDeque<Data[]>();

        this.blockBuffer = new AtomList();
		this.blockCounter = 0;
		this.blockFlag = false;
		this.atomCounter = 0;
		
		this.stdin = new FlexibleInputStream();
		this.stdout = new FlexibleOutputStream();
        this.stderr = new FlexibleOutputStream();

        this.stepStack = new ArrayDeque<Step>();
        this.callStack = new ArrayDeque<Trace>();
        this.error = null;
        this.meta = new MetaData();
        this.limits = new Limits();

		this.setup();
	}

    public void init(String code) throws NoTokenException, TokenParsingException {
        Lexer lexer = new Lexer(code, new AssemblerList());
        TokenList tokens = lexer.analyse();
        AtomList atoms = new AtomList();
        for (Token token: tokens) {
            atoms.add(TokenParser.parse(token));
        }
        this.bootstrap.clear();
        this.bootstrap.push(new Step(atoms, 0));
    }

    public void reboot() {
        this.bootstrap.clear();
        for (Step step: this.stepStack) {
			this.bootstrap.push(step);
        }
        this.stepStack.clear();

        this.atomCounter = 0;
        this.stdout.resetBytesWritten();
        this.stderr.resetBytesWritten();
        this.stopFlag = false;
    }

	private void setup() {
		this.setupProcedure("blstart", new BlockStartProcedure());
		this.setupProcedure("blstop", new BlockStopProcedure());
		this.setupProcedure("add", new AddProcedure());
		this.setupProcedure("sub", new SubProcedure());
		this.setupProcedure("mul", new MulProcedure());
		this.setupProcedure("div", new DivProcedure());
		this.setupProcedure("mod", new ModProcedure());
		this.setupProcedure("append", new AppendProcedure());
		this.setupProcedure("stack", new StackProcedure());
		this.setupProcedure("def", new DefinitionProcedure());
		this.setupProcedure("fcreate", new FunctionCreationProcedure());
		this.setupProcedure("concat", new ConcatProcedure());
		this.setupProcedure("eq", new EqualsProcedure());
		this.setupProcedure("true", new TrueProcedure());
		this.setupProcedure("false", new FalseProcedure());
		this.setupProcedure("gt", new GreaterProcedure());
		this.setupProcedure("lt", new LesserProcedure());
		this.setupProcedure("ge", new GreaterEqualsProcedure());
		this.setupProcedure("le", new LesserEqualsProcedure());
		this.setupProcedure("if", new IfProcedure());
		this.setupProcedure("ifelse", new IfElseProcedure());
		this.setupProcedure("loop", new LoopProcedure());
		this.setupProcedure("dup", new DupProcedure());
		this.setupProcedure("repeat", new RepeatProcedure());
		this.setupProcedure("range", new RangeProcedure());
		this.setupProcedure("for", new ForProcedure());
		this.setupProcedure("forv", new ForValueProcedure());
		this.setupProcedure("forkv", new ForKeyValueProcedure());
		this.setupProcedure("while", new WhileProcedure());
		this.setupProcedure("pop", new PopProcedure());
		this.setupProcedure("length", new LengthProcedure());
		this.setupProcedure("table", new TableProcedure());
		this.setupProcedure("keys", new KeysProcedure());
		this.setupProcedure("values", new ValuesProcedure());
		this.setupProcedure("set", new SetProcedure());
		this.setupProcedure("get", new GetProcedure());
		this.setupProcedure("null", new NullProcedure());
		this.setupProcedure("isnull", new IsNullProcedure());
		this.setupProcedure("do", new DoProcedure());
		this.setupProcedure("count", new CountProcedure());
		this.setupProcedure("clear", new ClearProcedure());
		this.setupProcedure("copy", new CopyProcedure());
		this.setupProcedure("exch", new ExchangeProcedure());
		this.setupProcedure("load", new LoadProcedure());
		this.setupProcedure("begin", new BeginProcedure());
		this.setupProcedure("end", new EndProcedure());
		this.setupProcedure("not", new NotProcedure());
		this.setupProcedure("reverse", new ReverseProcedure());
		this.setupProcedure("floor", new FloorProcedure());
		this.setupProcedure("ceil", new CeilProcedure());
		this.setupProcedure("time", new TimeProcedure());
		this.setupProcedure("trace", new TraceProcedure());
		this.setupProcedure("return", new ReturnProcedure());
		this.setupProcedure("save", new SaveProcedure());
		this.setupProcedure("restore", new RestoreProcedure());
		this.setupProcedure("core", new CoreProcedure());
		this.setupProcedure("int", new IntProcedure());
		this.setupProcedure("float", new FloatProcedure());
		this.setupProcedure("array", new ArrayProcedure());
		this.setupProcedure("tryc", new TryCatchProcedure());
		this.setupProcedure("trycf", new TryCatchFinallyProcedure());
		this.setupProcedure("tryf", new TryFinallyProcedure());
		this.setupProcedure("throw", new ThrowProcedure());
        this.setupProcedure("print", new PrintProcedure());
		this.setupProcedure("call", new CallProcedure());
		this.setupProcedure("uncall", new UncallProcedure());
		this.setupProcedure("free", new FreeProcedure());
        this.setupProcedure("delete", new DeleteProcedure());
        this.setupProcedure("try", new TryProcedure());
        this.setupProcedure("catch", new CatchProcedure());
		this.setupProcedure("import", new ImportProcedure());
        this.setupProcedure("break", new BreakProcedure());
        this.setupProcedure("unbreak", new UnbreakProcedure());
        this.setupProcedure("continue", new ContinueProcedure());
        this.setupProcedure("uncontinue", new UncontinueProcedure());
        this.setupProcedure("exit", new ExitProcedure());
        this.setupProcedure("charAt", new CharAtProcedure());

		this.dictStack.put("_line", new IntegerData(0));
	}
	
	private void setupProcedure(String name, Procedure procedure) {
		this.dictStack.put(name, new ProcedureData(procedure));
	}

    public void checkLimits() throws LimitException, FatalErrorException {
        if (this.atomCounter > this.limits.getAtomCountLimit()) {
            throw new AtomLimitException();
        }

        if (this.stdout.getBytesWritten() > this.limits.getBytesWrittenLimit()) {
            throw new BytesWrittenLimitException();
        }

        if (this.stderr.getBytesWritten() > this.limits.getBytesWrittenLimit()) {
            throw new BytesWrittenLimitException();
        }

        // TODO collect all distinct data objects here and count memsize
        int memsize = 0;
        if (memsize > this.limits.getMemsizeLimit()) {
            throw new MemsizeLimitFatalException();
        }
    }

    public boolean shouldSkip(Atom atom) {
        if (this.skipState == SkipState.None) {
            return false;
        }

        if (!(atom instanceof CallAtom)) {
            return true;
        }
        CallAtom callAtom = (CallAtom) atom;
		Data data = null;
		try {
			data = callAtom.resolveName(this);
		}
		catch (UndefinedException e) {
			return true;
		}
		if (!(data instanceof ProcedureData)) {
            return true;
        }
        ProcedureData procedureData = (ProcedureData) data;
        Procedure procedure = procedureData.getProcedure();

        if (this.skipState == SkipState.Break) {
            return !(procedure instanceof UnbreakProcedure);
        }
        if (this.skipState == SkipState.Continue) {
            return !(procedure instanceof UncontinueProcedure);
        }
        if (this.skipState == SkipState.Return) {
            return !(procedure instanceof UncallProcedure);
        }
		if (this.skipState == SkipState.Error) {
			return !(procedure instanceof TryProcedure);
		}

        throw new IllegalStateException();
    }

    public Stack getStack() {
        return this.stack;
    }

	public void setStack(Stack stack) {
		this.stack = stack;
	}

	public DictStack getDictStack() {
		return this.dictStack;
	}

    public void setDictStack(DictStack dictStack) {
        this.dictStack = dictStack;
    }

    public boolean getBlockFlag() {
        return this.blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public int getBlockCounter() {
        return this.blockCounter;
    }

    public void setBlockCounter(int blockCounter) {
        this.blockCounter = blockCounter;
    }

    public void resetBlockCounter() {
        this.blockCounter = 0;
    }

    public void incrementBlockCounter() {
        this.blockCounter++;
    }

    public void decrementBlockCounter() {
        this.blockCounter--;
    }

    public AtomList getBlockBuffer() {
        return this.blockBuffer;
    }

    public void setBlockBuffer(AtomList blockBuffer) {
        this.blockBuffer = blockBuffer;
    }

    public void resetBlockBuffer() {
        this.blockBuffer = new AtomList();
    }

	public boolean getStopFlag() {
		return this.stopFlag;
	}

	public void setStopFlag(boolean stopFlag) {
		this.stopFlag = stopFlag;
	}

    public ArrayDeque<Data[]> getSavedStacks() {
        return this.savedStacks;
    }

    public void setSavedStacks(ArrayDeque<Data[]> savedStacks) {
        this.savedStacks = savedStacks;
    }

	public FlexibleOutputStream getStdout() {
		return this.stdout;
	}

	public void setStdout(OutputStream outputStream) {
		this.stdout.set(outputStream);
	}

	public void unsetStdout() {
		this.stdout.unset();
	}

    public FlexibleInputStream getStdin() {
        return this.stdin;
    }

	public void setStdin(InputStream inputStream) {
		this.stdin.set(inputStream);
	}

	public void unsetStdin() {
		this.stdin.unset();
	}

    public FlexibleOutputStream getStderr() {
        return this.stderr;
    }

	public void setStderr(OutputStream outputStream) {
		this.stderr.set(outputStream);
	}

    public void unsetStderr() {
        this.stderr.unset();
    }

	public void setMeta(MetaData meta) {
		this.meta = meta;
	}

    public MetaData getMeta() {
        return this.meta;
    }

    public ArrayDeque<Trace> getCallStack() {
        return this.callStack;
    }

    public void setCallStack(ArrayDeque<Trace> callStack) {
        this.callStack = callStack;
    }

	public void incrementAtomCounter() {
		this.atomCounter++;
	}

    public ArrayDeque<Step> getStepStack() {
        return this.stepStack;
    }

    public void setStepStack(ArrayDeque<Step> stepStack) {
        this.stepStack = stepStack;
    }

    public ArrayDeque<Step> getBootstrap() {
        return this.bootstrap;
    }

	public void setBootstrap(ArrayDeque<Step> bootstrap) {
		this.bootstrap = bootstrap;
	}

    public DataStore getStore() {
        return this.store;
    }

	public void setStore(DataStore store) {
		this.store = store;
	}

    public void resetSkipState() {
        this.skipState = SkipState.None;
    }

    public SkipState getSkipState() {
        return this.skipState;
    }

    public void setSkipState(SkipState skipState) {
        this.skipState = skipState;
    }

    public FatalErrorException getError() {
        return this.error;
    }

    public void setError(FatalErrorException error) {
        this.error = error;
    }

    public int getAtomCount() {
        return this.atomCounter;
    }

    public Limits getLimits() {
        return this.limits;
    }
}
