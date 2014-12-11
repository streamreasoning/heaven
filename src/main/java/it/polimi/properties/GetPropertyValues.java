package it.polimi.properties;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import lombok.extern.log4j.Log4j;

@Log4j
public class GetPropertyValues {
	private static Properties prop = new Properties();
	private static final String propFileName = "properties/config.properties";
	private static final DateFormat DT = new SimpleDateFormat("yyyy-MM-dd");

	public static final String INPUT_FILE = "input_file";

	static {
		InputStream inputStream = GetPropertyValues.class.getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				log.error(e.getMessage());

			}
		} else {
			log.error("property file '" + propFileName + "' not found in the classpath");
		}
	}

	public static String getProperty(String propertyName) {
		log.debug("Property: [" + propertyName + "] Value [" + prop.getProperty(propertyName) + "]");
		return prop.getProperty(propertyName).trim();
	}

	public static Integer getIntegerProperty(String propertyName) {
		String property = getProperty(propertyName).trim();
		log.debug("Property: [" + propertyName + "] Value [" + property + "]");
		return property != null ? Integer.parseInt(property) : null;
	}

	public static Date getDateProperty(String propertyName) throws ParseException {
		String property = getProperty(propertyName).trim();
		log.debug("Property: [" + propertyName + "] Value [" + property + "]");
		return property != null ? DT.parse(prop.getProperty(propertyName)) : null;

	}

	public static <T> T getTypedProperty(String propertyName, Class<T> type) {
		String property = getProperty(propertyName).trim();
		log.debug("Property: [" + propertyName + "] Value [" + property + "]");
		return property != null ? type.cast(prop.getProperty(propertyName)) : null;

	}

	public static <T extends Enum<T>> T getEnumProperty(Class<T> enumType, String propertyName) {
		String property = prop.getProperty(propertyName).trim();
		log.info("Property: [" + propertyName + "] Value [" + property + "]");
		return Enum.valueOf(enumType, property);
	}

	public static boolean getBooleanProperty(String propertyName) {
		String property = prop.getProperty(propertyName).trim();
		log.debug("Property: [" + propertyName + "] Value [" + property + "]");
		return prop.containsKey(propertyName) && Boolean.parseBoolean(property);
	}

	public static boolean contains(String propertyName) {
		log.debug("Property: [" + propertyName + "] Value [" + prop.getProperty(propertyName) + "]");
		return prop.containsKey(propertyName);
	}
}
