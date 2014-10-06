package it.polimi.output.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManagerImpl extends FileManager {


	@Override
	public void close() {
		System.out.println("Noting to do");
	}

	@Override
	public void init() {
		System.out.println("Noting to do");
	}

	public void toFile(String filePath, String fileNameWithExtension,
			Writable content) throws IOException {
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
	}

}
