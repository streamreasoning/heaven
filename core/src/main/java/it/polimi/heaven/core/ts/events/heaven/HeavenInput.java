package it.polimi.heaven.core.ts.events.heaven;

import it.polimi.heaven.core.ts.data.TripleContainer;
import it.polimi.heaven.core.ts.events.engine.Stimulus;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeavenInput implements HeavenEvent {

	private String id;
	private String stream_name;
	private int eventNumber;
	private int experimentNumber;
	private long creation_timestamp;

	private long stimuli_application_timestamp;
	private long encoding_start_time;
	private long encoding_end_time;
	private double memory_usage_before_processing;
	private double engine_size_inmemory;
	private double teststand_size_inmemory;
	private double streamer_size_inmemory;

	private Stimulus[] stimuli;
	private Set<TripleContainer> eventTriples;

	public int size() {
		return eventTriples.size();
	}

	public HeavenInput(String id, String stream_name, int eventNumber, int experimentNumber, long stimuli_application_timestamp,
			Set<TripleContainer> eventTriples) {
		this.id = id;
		this.stream_name = stream_name;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.stimuli_application_timestamp = stimuli_application_timestamp;
		this.creation_timestamp = System.currentTimeMillis();
		this.eventTriples = eventTriples;
	}

	public HeavenInput rebuild(String id, String stream_name, int eventNumber, int experimentNumber, long stimuli_application_timestamp,
			Set<TripleContainer> eventTriples) {
		this.id = id;
		this.stream_name = stream_name;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.stimuli_application_timestamp = stimuli_application_timestamp;
		this.creation_timestamp = System.currentTimeMillis();
		this.eventTriples = eventTriples;
		return this;
	}

}
