package it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations;

import it.polimi.heaven.core.ts.events.TripleContainer;
import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.CityBenchUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

import com.hp.hpl.jena.vocabulary.RDF;

@Data
public abstract class SensorObservation implements Serializable {
	private static final long serialVersionUID = 1L;
	public Date sysTimestamp;
	public Date obTimeStamp;
	public String obId;
	public String streamID;

	public SensorObservation(Date obTimeStamp, String obId, String streamID) {
		this.obTimeStamp = obTimeStamp;
		this.obId = obId;
		this.sysTimestamp = new Date();
		this.streamID = streamID;
	}

	public Set<TripleContainer> getTriples() {
		Set<TripleContainer> s = new HashSet<TripleContainer>();
		String id = CityBenchUtils.defaultPrefix + obId + UUID.randomUUID();
		TripleContainer observation = new TripleContainer(id, RDF.type.toString(), CityBenchUtils.ssnPrefix + "Observation");
		TripleContainer observedBy = new TripleContainer(id, CityBenchUtils.ssnPrefix + "observedBy", CityBenchUtils.defaultPrefix + streamID);
		s.add(observedBy);
		s.add(observation);
		return s;
	};

}