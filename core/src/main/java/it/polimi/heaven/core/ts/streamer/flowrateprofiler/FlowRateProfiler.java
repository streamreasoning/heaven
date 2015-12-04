package it.polimi.heaven.core.ts.streamer.flowrateprofiler;


public interface FlowRateProfiler<T, E> {

	public T build();

	/**
	 * @return true if the built event can be sent
	 */
	public boolean isReady();

	/**
	 * @param triple
	 *            a container for an RDF Triple
	 * @return true if the appending phase happens successfully
	 */
	public boolean append(E triple);

}
