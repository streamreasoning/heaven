package it.polimi.processing.collector.saver.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TXT implements CollectableData {

	private String s;

	@Override
	public String getData() {
		return s;
	}

	@Override
	public CollectableData append(String c) {
		return new TXT(s + " " + c);
	}

}
