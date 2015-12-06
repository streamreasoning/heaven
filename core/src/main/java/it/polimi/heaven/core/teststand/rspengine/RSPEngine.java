package it.polimi.heaven.core.teststand.rspengine;

import it.polimi.heaven.core.teststand.EventProcessor;
import it.polimi.heaven.core.teststand.events.engine.Stimulus;

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
