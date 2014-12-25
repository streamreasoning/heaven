package it.polimi.processing.ets.core;

/**
 * @author Riccardo
 * 
 *         This class represents the abstraction of Event Processing. It is designed as with
 *         split-phases operation to provide the most general behavior
 * 
 * @param <Event>
 *            Message that contains the information
 * 
 * 
 */
public interface EventProcessor<Event> {

	public abstract boolean process(Event event);

	public abstract boolean processDone();

}
