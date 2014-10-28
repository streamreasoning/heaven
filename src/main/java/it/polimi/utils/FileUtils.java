package it.polimi.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

	private static String daypath;
	private static Date d;
	static {
		d = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

		daypath = "src/main/resources/data/output/" + dt.format(d) + "/";

		new File(daypath).mkdirs();

		new File(daypath + "plain/").mkdirs();
		new File(daypath + "plainrhodf/").mkdirs();
		new File(daypath + "jena/").mkdirs();
		new File(daypath + "jenared/").mkdirs();

		new File(daypath + "database/").mkdirs();

	}

	public static final String MODEL_FILE_PATH = "";
	public static final String INPUT_FILE_PATH = "src/main/resources/data/input/";
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
