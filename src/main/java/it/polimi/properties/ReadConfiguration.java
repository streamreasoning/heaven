package it.polimi.properties;

import java.io.IOException;

public class ReadConfiguration {

	public static void main(String[] args) throws IOException {
		Integer typedProperty;
		typedProperty = GetPropertyValues.getIntegerProperty("experiment");
		System.out.println(GetPropertyValues.getBooleanProperty("external_time_control_on"));

	}
}
