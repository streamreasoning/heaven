package it.polimi.processing.collector.saver.data;

import it.polimi.processing.services.SQLListeService;
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

	@Override
	public boolean save(String where) {
		return SQLListeService.write(this);
	}

}
