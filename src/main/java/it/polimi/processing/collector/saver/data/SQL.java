package it.polimi.processing.collector.saver.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SQL implements CollectableData {

	private final String s;

	@Override
	public String getData() {
		return s;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
