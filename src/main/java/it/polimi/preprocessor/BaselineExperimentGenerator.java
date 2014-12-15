package it.polimi.preprocessor;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.rspengine.windowed.jena.enums.JenaEventType;
import it.polimi.processing.rspengine.windowed.jena.enums.Reasoner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import lombok.extern.log4j.Log4j;

@Log4j
public class BaselineExperimentGenerator {

	private static Properties sourceProp;
	private static boolean autoCalcInitSize = true;
	private static int aggregation = 5;
	private static final int eventInWindow = 10;
	private static final boolean result_log_enabled = false, save_abox_log = false;
	private static final EventBuilderMode mode = EventBuilderMode.CONSTANT;
	private static final DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) throws IOException {
		String pathToClone = "properties/";
		String inputName = "default_teststand.properties";
		String outputName = "";
		Date experimentDate = new Date();
		sourceProp = new Properties();
		InputStream inputStream = BaselineExperimentGenerator.class.getClassLoader().getResourceAsStream(pathToClone + inputName);
		if (inputStream != null)
			sourceProp.load(inputStream);
		int experimentNumber = 0;
		for (Reasoner reasoner : Reasoner.values()) {
			for (JenaEventType engine : JenaEventType.values()) {
				for (int tripleInWindow = 100; tripleInWindow < 10000000; tripleInWindow *= 10) {
					for (String kind : new String[] { "memory", "latency" }) {
						for (int i = 0; i < 5; i++) {
							outputName = kind.toUpperCase() + "_" + tripleInWindow + "_" + engine + "_" + reasoner + "_EXEN";
							String pathname = "src/main/resources/" + pathToClone + "baseline/constant/" + reasoner.toString().toLowerCase() + "/"
									+ engine.toString().toLowerCase() + "/" + tripleInWindow + "/" + kind + "/";
							File folder = new File(pathname);
							folder.mkdirs();

							File f = new File(pathname + outputName + i + ".properties");
							if (!f.exists())
								f.createNewFile();

							Properties dest_prop = (Properties) sourceProp.clone();
							for (Enumeration propertyNames = sourceProp.propertyNames(); propertyNames.hasMoreElements();) {
								Object key = propertyNames.nextElement();
								dest_prop.put(key, sourceProp.get(key));
							}

							if (autoCalcInitSize)
								calculateInitSize(i, dest_prop, tripleInWindow);

							if ("memory".equals(kind)) {
								dest_prop.setProperty("latency_log_enabled", "false");
								dest_prop.setProperty("experiment_type", "false");

							} else if ("latency".equals(kind)) {
								dest_prop.setProperty("memory_log_enabled", "false");
								dest_prop.setProperty("latency_log_enabled", "true");
							}

							dest_prop.setProperty("experiment_type", kind.toUpperCase());

							switch (mode) {
								case CONSTANT:
									dest_prop.setProperty("streaming_mode", "CONSTANT");
									dest_prop.setProperty("x_size", "0");
									dest_prop.setProperty("y_size", "0");
									break;
								default:
									break;
							}

							dest_prop.setProperty("execution_number", i + "");
							dest_prop.setProperty("experiment_number", experimentNumber + "");

							dest_prop.setProperty("experiment_date", dt.format(experimentDate));

							OutputStream out = new FileOutputStream(f);
							dest_prop.store(out, "This is an optional header comment string");
						}
					}
					experimentNumber++;
				}
			}
		}

	}

	protected static void calculateInitSize(int i, Properties dest_prop, int tripleInWindow) {
		dest_prop.setProperty("external_time_control_on", "true");
		dest_prop.getProperty("time_strategy", "AGGREGATION");
		dest_prop.setProperty("init_size", (tripleInWindow / (eventInWindow * aggregation) + ""));
		final int rspEvents = ((tripleInWindow * aggregation) + 100) < 1000 ? 1000 * aggregation : (tripleInWindow * aggregation) + 100;
		dest_prop.setProperty("rspevent_number", rspEvents + "");
	}

}
