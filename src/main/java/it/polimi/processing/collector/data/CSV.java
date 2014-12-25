package it.polimi.processing.collector.saver.data;

import it.polimi.processing.system.ExecutionEnvirorment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class CSV extends WritableData {
	private String s;

	@Override
	public String getData() {
		return s + System.getProperty("line.separator");
	}

	@Override
	public CollectableData append(String c) {
		return new CSV(s + "," + c);
	}

	@Override
	public boolean save(String where) {
		return ExecutionEnvirorment.latencyLogEnabled || ExecutionEnvirorment.memoryLogEnabled ? super.save(where) : false;
	}

}
