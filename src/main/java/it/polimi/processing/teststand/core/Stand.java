package it.polimi.processing.teststand.core;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Stand {
	protected ExecutionState status;
	protected Experiment currentExperiment;

	public BufferedReader getBuffer(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		return new BufferedReader(new FileReader(file));
	}
}
