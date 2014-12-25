package it.polimi.services.system;

import it.polimi.main.BaselineMain;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetPropertyValues {
	private static Properties prop = new Properties();
	public static String inputPropertiesFileName = null;
	private static final String defaultPropertiesFileName = "properties/default_teststand.properties";
	private static final DateFormat DT = new SimpleDateFormat("yyyy-MM-dd");

	public static final String DEFAULT_INPUT_FILE = "input_file";

	static {
		InputStream inputStream;
		try {
			String fileToFind;
			if (BaselineMain.INPUT_PROPERTIES != null) {
				fileToFind = BaselineMain.INPUT_PROPERTIES;
				log.info("Input Properties Accepted");
				inputStream = new FileInputStream(fileToFind);
			} else {
				fileToFind = defaultPropertiesFileName;
				inputStream = GetPropertyValues.class.getClassLoader().getResourceAsStream(defaultPropertiesFileName);
			}

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				log.error("property file '" + fileToFind + "' not found in the classpath");
			}

		} catch (IOException e) {
			log.error(e.getMessage());
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

	public static Date getDateProperty(String propertyName) {
		String property = getProperty(propertyName).trim();
		log.debug("Property: [" + propertyName + "] Value [" + property + "]");

		try {
			Date parse = DT.parse(prop.getProperty(propertyName));
			return property != null ? parse : new Date();
		} catch (ParseException e) {
			log.error(e.getMessage());
			return new Date();
		}
	}

	public static <T> T getTypedProperty(String propertyName, Class<T> type) {
		String property = getProperty(propertyName).trim();
		log.debug("Property: [" + propertyName + "] Value [" + property + "]");
		return property != null ? type.cast(prop.getProperty(propertyName)) : null;

	}

	public static <T extends Enum<T>> T getEnumProperty(Class<T> enumType, String propertyName) {
		String property = prop.getProperty(propertyName).trim();
		log.debug("Property: [" + propertyName + "] Value [" + property + "]");
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
