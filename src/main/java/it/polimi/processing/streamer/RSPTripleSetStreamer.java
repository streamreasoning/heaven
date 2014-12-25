package it.polimi.processing.streamer;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.ets.core.EventProcessor;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.factory.abstracts.EventBuilder;
import it.polimi.processing.events.interfaces.Event;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class RSPTripleSetStreamer implements Streamer<RSPTripleSet> {

	protected final EventProcessor<Event> processor;
	protected EventBuilder<RSPTripleSet> builder;
	protected ExecutionState status;
	protected int eventLimit;

	@Override
	public RSPTripleSet createEvent(Set<TripleContainer> triple, int eventNumber, int experimentNumber) {
		return builder.getEvent();
	}
}
