package it.polimi.processing.streamer;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.workbench.core.EventProcessor;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public abstract class RSPEventStreamer implements Streamer<RSPEvent> {

	protected final EventProcessor<RSPEvent> processor;
	@Setter
	protected ExecutionState status;

	@Override
	public RSPEvent createEvent(String key, Set<String[]> triple, int eventNumber, int experimentNumber) {
		return new RSPEvent(key, triple, eventNumber, experimentNumber);
	}
}
