package it.polimi.utils;

import it.polimi.processing.services.system.GetPropertyValues;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.log4j.Log4j;

@Log4j
public final class FileUtils {

	public static String daypath;
	static {

		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		try {

			Date d = dt.parse(GetPropertyValues.getProperty("experiment_date"));

			daypath = "./data/output/" + dt.format(d) + "/";

			new File(daypath).mkdirs();
			new File(daypath + "database/").mkdirs();
		} catch (java.text.ParseException e) {
			log.error("Wrong parsing");
		}
	}

	public static final String MODEL_FILE_PATH = "";
	public static final String INPUT_FILE_PATH = "./data/input/";
	public static final String OUTPUT_FILE_PATH = daypath;
	public static final String PREPROCESSING_FILE_PATH = "./data/preprocessing/";
	public static final String PREPROCESSING_EXCLUDED_FILE_PATH = PREPROCESSING_FILE_PATH + "excluded/";
	public static final String PREPROCESSING_DATATYPE_FILE_PATH = PREPROCESSING_FILE_PATH + "datatype/";
	public static final String CSV_OUTPUT_FILE_PATH = OUTPUT_FILE_PATH + "/";

	public static final String CSV = ".csv";
	public static final String SQLLITE_OUTPUT_FILE_PATH = OUTPUT_FILE_PATH + "database/";
	public static final String SQLLITE_FILE_EXTENSION = ".db";
	public static final String TRIG_OUTPUT_FILE_PATH = OUTPUT_FILE_PATH + "/";
	public static final String TRIG_FILE_EXTENSION = ".trig";

	public static final String RHODF_RULE_SET = "./data/inference/rules/rdfs-rules-rhodf.rules";
	public static final String RHODF_RULE_SET_RUNTIME = "./data/inference/rules/rdfs-rules-rhodf-runtime.rules";

	public static final String UNIV_BENCH = "./data/inference/univ-bench-rdfs.rdf";

	public static final String UNIV_BENCH_RDFS = "./data/inference/univ-bench-rdfs-without-datatype-materialized.rdfs";
	public static final String UNIV_BENCH_RDFS_MODIFIED = "./data/inference/univ-bench-rdfs-without-datatype-materialized.rdfs";

	public static final String UNIV_BENCH_RHODF = "./data/inference/univ-bench-rdfs-materialized-rhodf.rdf";
	public static final String UNIV_BENCH_RHODF_MODIFIED = "./data/inference/univ-bench-rdfs-materialized-rhodf-modified.rdf";
	public static final String DATABASEPATH = daypath + "database/";

}
