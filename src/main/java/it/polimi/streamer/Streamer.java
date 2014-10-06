package it.polimi.streamer;

import it.polimi.teststand.enums.ExecutionStates;
import it.polimi.teststand.events.StreamingEvent;
import it.polimi.teststand.exceptions.WrongStatusTransitionException;
import it.polimi.teststand.utils.RDFSUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Streamer<T extends EventProcessor<StreamingEvent>> {

	public static final boolean USEDATATYPEPROPERTIES = false;
	public static final boolean USESHEMAPROPERTIES = false;

	private static final boolean debug = true;

	private T engine;

	public Streamer(T model) {
		this.engine = model;
	}

	/**
	 * Apro il file, apro una connesione al database e salvo il file di output e
	 * l'inizio dell'esperimento
	 * 
	 * @param status
	 **/
	public void stream(ExecutionStates status, BufferedReader br)
			throws IOException {

		if (!ExecutionStates.READY.equals(status)) {
			throw new WrongStatusTransitionException("Not Ready");
		} else {
			int count = 0, lineNumber = 0;
			String line;
			Set<String[]> eventTriples;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				String[] s = Parser.parseTriple(line,
						"src/main/resources/data/", false);
				if (s.length > 3) {
					throw new IllegalArgumentException("Too much arguments");
				}

				s[0] = s[0].replace("<", "");
				s[0] = s[0].replace(">", "");

				s[1] = s[1].replace("<", "");
				s[1] = s[1].replace(">", "");

				s[2] = s[2].replace("<", "");
				s[2] = s[2].replace(">", "");

				if (RDFSUtils.isDatatype(s[1]) || RDFSUtils.isType(s[1])) {
					continue;
				} else {
					System.out.println(line);
					status = ExecutionStates.SENDING;
					debug("SEND NEW EVENT");
					eventTriples = new HashSet<String[]>();
					eventTriples.add(s);
					if (engine.sendEvent(new StreamingEvent(eventTriples,
							lineNumber))) {
						count++;
					} else {
						debug("Not Saved");
					}
					status = ExecutionStates.READY;
				}

			}
			debug("streamer count: " + count);
			br.close();

		}
	}

	private void debug(String s) {
		if (debug)
			System.err.println(s);
	}

}