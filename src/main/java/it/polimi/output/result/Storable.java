package it.polimi.output.result;

import it.polimi.teststand.events.TestResultEvent;
import it.polimi.teststand.events.Experiment;

import java.util.Set;

public interface Storable {

	public TestResultEvent toEventResult(Experiment e);
	public Set<String[]> getTriples();
}
