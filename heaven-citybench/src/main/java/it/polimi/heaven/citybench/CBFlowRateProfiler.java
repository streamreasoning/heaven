package it.polimi.heaven.citybench;

import it.polimi.heaven.core.teststand.data.Line;
import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.streamer.Encoder;
import it.polimi.heaven.core.teststand.streamer.FlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;

import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CBFlowRateProfiler extends FlowRateProfiler {

	protected int initSize, roundSize, current_heaven_input;
	protected int x, y;
	protected boolean sizeReached;
	protected Line current_observation;

	public CBFlowRateProfiler(ParsingTemplate parser, Encoder encoder, int experiment_number) {
		super(experiment_number, 0, false, parser, encoder);
		this.parser = parser;
		this.current_heaven_input = 1;
		this.sizeReached = false;
		this.current_assigned_timestamp = 0L;
		this.event_id = "<http://example.org/" + experiment_number + "/";
		e = new HeavenInput(event_id + current_assigned_timestamp + ">", getStreamName(), current_heaven_input, experiment_number,
				current_assigned_timestamp, new HashSet<Line>());
	}

	public HeavenInput build() {
		current_assigned_timestamp += timing;
		e.setEncoding_start_time(System.currentTimeMillis());
		e.setStimuli(encoder.encode(e));
		e.setEncoding_end_time(System.currentTimeMillis());
		return sizeReached ? e : null;
	}

	public boolean isReady() {
		return true;
	}

	public boolean append(String lineString) {
		current_observation = parser.parse(lineString);
		return true;
	}

	public long getTimestamp() {
		return current_assigned_timestamp;
	};

	public String getStreamName() {
		return "lubmEvent";
	}

}
