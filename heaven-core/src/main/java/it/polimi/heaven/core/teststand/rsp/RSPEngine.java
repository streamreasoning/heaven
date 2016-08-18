package it.polimi.heaven.core.teststand.rsp;

import it.polimi.heaven.core.teststand.EventProcessor;
import it.polimi.heaven.core.teststand.rsp.data.Stimulus;
import it.polimi.heaven.core.teststand.rsp.querying.ContinousQueryExecution;
import it.polimi.heaven.core.teststand.rsp.querying.Query;

import javax.sound.midi.Receiver;

/**
 * @author Riccardo
 * 
 */
public interface RSPEngine extends EventProcessor<Stimulus> {

	public void startProcessing();

	public void stopProcessing();

	public ContinousQueryExecution registerQuery(Query q);

	public void registerReceiver(Receiver r);

	// TODO is reasoning enabled
	// TODO is external time control enabled
}
