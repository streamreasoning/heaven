package it.polimi.processing.services.system;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutionEnvirorment {

	public static final boolean memoryLogEnabled = GetPropertyValues.getBooleanProperty("memory_log_enabled");
	public static final boolean latencyLogEnabled = GetPropertyValues.getBooleanProperty("latency_log_enabled");
	public static final boolean finalresultTrigLogEnabled = GetPropertyValues.getBooleanProperty("result_log_enabled");
	public static final boolean aboxLogEnabled = GetPropertyValues.getBooleanProperty("save_abox_log");
}
