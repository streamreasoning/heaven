package it.polimi.processing.collector.saver.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TriG implements CollectableData {
	private String s;

	@Override
	public String getData() {
		return s;

	}

}
