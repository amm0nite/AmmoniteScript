package fr.ambox.assembler;

import java.util.Scanner;

public class Interactive {
	
	private Context context;

	public Interactive() throws Exception {
		this.context = new Context();
	}
	
	public String process(String cmd) throws Exception {
		this.context.init(cmd);
		Assembler assembler = new Assembler(this.context);
		assembler.execute();
		return this.context.toString();
	}
	
	public static void main(String[] args) {
		Interactive interactive = null;
		try {
			interactive = new Interactive();
		}
		catch (Exception e) {
			throw new RuntimeException("env setup failed");
		}
		
		boolean running = true;
		Scanner scan = new Scanner(System.in);
		while (running) {
			System.out.print("> ");
			String input = scan.nextLine();
			
			try {
				System.out.println(interactive.process(input));
			}
			catch (Exception e) {
				System.out.println(e);
			}
		}
		scan.close();
	}
}
