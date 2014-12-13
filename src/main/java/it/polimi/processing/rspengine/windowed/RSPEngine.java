package it.polimi.processing.rspengine.windowed;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.workbench.core.EventProcessor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RSPEngine implements EventProcessor<RSPEvent> {

	protected ExecutionState status;
	protected EventProcessor<Event> next;
	protected String name;

	@Getter
	protected RSPEvent currentEvent = null;

	public RSPEngine(String name, EventProcessor<Event> next) {
		this.next = next;
		this.name = name;
	}

	public abstract ExecutionState init();

	public abstract ExecutionState close();

	public abstract ExecutionState startProcessing();

	public abstract ExecutionState stopProcessing();

	public abstract int getEventNumber();

	public abstract void progress(int i);

}
