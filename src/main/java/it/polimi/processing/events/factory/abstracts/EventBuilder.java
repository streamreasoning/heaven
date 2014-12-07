package it.polimi.processing.events.factory.abstracts;

import it.polimi.processing.events.TripleContainer;

import java.util.Set;

public interface EventBuilder<T> {

	public T getEvent();

	public boolean canSend();

	public boolean append(Set<TripleContainer> triple);

	public boolean append(TripleContainer triple);

}
