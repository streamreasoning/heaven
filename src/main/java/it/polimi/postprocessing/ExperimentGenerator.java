package it.polimi.postprocessing;

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

	public static void main(String[] args) throws IOException {
		String pathToClone = "properties/";
		String inputName = "default_teststand.properties";
		String outputName = "smpl_tevent_latency_1000_10_EN";
		sourceProp = new Properties();
		InputStream inputStream = ExperimentGenerator.class.getClassLoader().getResourceAsStream(pathToClone + inputName);
		if (inputStream != null)
			sourceProp.load(inputStream);

		for (int i = 0; i < 5; i++) {
			File f = new File("src/main/resources/" + pathToClone + outputName + i + ".properties");
			if (!f.exists())
				f.createNewFile();

			Properties dest_prop = (Properties) sourceProp.clone();
			for (Enumeration propertyNames = sourceProp.propertyNames(); propertyNames.hasMoreElements();) {
				Object key = propertyNames.nextElement();
				dest_prop.put(key, sourceProp.get(key));
			}

			dest_prop.setProperty("execution_number", i + "");
			OutputStream out = new FileOutputStream(f);
			dest_prop.store(out, "This is an optional header comment string");
		}

	}
}
