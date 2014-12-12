package it.polimi.processing.collector.saver.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SQL extends WritableData {

	private final String s;

	@Override
	public String getData() {
		return s;
	}

	@Override
	public CollectableData append(String c) {
		return new SQL(s + c);
	}

}
