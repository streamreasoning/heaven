package it.polimi.processing.events.profiler.abstracts;

import it.polimi.processing.events.TripleContainer;

public interface FlowRateProfiler<T> {

	public T getEvent();

	public boolean isReady();

	public boolean append(TripleContainer triple);

}
