package it.polimi.utils;

import it.polimi.properties.GetPropertyValues;

public class ExecutionEnvirorment {

	public static boolean memoryAnalysisEnabled() {
		return GetPropertyValues.getBooleanProperty("memory_log_enabled");
	}

}
