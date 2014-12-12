package it.polimi.processing.collector;

import it.polimi.processing.collector.saver.data.CollectableData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileWriterService {

	public static boolean write(String where, CollectableData d) {
		try {
			FileWriter writer;
			File file = new File(where);
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new FileWriter(file, true);
			writer.write(d.getData());
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			log.info(e.getMessage());
			return false;
		}
	}
}
