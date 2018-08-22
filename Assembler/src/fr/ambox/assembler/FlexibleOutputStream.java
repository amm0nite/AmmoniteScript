package fr.ambox.assembler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;

public class FlexibleOutputStream extends OutputStream {

	private ArrayDeque<Byte> buffer;
	private OutputStream outputStream;
	private long bytesWritten;

	public FlexibleOutputStream() {
		this.outputStream = null;
		this.buffer = new ArrayDeque<Byte>();
		this.bytesWritten = 0;
	}

	@Override
	public void write(int b) throws IOException {
		if (this.outputStream == null) {
			if (this.buffer.size() == 4096) {
				this.buffer.removeFirst();
			}
			this.buffer.addLast((byte) b);
		}
		else {
			this.outputStream.write(b);
		}
		this.bytesWritten++;
	}

	synchronized public void set(OutputStream outputStream) {
		try {
			for (Byte b: this.buffer) {
				outputStream.write(b);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		this.outputStream = outputStream;
	}

	synchronized public void unset() {
		this.outputStream = null;
		this.buffer.clear();
	}

	public long getBytesWritten() {
		return bytesWritten;
	}

	public void resetBytesWritten() {
		this.bytesWritten = 0;
	}
}
