package it.polimi.heaven.lubm;

import it.polimi.heaven.core.teststand.data.Line;
import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.streamer.Encoder;
import it.polimi.heaven.core.teststand.streamer.FlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Setter
@Getter
@Log4j
public abstract class LUBMFlowRateProfiler extends FlowRateProfiler {

	protected FlowRateProfile mode;
	protected int initSize, roundSize, current_heaven_input;
	protected int x, y;
	protected boolean sizeReached;
	protected Line current_line;

	public LUBMFlowRateProfiler(FlowRateProfile mode, ParsingTemplate parser, int x, int y, int initSize, int experiment_number, int timing) {
		this(mode, parser, null, x, y, initSize, experiment_number, timing);
	}

	public LUBMFlowRateProfiler(FlowRateProfile mode, ParsingTemplate parser, Encoder encoder, int x, int y, int initSize, int experiment_number,
			int timing) {
		super(experiment_number, timing, true, parser, encoder);
		this.mode = mode;
		this.parser = parser;
		this.x = x;
		this.y = y;
		this.initSize = roundSize = initSize;
		this.current_heaven_input = 1;
		this.sizeReached = false;
		this.current_assigned_timestamp = 0L;
		this.timing = timing;
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
		return sizeReached;
	}

	public boolean append(String lineString) {
		current_line = parser.parse(lineString);
		if (sizeReached) {
			updateSize();
			Set<Line> set = new HashSet<Line>();
			if (roundSize > 0) {
				current_heaven_input++;
				set.add(current_line);
			}
			e = e.rebuild(event_id + current_heaven_input + ">", getStreamName(), current_heaven_input, experiment_number, getTimestamp(), set);
			log.debug("is Full Event Size [" + e.size() + "] roundSize [" + roundSize + "]");
		} else {
			e.getLines().add(current_line);
			log.debug("NotFull Event Size [" + e.size() + "] roundSize [" + roundSize + "]");
		}

		sizeReached = (e.size() == roundSize);

		return sizeReached;
	}

	public abstract void updateSize();

	public long getTimestamp() {
		return current_assigned_timestamp;
	}

	public String getStreamName() {
		return "lubmEvent";
	}
}
