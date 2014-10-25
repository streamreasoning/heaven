package it.polimi.processing.teststand.core;

import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.Experiment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Stand {
	protected ExecutionStates status;
	protected Experiment currentExperiment;


}
