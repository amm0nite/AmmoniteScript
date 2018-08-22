package fr.ambox.assembler;

import java.io.IOException;
import java.io.InputStream;

public class FlexibleInputStream extends InputStream {

	private InputStream inputStream;
	
	public FlexibleInputStream() {
		this.inputStream = null;
	}
	
	@Override
	public int read() throws IOException {
		try {
			return this.waitForInput().read();
		} 
		catch (InterruptedException e) {
			return -1;
		}
	}
	
	synchronized public InputStream waitForInput() throws InterruptedException {
		while (this.inputStream == null) {
			this.wait();
		}
		return this.inputStream;
	}

	synchronized public void set(InputStream inputStream) {
		this.inputStream = inputStream;
		this.notify();
	}

	synchronized public void unset() {
		this.inputStream = null;
	}
}
