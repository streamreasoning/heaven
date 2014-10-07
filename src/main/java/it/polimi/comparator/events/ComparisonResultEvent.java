package it.polimi.comparator.events;

import it.polimi.events.Event;
import it.polimi.output.filesystem.Writable;

public class ComparisonResultEvent extends Event implements Writable {

	private String event_id, experiment_id;
	private long result_timestamp;
	private long event_timestamp;
	private long experiment_timestamp;
	private boolean complete;
	private boolean sound;
	private double memory;
	private long latency;

	public ComparisonResultEvent(String experiment_id, String event_id,
			long event_ts, long experiment_ts,
			boolean complete, boolean sound, double memory, long latency) {
		this.event_id = event_id;
		this.experiment_id = experiment_id;
		this.result_timestamp = System.currentTimeMillis();
		this.event_timestamp = event_ts;
		this.experiment_timestamp = experiment_ts;
		this.complete = complete;
		this.sound = sound;
		this.memory = memory;
		this.latency = latency;
	}

	// (EXP_ID, EVENT_ID, SOUND, COMPLETE,MEMORY, LATENCY)
	public String toString() {
		return "VALUES (" + "'" + experiment_id + "'" + "," + "'" + event_id
				+ "'" + ","  + (sound ? 1 : 0) + "," + 
				+ (complete ? 1 : 0) + "," +memory +  ","
				+ latency +  ");";
	}

	public long getEvent_timestamp() {
		return event_timestamp;
	}

	@Override
	public byte[] getBytes() {
		return toString().getBytes();
	}

	public String getEvent_id() {
		return event_id;
	}

	public long getResultTimestamp() {
		return result_timestamp;
	}

	public long getExperiment_timestamp() {
		return experiment_timestamp;
	}

}
