package it.polimi.processing.events.results;

import it.polimi.processing.collector.data.CSV;
import it.polimi.processing.collector.data.CollectableData;
import it.polimi.processing.collector.data.TriG;
import it.polimi.processing.events.TripleContainer;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result implements EventResult {

	private String id;
	private Set<TripleContainer> statements;
	private long eventNumber;
	private long timestamp;
	private boolean abox;

	@Override
	public CollectableData getTrig() {
		String key;
		if (id == null) {
			key = "<http://example.org/" + eventNumber + ">";
		} else {
			key = this.id;
		}
		return new TriG(key, statements);
	}

	@Override
	public CollectableData getCSV() {
		String s = "<http://example.org/" + eventNumber + ">";
		return new CSV(s);
	}

	public int size() {
		return statements.size();
	}

}
