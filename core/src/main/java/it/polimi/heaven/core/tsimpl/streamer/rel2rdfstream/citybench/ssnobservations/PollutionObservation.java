package it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations;

import it.polimi.heaven.core.ts.events.TripleContainer;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

//Pollution Data ozone,particullate_matter,carbon_monoxide,sulfure_dioxide,nitrogen_dioxide,longitude,latitude,timestamp

@Data
@EqualsAndHashCode(callSuper = true)
public class PollutionObservation extends SensorObservation {

	private static final long serialVersionUID = 1L;

	private String obId;
	private Date sysTimestamp;
	private Date obTimeStamp;

	private double ozone, particullate_matter, carbon_monoxide, sulfure_dioxide, nitrogen_dioxide;
	private double lon, lat;

	public PollutionObservation(String obId, String streamID, Date obTimeStamp, double ozone, double particullate_matter, double carbon_monoxide,
			double sulfure_dioxide, double nitrogen_dioxide, double lon, double lat) {
		super(obTimeStamp, obId, streamID);
		this.ozone = ozone;
		this.particullate_matter = particullate_matter;
		this.carbon_monoxide = carbon_monoxide;
		this.sulfure_dioxide = sulfure_dioxide;
		this.nitrogen_dioxide = nitrogen_dioxide;
		this.lon = lon;
		this.lat = lat;
	}

	@Override
	public Set<TripleContainer> getTriples() {
		// TODO Auto-generated method stub
		return null;
	}

}
