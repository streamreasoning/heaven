package it.polimi.output.filesystem;

import it.polimi.enums.ExecutionStates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FileManager {

	public static final String FILE_PATH = "";
	public static final String DATA_FILE_PATH = "src/main/resource/data/";
	public static final String OUTPUT_FILE_PATH = "src/main/resource/data/output/";
	public static final String LOG_PATH = "src/main/resource/data/log/";
	protected ExecutionStates status;

	public abstract void toFile(String s, Writable w) throws IOException;

	public abstract void toFile(String filePath, String s, Writable w)
			throws IOException;

	public abstract void toFile(String s, byte[] content) throws IOException;

	public abstract void toFile(String filePath, String s, byte[] content)
			throws IOException;

	public static BufferedReader getBuffer(String fileName)
			throws FileNotFoundException {
		File file = new File(fileName);
		return new BufferedReader(new FileReader(file));
	}


	public abstract ExecutionStates init();
	public abstract ExecutionStates close();

}
