package fr.ambox.machine;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import fr.ambox.assembler.Context;
import fr.ambox.compiler.Assembly;
import fr.ambox.compiler.Compiler;
import fr.ambox.machine.consumers.MakerWorker;
import fr.ambox.machine.consumers.RunnerWorker;
import fr.ambox.machine.consumers.SchedulerWorker;
import fr.ambox.program.Program;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

public class CommandLine implements Runnable {
	@Parameter(names = "-f", description = "FileName")
	private String filename = "input.txt";

    @Parameter(names = "-a", description = "Action", required = true)
    private String action;

	public static void main(String[] args) {
        CommandLine cm = new CommandLine();
        new JCommander(cm, args);
        cm.run();
	}

    @Override
    public void run() {
        if (this.action.equals("execute")) {
            this.execute(this.getFileContent());
        }
        else if (this.action.equals("compile")) {
            this.compile(this.getFileContent());
        }
        else if (this.action.equals("maker")) {
            MakerWorker worker = new MakerWorker();
            worker.run();
        }
        else if (this.action.equals("runner")) {
            RunnerWorker worker = new RunnerWorker();
            worker.run();
        }
        else if (this.action.equals("scheduler")) {
            SchedulerWorker worker = new SchedulerWorker();
            worker.run();
        }
        else {
            this.error("unknown action");
        }
    }

    private String getFileContent() {
        String content = null;

        File file = new File(this.filename);
        if (!file.exists()) {
            this.error(this.filename + " does not exist");
        }

        try {
            content = new String(Files.readAllBytes(file.toPath()), "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            this.error("unsupported encoding");
        }
        catch (IOException e) {
            this.error("could not read the file");
        }

        return content;
    }

	private void error(String string) {
		System.err.println("Error");
		System.err.println(string);
		System.exit(-1);
	}

	private void compile(String code) {
        try {
            Compiler compiler = new Compiler();
            Assembly assembly = compiler.compile(code);
            System.out.println(assembly.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            this.error(e.getMessage());
        }
    }

	private void execute(String code) {
        try {
            Program program = Program.make(code);
            Context context = program.getContext();

            context.setStdin(System.in);
            context.setStdout(System.out);
            context.setStderr(System.err);

            program.run();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.error(e.getMessage());
        }
	}
}
