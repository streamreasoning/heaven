package it.polimi.heaven.core.teststand.streamer;

import it.polimi.heaven.core.teststand.events.HeavenInput;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class FlowRateProfiler {

	protected HeavenInput e;
	protected int current_heaven_input;
	protected boolean ready;
	protected int experiment_number;
	protected long current_assigned_timestamp;
	protected String event_id;

	protected int timing;
	protected boolean assigned_timestamp_enabled;

	protected ParsingTemplate parser;
	protected Encoder encoder;

	public FlowRateProfiler(int experiment_number, int timing, boolean assigned_timestamp_enabled, ParsingTemplate parser, Encoder encoder) {
		this.experiment_number = experiment_number;
		this.timing = timing;
		this.assigned_timestamp_enabled = assigned_timestamp_enabled;
		this.parser = parser;
		this.encoder = encoder;
	}

	public FlowRateProfiler(int experiment_number, int timing, boolean assigned_timestamp_enabled, ParsingTemplate parser) {
		this.experiment_number = experiment_number;
		this.timing = timing;
		this.assigned_timestamp_enabled = assigned_timestamp_enabled;
		this.parser = parser;
	}

	public FlowRateProfiler(int experiment_number, ParsingTemplate parser) {
		this.experiment_number = experiment_number;
		this.parser = parser;
	}

	public HeavenInput build() {
		e.setEncoding_start_time(System.currentTimeMillis());
		e.setStimuli(encoder.encode(e));
		e.setEncoding_end_time(System.currentTimeMillis());
		return ready ? e : null;
	}

	public boolean isReady() {
		return ready;
	}

	public abstract boolean append(String s);

	public long getTimestamp() {
		current_assigned_timestamp += timing;
		return current_assigned_timestamp;
	};

	public abstract String getStreamName();

}
