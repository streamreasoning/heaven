package it.polimi.heaven.core.ts;

public interface EventProcessor<Event> {

	public abstract boolean process(Event event);

}