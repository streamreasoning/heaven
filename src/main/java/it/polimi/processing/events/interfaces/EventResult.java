package it.polimi.processing.events.interfaces;

import it.polimi.processing.collector.saver.data.CollectableData;

public interface EventResult extends Event {

	public CollectableData getTrig();

	public CollectableData getCSV();

}
