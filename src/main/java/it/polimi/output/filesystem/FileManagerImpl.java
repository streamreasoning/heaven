package it.polimi.output.filesystem;

import it.polimi.enums.ExecutionStates;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

public class FileManagerImpl extends FileManager {

	@Override
	public ExecutionStates close() {
		Logger.getRootLogger().info("Nothing to do");
		return status = ExecutionStates.CLOSED;
	}

	@Override
	public ExecutionStates init() {
		Logger.getRootLogger().info("Nothing to do");
		return status = ExecutionStates.READY;
	}

	public void toFile(String filePath, String fileNameWithExtension,
			Writable content) throws IOException {
		if (ExecutionStates.READY.equals(status)) {
			File file;
			if (filePath == null || filePath.isEmpty())
				file = new File(fileNameWithExtension);
			else {
				file = new File(filePath + fileNameWithExtension);
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fop = new FileOutputStream(file, true);
			fop.write(content.getBytes());
			fop.write(System.getProperty("line.separator").getBytes());
			fop.flush();
			fop.close();
		} else {
			Logger.getRootLogger().warn("Not Ready to write file");
		}
	}

	@Override
	public void toFile(String fileNameWithExtension, Writable content)
			throws IOException {
		toFile(null, fileNameWithExtension, content);
	}

	@Override
	public void toFile(String fileNameWithExtension, byte[] content)
			throws IOException {
		toFile(null, fileNameWithExtension, content);
	}

	@Override
	public void toFile(String filePath, String fileNameWithExtension,
			byte[] content) throws IOException {
		if (ExecutionStates.READY.equals(status)) {
			File file;
			if (filePath == null || filePath.isEmpty()) {
				file = new File(fileNameWithExtension);
			} else {
				file = new File(filePath + fileNameWithExtension);
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fop = new FileOutputStream(file, true);
			fop.write(content);
			fop.write(System.getProperty("line.separator").getBytes());
			fop.flush();
			fop.close();
		} else {
			Logger.getRootLogger().warn("Not Ready to write file");
		}
	}

}
