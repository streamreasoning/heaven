package it.polimi.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Log4j
public class FileUtils {

	private static String daypath;
	@Getter
	public static Date d;
	static {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = dt.parse("2014-11-19");
		} catch (java.text.ParseException e) {
			log.info("Wrong parsing");
			d = new Date();
		}

		daypath = "src/main/resources/data/output/" + dt.format(d) + "/";

		new File(daypath).mkdirs();

		new File(daypath + "plain2369/").mkdirs();
		new File(daypath + "plain2369NW/").mkdirs();
		new File(daypath + "plain2369NWM/").mkdirs();
		new File(daypath + "plain2369NM/").mkdirs();
		new File(daypath + "jenasmpl/").mkdirs();
		new File(daypath + "jenasmplNW/").mkdirs();
		new File(daypath + "jenasmplNWM/").mkdirs();
		new File(daypath + "jenasmplNM/").mkdirs();
		new File(daypath + "jenarhodf/").mkdirs();
		new File(daypath + "jenarhodfNW/").mkdirs();
		new File(daypath + "jenarhodfNM/").mkdirs();
		new File(daypath + "jenarhodfNWM/").mkdirs();

		new File(daypath + "database/").mkdirs();

	}

	public static final String MODEL_FILE_PATH = "";
	public static final String INPUT_FILE_PATH = "src/main/resources/data/input/SINK1/1000Events/";
	public static final String OUTPUT_FILE_PATH = daypath;
	public static final String PREPROCESSING_FILE_PATH = "src/main/resources/data/preprocessing/";
	public static final String PREPROCESSING_EXCLUDED_FILE_PATH = PREPROCESSING_FILE_PATH + "excluded/";
	public static final String PREPROCESSING_DATATYPE_FILE_PATH = PREPROCESSING_FILE_PATH + "datatype/";
	public static final String CSV_OUTPUT_FILE_PATH = OUTPUT_FILE_PATH + "/";

	public static final String CSV = ".csv";
	public static final String SQLLITE_OUTPUT_FILE_PATH = OUTPUT_FILE_PATH + "database/";
	public static final String SQLLITE_FILE_EXTENSION = ".db";
	public static final String TRIG_OUTPUT_FILE_PATH = OUTPUT_FILE_PATH + "/";
	public static final String TRIG_FILE_EXTENSION = ".trig";

	public static final String RHODF_RULE_SET = "src/main/resources/data/inference/rules/rdfs-rules-rhodf.rules";
	public static final String RHODF_RULE_SET_RUNTIME = "src/main/resources/data/inference/rules/rdfs-rules-rhodf-runtime.rules";

	public static final String UNIV_BENCH = "src/main/resources/data/inference/univ-bench-rdfs.rdf";

	public static final String UNIV_BENCH_RDFS = "src/main/resources/data/inference/univ-bench-rdfs-without-datatype-materialized.rdfs";
	public static final String UNIV_BENCH_RDFS_MODIFIED = "src/main/resources/data/inference/univ-bench-rdfs-without-datatype-materialized.rdfs";

	public static final String UNIV_BENCH_RHODF = "src/main/resources/data/inference/univ-bench-rdfs-materialized-rhodf.rdf";
	public static final String UNIV_BENCH_RHODF_MODIFIED = "src/main/resources/data/inference/univ-bench-rdfs-materialized-rhodf-modified.rdf";
	public static final String DATABASEPATH = daypath + "database/";

	public static void createFolders(String vsfolder) {
		new File(vsfolder).mkdirs();
	}

	public static void createFolder(String vsfolder) {
		new File(vsfolder).mkdir();
	}
}
