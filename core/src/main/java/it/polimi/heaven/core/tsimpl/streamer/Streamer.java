package it.polimi.heaven.core.tsimpl.streamer;

import it.polimi.heaven.core.ts.EventProcessor;
import it.polimi.heaven.core.ts.events.engine.Stimulus;
import it.polimi.heaven.core.ts.events.heaven.HeavenEvent;
import it.polimi.heaven.core.ts.streamer.Encoder;

import java.io.FileReader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * 
 * General implementation of the Streamer used by the TestStand It process an
 * Experiment event trough the method process(Experiment e). It must be
 * initialized before processing to stop the processing until next
 * initialization. It builds an flow rate according to the provided
 * FlowRateProfiler and send events to the next provided EventProcessor in
 * CTEvent format
 * 
 * A limitation in the number of generated events can be set up in construction
 * 
 * 
 * 
 * @author Riccardo
 * 
 */
@Log4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Streamer {
	@Setter
	protected int eventLimit;
	@Setter
	protected Encoder encoder;
	protected EventProcessor<Stimulus> engine;
	@Setter
	protected EventProcessor<HeavenEvent> collector;

	public Streamer(int eventLimit, Encoder encoder, EventProcessor<Stimulus> engine) {
		this.eventLimit = eventLimit;
		this.encoder = encoder;
		this.engine = engine;
	}

	public Streamer(Encoder encoder, EventProcessor<Stimulus> engine) {
		this.eventLimit = 1000;
		this.encoder = encoder;
		this.engine = engine;
	}

	public abstract boolean start(FileReader in);

	@SuppressWarnings("unchecked")
	public boolean setEngine(EventProcessor<?> ep) {
		try {
			this.engine = (EventProcessor<Stimulus>) ep;
			return true;
		} catch (ClassCastException cce) {
			log.error(cce.getMessage());
			return false;
		}
	}
}
