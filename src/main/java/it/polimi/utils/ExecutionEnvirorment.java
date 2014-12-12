package it.polimi.utils;

import it.polimi.properties.GetPropertyValues;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutionEnvirorment {

	private static boolean memoryEnabled;
	static {
		memoryEnabled = GetPropertyValues.getBooleanProperty("memory_log_enabled");
	}

	public static boolean memoryAnalysisEnabled() {
		return memoryEnabled;
	}

}
