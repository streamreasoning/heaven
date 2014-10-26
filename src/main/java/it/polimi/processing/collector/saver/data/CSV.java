package it.polimi.processing.collector.saver.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CSV implements CollectableData {

	private String s, name;

	@Override
	public String getData() {
		return s;
	}

	@Override
	public String getName() {
		return name;
	}

}
