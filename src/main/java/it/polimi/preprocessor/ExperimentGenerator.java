package it.polimi.preprocessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import lombok.extern.log4j.Log4j;

@Log4j
public class ExperimentGenerator {

	private static Properties sourceProp;
	private static boolean autoCalcInitSize = true;

	public static void main(String[] args) throws IOException {
		String pathToClone = "properties/";
		String inputName = "default_teststand.properties";
		String outputName = "prova_auto";
		sourceProp = new Properties();
		InputStream inputStream = ExperimentGenerator.class.getClassLoader().getResourceAsStream(pathToClone + inputName);
		if (inputStream != null)
			sourceProp.load(inputStream);

		for (int i = 0; i < 5; i++) {
			File f = new File("./src/main/resources/" + pathToClone + outputName + i + ".properties");
			if (!f.exists())
				f.createNewFile();

			Properties dest_prop = (Properties) sourceProp.clone();
			for (Enumeration propertyNames = sourceProp.propertyNames(); propertyNames.hasMoreElements();) {
				Object key = propertyNames.nextElement();
				dest_prop.put(key, sourceProp.get(key));
			}

			if (autoCalcInitSize)
				calculateInitSize(i, dest_prop);

			OutputStream out = new FileOutputStream(f);
			dest_prop.store(out, "This is an optional header comment string");
		}

	}

	protected static void calculateInitSize(int i, Properties dest_prop) {
		final int eventInWindow = 10;
		dest_prop.setProperty("execution_number", i + "");
		if (Boolean.parseBoolean(dest_prop.getProperty("external_time_control_on")) && "AGGREGATION".equals(dest_prop.getProperty("time_strategy"))) {
			int triple_in_window = new Integer(dest_prop.getProperty("triple_in_window"));
			int aggregation = new Integer(dest_prop.getProperty("aggregation"));

			dest_prop.setProperty("init_size", (triple_in_window / (eventInWindow * aggregation) + ""));
			dest_prop.setProperty("rspevent_number", (triple_in_window * aggregation) + "");
		}
	}
}
