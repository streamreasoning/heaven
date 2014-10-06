package it.polimi.output.result;

import it.polimi.teststand.events.EventResult;
import it.polimi.teststand.events.Experiment;

import java.util.Set;

public interface Storable {

	public EventResult toEventResult(Experiment e);
	public Set<String[]> getTriples();
}
