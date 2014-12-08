package it.polimi.postprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostUtils {

	private static String daypath;

	private static Date d;
	private static String today;
	private static String yesterday = "2014-11-11";
	static {
		d = it.polimi.utils.FileUtils.d;
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		today = dt.format(d).toString();
		daypath = "src/main/resources/" + dt.format(d) + "/comparation/";

		new File(daypath).mkdirs();
		new File(daypath + "database/").mkdirs();
	}
	public static final String COMPARATION_OUT_PATH = daypath;
	public static final String CSV_OUT_PATH = daypath + "";
	public static final String TRIG_OUT_PATH = daypath + "";
	public static final String DATABASE_PATH = daypath + "database/";
	public static final String INPUT_FILE_PATH = "src/main/resources/data/input/";
	public static final String todaypath = "src/main/resources/" + today + "/";
	public static final String customPath = "src/main/resources/" + "2014-11-19" + "/";

	public static BufferedReader getBuffer(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		return new BufferedReader(new FileReader(file));
	}
}
