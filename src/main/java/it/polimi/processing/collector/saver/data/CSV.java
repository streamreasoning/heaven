package it.polimi.processing.collector.saver.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CSV implements CollectableData {
	private String s;

	@Override
	public String getData() {
		return s;
	}

	@Override
	public CollectableData append(String c) {
		return new CSV(s + "," + c);
	}

}
