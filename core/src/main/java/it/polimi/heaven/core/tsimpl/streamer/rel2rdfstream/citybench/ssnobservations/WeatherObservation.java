package it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations;

import it.polimi.heaven.core.ts.events.TripleContainer;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WeatherObservation extends SensorObservation {

	private static final long serialVersionUID = 1L;
	private String obId;
	private Date sysTimestamp;
	private Date obTimeStamp;

	private double humidity, windSpeed, temperature;

	public WeatherObservation(String obId, String streamID, Date obTimeStamp, double humidity, double windSpeed, double temperature) {
		super(obTimeStamp, obId, streamID);
		this.humidity = humidity;
		this.windSpeed = windSpeed;
		this.temperature = temperature;
	}

	@Override
	public Set<TripleContainer> getTriples() {
		// TODO Auto-generated method stub
		return null;
	}
}
