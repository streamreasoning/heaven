package it.polimi.preprocessor.generator;

import it.polimi.processing.enums.FlowRateProfile;
import it.polimi.processing.rspengine.jena.enums.JenaEventType;
import it.polimi.processing.rspengine.jena.enums.Reasoner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class BaselineExperimentGenerator {

	private static Properties sourceProp;
	private static final FlowRateProfile mode = FlowRateProfile.CONSTANT;
	private static final DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
	private static int experimentNumber = 0;
	private static Properties prop;
	private static final Date experimentDate = new Date();

	public static void main(String[] args) throws IOException {
		String pathToClone = "properties/";
		String inputName = "default_teststand.properties";
		String outputName = "";

		sourceProp = new Properties();
		InputStream inputStream = BaselineExperimentGenerator.class.getClassLoader().getResourceAsStream(pathToClone + inputName);
		if (inputStream != null)
			sourceProp.load(inputStream);

		for (JenaEventType engine : JenaEventType.values()) {
			for (Reasoner reasoner : Reasoner.values()) {

				if (reasoner.equals(Reasoner.FULL)) {
					continue;
				}
				String engineName = engine.toString().toLowerCase();
				String reasonerName = reasoner.name();
				for (int rsp_events_in_window = 1; rsp_events_in_window < 10000; rsp_events_in_window *= 10) {
					for (int init_size = 1; init_size <= 10000; init_size *= 10) {
						for (String kind : new String[] { "memory", "latency" }) {
							for (int i = 0; i < 5; i++) {
								outputName = kind.toUpperCase() + "_INIT" + init_size + "_EW_" + rsp_events_in_window + "_" + engine + "_" + reasoner
										+ "_EXEN";
								String rootPath = "./" + pathToClone + "baseline/constant/";

								String pathname = rootPath + engineName + "/I" + init_size + "/EW" + rsp_events_in_window + "/" + "/" + kind + "/";

								File folder = new File(rootPath + "experiments/" + reasonerName + "/" + kind + "/");
								folder.mkdirs();
								folder = new File(pathname);
								folder.mkdirs();

								String outputFileName = outputName + i + ".properties";
								File f = new File(pathname + outputFileName);
								if (!f.exists())
									f.createNewFile();

								prop = (Properties) sourceProp.clone();

								experimentProperties(i, kind.toUpperCase(), "memory".equals(kind));

								timeProperties();

								engineProperties(engine.toString(), reasonerName);

								eventsProperties(mode, init_size, rsp_events_in_window);

								// for (Enumeration propertyNames = sourceProp.propertyNames();
								// propertyNames.hasMoreElements();) {
								// Object key = propertyNames.nextElement();
								// dest_prop.put(key, sourceProp.get(key));
								// }

								OutputStream out = new FileOutputStream(f);
								prop.store(out, outputName + i);

								// Big folder for scripting
								prop.store(new FileOutputStream(
										new File(rootPath + "experiments/" + reasonerName + "/" + kind + "/" + outputFileName)), outputName + i);
							}
						}
						experimentNumber++;
					}
				}
			}
		}
	}

	private static void eventsProperties(FlowRateProfile mode, int init_size, int rsp_events_in_winodow) {

		prop.setProperty("max_event_stream", "5000");
		prop.setProperty("rsp_events_in_window", rsp_events_in_winodow + "");
		prop.setProperty("init_size", init_size + "");
		switch (mode) {
			case CONSTANT:
				prop.setProperty("streaming_mode", "CONSTANT");
				prop.setProperty("x_size", "0");
				prop.setProperty("y_size", "0");
				break;
			default:
				break;
		}
	}

	private static void experimentProperties(int exe, String type, Boolean memory) {
		prop.setProperty("experiment_name", "RHODF EXPERIMENTS");
		prop.setProperty("experiment_number", (experimentNumber) + "");
		prop.setProperty("execution_number", exe + "");
		prop.setProperty("experiment_date", dt.format(experimentDate));

		prop.setProperty("experiment_type", type);

		prop.setProperty("result_log_enabled", "false");
		prop.setProperty("latency_log_enabled", "true");
		prop.setProperty("memory_log_enabled", memory.toString());
		prop.setProperty("save_abox_log", "false");

		prop.setProperty("input_file", "BIG_FILE.nt");
	}

	private static void timeProperties() {
		prop.setProperty("external_time_control_on", "true");
		prop.setProperty("time_strategy", "NAIVE");
	}

	private static void engineProperties(String eventType, String reasoner) {
		prop.setProperty("cep_event_type", eventType);
		prop.setProperty("jena_current_reasoner", reasoner);
	}
}
