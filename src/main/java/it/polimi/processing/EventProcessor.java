package it.polimi.processing;

public interface EventProcessor<Event> {

	public abstract boolean process(Event event);

	public abstract boolean processDone();

}
