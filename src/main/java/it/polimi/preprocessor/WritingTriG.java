package it.polimi.preprocessor;

import java.io.FileOutputStream;
import java.io.IOException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WritingTriG {

	private final byte[] EOF = System.getProperty("line.separator").getBytes();
	private final FileOutputStream fop;

	public void write(String s) throws IOException {
		fop.write(s.getBytes());
	}

	public void close() throws IOException {
		fop.close();
	}

	public void EOF() throws IOException {
		fop.write(EOF);
	}

	public void flush() throws IOException {
		fop.flush();
	}
}