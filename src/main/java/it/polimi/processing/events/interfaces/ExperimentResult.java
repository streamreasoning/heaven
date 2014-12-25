package it.polimi.processing.events.interfaces;

import it.polimi.processing.collector.data.CollectableData;

public interface ExperimentResult extends Event {
	public CollectableData getSQL();
}
