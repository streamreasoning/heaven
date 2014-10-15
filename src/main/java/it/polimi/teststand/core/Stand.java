package it.polimi.teststand.core;

import it.polimi.enums.ExecutionStates;
import it.polimi.events.Experiment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Stand {
	protected ExecutionStates status;
	protected Experiment currentExperiment;


}
