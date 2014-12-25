package it.polimi.processing.events.results;

import it.polimi.processing.collector.data.CollectableData;
import it.polimi.processing.events.interfaces.Event;

public interface EventResult extends Event {

	public CollectableData getTrig();

	public CollectableData getCSV();

}
