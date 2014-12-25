package it.polimi.postprocessing;

import it.polimi.services.FileService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OutputLogDataUnifier {
	private static FileOutputStream fop;
	private static Map<String, String> map;
	private static final byte[] EOF = System.getProperty("line.separator").getBytes();
	private static final DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
	private static String basePath = "./data/output/";
	private static String head;

	public static void main(String[] args) throws IOException, ParseException {

		basePath += dt.format(dt.parse("2014-12-24")) + "/exp";

		head = "rhodf";

		// process("rhodf", 10);
		// // process("rhodf", 11);
		// process("rhodf", 14);
		// process("rhodf", 15);
		// process("rhodf", 18);
		// process("rhodf", 19);

		process("rhodf", 152);
		process("rhodf", 150);
		process("rhodf", 151);

		// process("rhodf", 104);
		// process("rhodf", 105);
		// process("rhodf", 106);
		// process("rhodf", 108);
		// process("rhodf", 109);

	}

	public static void process(String folder, int EXPERIMENT) throws IOException {

		String memlog = "MEMLOG_EN";
		String latlog = "LATLOG_EN";
		List<String> memFilesList = new ArrayList<String>();
		List<String> latFilesList = new ArrayList<String>();
		String memOutName = "";
		String latOutName = "";

		String pathname = basePath + EXPERIMENT + "/" + folder;
		File f = new File(pathname); // current directory

		File[] files = f.listFiles();
		for (File file : files) {
			if (!file.isDirectory()) {
				String canonicalPath = file.getName();
				System.out.println(canonicalPath);
				if (file.isDirectory() || canonicalPath.contains(".trig")) {
					continue;
				} else if (canonicalPath.contains("WIN")) {
					move(file, "win");
				} else if (canonicalPath.contains(memlog + EXPERIMENT)) {
					memFilesList.add(canonicalPath);
					memOutName = canonicalPath.replace(memlog, "UFD_MEMLOG_EN").replaceAll("_EXE" + ".?" + "_", "");
					move(file, "mem");
				} else if (canonicalPath.contains(latlog + EXPERIMENT)) {
					latFilesList.add(canonicalPath);
					latOutName = canonicalPath.replace(latlog, "UFD_LATLOG_EN").replaceAll("_EXE" + ".?" + "_", "");
					move(file, "lat");
				}
			}
		}
		unify(EXPERIMENT, memFilesList.toArray(new String[memFilesList.size()]), memOutName);
		unify(EXPERIMENT, latFilesList.toArray(new String[latFilesList.size()]), latOutName);

	}

	protected static void move(File file, String folder) throws IOException {
		String newPath = file.getPath().replace(file.getName(), folder + "/");
		new File(newPath).mkdirs();
		Files.copy(file.toPath(), new File(newPath + file.getName()).toPath());
		file.deleteOnExit();
	}

	private static void unify(final int exp, String[] files, String outputName) throws IOException {
		String pathname = basePath + exp + "/" + head + "/" + outputName;
		File output = new File(pathname);

		if (!output.exists()) {
			output.createNewFile();
		}
		if (output.isDirectory()) {
			return;
		}

		fop = new FileOutputStream(output, true);

		String header = "ENUM, ID";
		map = new HashMap<String, String>();

		for (int i = 0; i < files.length; i++) {

			String f = files[i];
			System.out.println(f);
			header += ",MEMB" + i + ",MEMA" + i + ",LAT" + i;
			BufferedReader br = FileService.getBuffer(basePath + exp + "/" + head + "/" + f);
			String line;
			int eventNum = 0;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				String id = parts[0];
				eventNum++;
				String memoryB = parts[2];
				String memoryA = parts[3];
				String latency = parts[4];
				String newCSV = memoryB + "," + memoryA + "," + latency;
				if (map.containsKey(id)) {
					map.put(id, map.get(id) + "," + newCSV);
				} else {
					map.put(id, eventNum + "," + id + "," + newCSV);
				}

			}

		}

		write(header);
		Set<String> keySet = map.keySet();

		List<String> keyList = new ArrayList<String>(keySet);
		Collections.sort(keyList, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				Integer i1 = new Integer(o1.replace("<http://example.org/" + exp + "/", "").replace(">", ""));
				Integer i2 = new Integer(o2.replace("<http://example.org/" + exp + "/", "").replace(">", ""));
				return i1.compareTo(i2);
			}
		});
		for (int i = 0; i < keyList.size(); i++) {
			write(map.get(keyList.get(i)));
		}
	}

	public static void write(String name) throws IOException {
		fop.write(name.getBytes());
		fop.write(EOF);
		fop.flush();
	}
}
