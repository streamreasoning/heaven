package it.polimi.processing.events;

import it.polimi.processing.collector.saver.data.CSV;
import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.events.interfaces.EventResult;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result implements EventResult {

	private Set<TripleContainer> statements;
	private long from, to;
	private long timestamp;
	private double memory;
	private Boolean completeSMPL, soundSMPL, completeRHODF, soundRHODF;
	private boolean abox;

	@Override
	public CollectableData getTrig() {
		return new TriG("<http://example.org/" + from + "/" + to + ">", statements);
	}

	@Override
	public CollectableData getCSV() {
		String s = "<http://example.org/" + from + "/" + to + ">";
		return new CSV(s);
	}

	public Result(Set<TripleContainer> statements, long from, long to, long timestamp, double memory, boolean abox) {
		this.statements = statements;
		this.from = from;
		this.to = to;
		this.timestamp = timestamp;
		this.memory = memory;
		this.abox = abox;

	}
}
