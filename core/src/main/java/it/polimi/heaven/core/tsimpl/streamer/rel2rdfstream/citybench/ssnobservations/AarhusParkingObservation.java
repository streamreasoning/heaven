package it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations;

import it.polimi.heaven.core.ts.events.TripleContainer;
import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.CityBenchUtils;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data()
@EqualsAndHashCode(callSuper = false)
public class AarhusParkingObservation extends SensorObservation {
	private static final long serialVersionUID = 1L;
	private int totalspaces, vehiclecount;
	private String garageCode, streetName;

	public AarhusParkingObservation(int totalspaces, int vehiclecount, String garageCode, String streetName, Date obTimeStamp, String obId,
			String streamID) {
		super(obTimeStamp, obId, streamID);
		this.totalspaces = totalspaces;
		this.vehiclecount = vehiclecount;
		this.garageCode = garageCode;
	}

	public int getVacancies() {
		return (totalspaces - vehiclecount);
	}

	@Override
	public Set<TripleContainer> getTriples() {
		Set<TripleContainer> s = super.getTriples();
		String id = CityBenchUtils.defaultPrefix + obId + UUID.randomUUID();
		TripleContainer hasValue = new TripleContainer(id, CityBenchUtils.saoHasValue, "\"" + getVacancies() + "\"" + "^^xsd:integer");
		TripleContainer foi = new TripleContainer(id, CityBenchUtils.ssnPrefix + "featureOfInterest", CityBenchUtils.defaultPrefix + garageCode);
		s.add(hasValue);
		s.add(foi);
		return s;
	}
}
