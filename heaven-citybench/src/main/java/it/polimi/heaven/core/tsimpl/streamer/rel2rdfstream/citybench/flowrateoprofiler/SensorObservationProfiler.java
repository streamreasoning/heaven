package it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.flowrateoprofiler;

import it.polimi.heaven.core.ts.events.engine.Stimulus;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.FlowRateProfiler;
import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations.SensorObservation;

import java.util.List;

public class SensorObservationProfiler implements FlowRateProfiler<Stimulus, SensorObservation> {

	private Stimulus s;
	private List<String> streams;
	private int experimentNumber;
	private int eventNumber;

	public SensorObservationProfiler(List<String> streams) {
		this.streams = streams;
		this.eventNumber = 0;

	}

	@Override
	public Stimulus build() {
		return s;
	}

	@Override
	public boolean isReady() {
		return s.getEventNumber() < eventNumber;
	}

	@Override
	public boolean append(SensorObservation so) {
		s = new Stimulus();
		s.rebuild(so.getObId(), so.getService(), so.getTriples(), eventNumber, experimentNumber, so.getObTimeStamp().getTime());
		s.setInputTimestamp(so.getSysTimestamp().getTime());
		eventNumber++;
		return true;
	}

}
