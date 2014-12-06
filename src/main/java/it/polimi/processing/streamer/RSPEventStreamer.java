package it.polimi.processing.streamer;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.factory.abstracts.EventBuilder;
import it.polimi.processing.workbench.core.EventProcessor;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class RSPEventStreamer implements Streamer<RSPEvent> {

	protected final EventProcessor<RSPEvent> processor;
	protected EventBuilder<RSPEvent> builder;
	protected ExecutionState status;
	protected int eventLimit;

	@Override
	public RSPEvent createEvent(Set<TripleContainer> triple, int eventNumber, int experimentNumber) {
		return builder.getEvent();
	}
}
