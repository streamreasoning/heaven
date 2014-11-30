package it.polimi.processing.rspengine.esper.commons.listener;

import it.polimi.processing.collector.saver.data.CSV;
import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.events.interfaces.EventResult;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result implements EventResult {

	private Set<String[]> statements;
	private long from, to;
	private long timestamp;

	@Override
	public CollectableData getTrig() {
		return new TriG("<http://example.org/" + from + "/" + to + ">", statements);
	}

	@Override
	public CollectableData getCSV() {
		String s = "<http://example.org/" + from + "/" + to + ">";
		return new CSV(s);
	}

}
