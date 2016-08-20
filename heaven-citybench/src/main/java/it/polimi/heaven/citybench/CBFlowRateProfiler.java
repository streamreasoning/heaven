package it.polimi.heaven.citybench;

import it.polimi.heaven.citybench.ssnobservations.SensorObservation;
import it.polimi.rdf.Line;
import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.streamer.Encoder;
import it.polimi.heaven.core.teststand.streamer.FlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Setter
@Getter
public class CBFlowRateProfiler extends FlowRateProfiler {

	protected int initSize, roundSize, current_heaven_input;
	protected int x, y;
	protected boolean contemporary;
	protected SensorObservation current_observation;
	protected SensorObservation previous_observation;
	private int eventNumber;
	private HeavenInput ret;
	long start_time;

	public CBFlowRateProfiler(ParsingTemplate parser, Encoder encoder, int experiment_number, long start_time) {
		super(experiment_number, 0, false, parser, encoder);
		this.parser = parser;
		this.current_heaven_input = 1;
		this.current_assigned_timestamp = 0L;
		this.eventNumber = 0;
		this.start_time = start_time;
		this.event_id = "<http://example.org/" + experiment_number + "/";
		e = new HeavenInput(event_id + current_assigned_timestamp + ">", "", current_heaven_input, experiment_number, current_assigned_timestamp,
				new HashSet<Line>());
	}

	public HeavenInput build() {
		ret.setId(event_id + previous_observation.getObTimeStamp() + ">");
		ret.setEncoding_start_time(System.currentTimeMillis());
		ret.setStimuli(encoder.encode(ret));
		ret.setEncoding_end_time(System.currentTimeMillis());
		eventNumber++;
		return ret;
	}

	public boolean isReady() {
		return contemporary;
	}

	public boolean append(String lineString) {

		this.previous_observation = current_observation;
		current_observation = (SensorObservation) parser.parse(lineString);
		if (previous_observation != null && (contemporary = current_observation.getObTimeStamp() > previous_observation.getObTimeStamp())) {
			log.info("Timestamp Mismatch, line [" + lineString + "] has a timestamp greater than [" + previous_observation.getObTimeStamp() + "]");
			HashSet<Line> lines = new HashSet<Line>();
			lines.add(current_observation);
			this.ret = e;
			e = new HeavenInput(event_id + current_observation.getObTimeStamp() + ">", "", eventNumber, experiment_number,
					current_observation.getObTimeStamp(), lines);
		} else {
			log.info("Line to Append: " + lineString);
			Set<Line> lines = e.getLines();
			lines.add(current_observation);
			e.setLines(lines);
			log.info("The new Observation was appended, current timestamp is [" + current_observation.getObTimeStamp() + "]");
		}
		return contemporary;
	}

	public long getTimestamp() {
		return current_assigned_timestamp;
	}

}
