package it.polimi.processing.events.factory.abstracts;

import it.polimi.processing.events.TripleContainer;

public interface FlowRateProfiler<T> {

	public T getEvent();

	public boolean canSend();

	public boolean append(TripleContainer triple);

}