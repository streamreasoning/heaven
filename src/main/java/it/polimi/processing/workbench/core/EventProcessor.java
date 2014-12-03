package it.polimi.processing.workbench.core;

public interface EventProcessor<Event> {

	public abstract boolean process(Event event);

	public abstract boolean processDone();

}
