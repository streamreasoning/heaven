package it.polimi.processing.collector.data;

import it.polimi.processing.services.SQLListeService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class SQLStmt extends WritableData {

	private final String s;

	@Override
	public String getData() {
		return s;
	}

	@Override
	public CollectableData append(String c) {
		return new SQLStmt(s + c);
	}

	@Override
	public boolean save(String where) {
		return SQLListeService.write(this);
	}

}
