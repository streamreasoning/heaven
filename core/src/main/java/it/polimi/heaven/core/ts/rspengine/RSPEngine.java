package it.polimi.heaven.core.ts.rspengine;

import it.polimi.heaven.core.ts.EventProcessor;
import it.polimi.heaven.core.ts.events.engine.Stimulus;

import javax.sound.midi.Receiver;

/**
 * @author Riccardo
 * 
 */
public interface RSPEngine extends EventProcessor<Stimulus> {

	public void startProcessing();

	public void stopProcessing();

	public void registerQuery(Query q);

	public void registerReceiver(Receiver r);
}
