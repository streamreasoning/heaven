package it.polimi.processing.services;

import it.polimi.processing.collector.data.CollectableData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileService {

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
			log.error(where);
			log.error(e.getMessage());
			return false;
		}
	}

	public static void createFolders(String vsfolder) {
		new File(vsfolder).mkdirs();
	}

	public static void createFolder(String vsfolder) {
		new File(vsfolder).mkdir();
	}

	public static void createOutputFolder(String folder) {
		new File(folder).mkdirs();
	}

	public static BufferedReader getBuffer(String fileName) {
		log.info("Try to load [" + fileName + "]");
		try {
			File file = new File(fileName);
			return new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
