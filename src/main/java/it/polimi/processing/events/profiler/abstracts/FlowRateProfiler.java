package it.polimi.processing.events.profiler.abstracts;

import it.polimi.processing.events.TripleContainer;

public interface FlowRateProfiler<T> {

	public T getEvent();

	/**
	 * @return true if the built event can be sent
	 */
	public boolean isReady();

	/**
	 * @param triple
	 *            a container for an RDF Triple
	 * @return true if the appending phase happens successfully
	 */
	public boolean append(TripleContainer triple);

}
