package it.polimi.postprocessing;

import it.polimi.services.FileService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
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
	private static int numerOfExecutions = 10;

	public static void main(String[] args) throws IOException, ParseException {

		basePath += dt.format(dt.parse("2015-04-30")) + "/";

		File folder = new File(basePath);
		File[] listFiles = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});

		for (File f : listFiles) {
			File[] sfs = f.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});
			for (File sf : sfs) {
				process(head = sf.getName(), f.getName());
			}

		}

	}

	public static void process(String folder, String EXPERIMENT) throws IOException {
		String memlog = "MEMLOG_EN";
		String latPrefix = "UFD_LATLOG_EN";
		String latlog = "LATLOG_EN";
		String memPrefix = "UFD_MEMLOG_EN";
		List<String> filesList = new ArrayList<String>();
		String execution = "";
		String pathname = basePath + EXPERIMENT + "/" + folder;
		File f = new File(pathname); // current directory
		int expNum = Integer.parseInt(EXPERIMENT.replace("exp", ""));
		File[] files = f.listFiles();
		for (File file : files) {
			if (!file.isDirectory()) {
				String canonicalPath = file.getName();
				System.out.println(canonicalPath);
				if (file.isDirectory() || canonicalPath.contains(".trig") || canonicalPath.contains("UFD") || !canonicalPath.contains("_EXE0")) {
					continue;
				} else if (canonicalPath.contains("WIN")) {
					move(file, "win");
				} else {

					if (canonicalPath.contains(memlog + expNum)) {
						filesList = new ArrayList<String>();
						for (int i = 0; i < numerOfExecutions; i++) {
							filesList.add(execution = canonicalPath.replaceAll("_EXE" + ".?" + "_", "_EXE" + i + "_"));
							move(new File(pathname + "/" + execution), "mem");
						}

						unify(EXPERIMENT, filesList.toArray(new String[filesList.size()]), canonicalPath.replace(memlog, memPrefix).replaceAll(
								"_EXE" + ".?" + "_", ""));

					} else if (canonicalPath.contains(latlog + expNum)) {
						filesList = new ArrayList<String>();
						for (int i = 0; i < numerOfExecutions; i++) {
							filesList.add(execution = canonicalPath.replaceAll("_EXE" + ".?" + "_", "_EXE" + i + "_"));
							move(new File(pathname + "/" + execution), "lat");
						}
						unify(EXPERIMENT, filesList.toArray(new String[filesList.size()]), canonicalPath.replace(latlog, latPrefix).replaceAll(
								"_EXE" + ".?" + "_", ""));
					}
				}

			}
		}

	}

	protected static void move(File file, String folder) throws IOException {
		String newPath = file.getPath().replace(file.getName(), folder + "/");
		new File(newPath).mkdirs();
		try {
			Files.copy(file.toPath(), new File(newPath + file.getName()).toPath());
			file.deleteOnExit();
		} catch (FileAlreadyExistsException e) {
			System.out.println("Already moved");
		}
	}

	private static void unify(final String exp, String[] files, String outputName) throws IOException {
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

				String memoryB = parts[2];
				String memoryA = parts[3];
				String latency = parts[4];
				String newCSV = memoryB + "," + memoryA + "," + latency;
				if (map.containsKey(id)) {
					map.put(id, map.get(id) + "," + newCSV);
				} else {
					map.put(id, eventNum + "," + id + "," + newCSV);
				}
				eventNum++;
			}

		}

		final String number = exp.replace("exp", "");
		write(header);
		Set<String> keySet = map.keySet();

		List<String> keyList = new ArrayList<String>(keySet);
		Collections.sort(keyList, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				Integer i1 = new Integer(o1.replace("<http://example.org/" + number + "/", "").replace(">", ""));
				Integer i2 = new Integer(o2.replace("<http://example.org/" + number + "/", "").replace(">", ""));
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
