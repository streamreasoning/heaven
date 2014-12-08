package it.polimi.postprocessing;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OutputLogDataUnifier {
	private static FileOutputStream fop;
	private static Map<Integer, String> map;
	private static final byte[] EOF = System.getProperty("line.separator").getBytes();
	private static final DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
	private static String basePath = "src/main/resources/data/output/";
	private static String head;

	public static void main(String[] args) throws IOException, ParseException {

		basePath += dt.format(dt.parse("2014-12-10")) + "/exp";
		for (String string : new String[] { "jenarhodf", "jenasmpl" }) {
			head = string;
			process(string, 6);
			process(string, 7);
			process(string, 8);
		}

	}

	public static void process(String folder, int EXPERIMENT) throws IOException {

		String memlog = "MEMLOG_EN";
		String latlog = "LATLOG_EN";
		List<String> memFilesList = new ArrayList<String>();
		List<String> latFilesList = new ArrayList<String>();
		String memOutName = "";
		String latOutName = "";

		String pathname = basePath + EXPERIMENT + "/" + folder;
		System.out.println(pathname);
		File f = new File(pathname); // current directory

		File[] files = f.listFiles();
		for (File file : files) {
			if (!file.isDirectory()) {
				String canonicalPath = file.getName();
				System.out.println(canonicalPath);
				if (canonicalPath.contains("WIN")) {
					move(file, "win");
				} else if (canonicalPath.contains(memlog + EXPERIMENT) && !canonicalPath.contains(".trig") && !canonicalPath.contains("UFD")) {
					memFilesList.add(canonicalPath);
					memOutName = canonicalPath.replace(memlog, "UFD_MEMLOG_EN").replaceAll("_EXE" + ".?" + "_", "");
					move(file, "mem");
				} else if (canonicalPath.contains(latlog + EXPERIMENT) && !canonicalPath.contains(".trig") && !canonicalPath.contains("UFD")) {
					latFilesList.add(canonicalPath);
					latOutName = canonicalPath.replace(latlog, "UFD_LATLOG_EN").replaceAll("_EXE" + ".?" + "_", "");
					move(file, "lat");
				}

			}
		}
		System.out.println(memOutName);
		System.out.println(latOutName);
		unify(EXPERIMENT, memFilesList.toArray(new String[memFilesList.size()]), memOutName);
		unify(EXPERIMENT, latFilesList.toArray(new String[latFilesList.size()]), latOutName);

	}

	protected static void move(File file, String folder) throws IOException {
		String newPath = file.getPath().replace(file.getName(), folder + "/");
		new File(newPath).mkdirs();
		Files.copy(file.toPath(), new File(newPath + file.getName()).toPath());
		file.deleteOnExit();
	}

	private static void unify(int exp, String[] files, String outputName) throws IOException {
		String pathname = basePath + exp + "/" + head + "/" + outputName;
		System.out.println(pathname);
		File output = new File(pathname);

		if (!output.exists()) {
			output.createNewFile();
		}

		fop = new FileOutputStream(output, true);

		String header = "ENUM, ID";
		map = new HashMap<Integer, String>();

		for (int i = 0; i < files.length; i++) {

			String f = files[i];
			System.out.println(f);
			header += ",MEMB" + i + ",MEMA" + i + ",LAT" + i;
			BufferedReader br = PostUtils.getBuffer(basePath + exp + "/" + head + "/" + f);
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				String id = parts[0];
				String eventNum = parts[1];
				String memoryB = parts[2];
				String memoryA = parts[3];
				String latency = parts[4];

				String newCSV = memoryB + "," + memoryA + "," + latency;
				int key = Integer.parseInt(eventNum);
				if (map.containsKey(key)) {
					map.put(key, map.get(key) + "," + newCSV);
				} else {
					map.put(key, eventNum + "," + id + "," + newCSV);
				}

			}

		}

		write(header);
		Set<Integer> keySet = map.keySet();

		List<Integer> keyList = new ArrayList<Integer>(keySet);
		Collections.sort(keyList);
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
