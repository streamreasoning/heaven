package it.polimi.heaven.citybench.ssnobservations;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.insight_centre.aceis.io.rdf.RDFFileManager;

import com.hp.hpl.jena.rdf.model.Property;

@Data
@EqualsAndHashCode(callSuper = false)
public class WeatherObservation extends SensorObservation {

	private static final long serialVersionUID = 1L;
	private String obId, payload;
	private Date sysTimestamp;
	private Date obTimeStamp;

	private double humidity, windSpeed, temperature;

	public WeatherObservation(String obId, String streamID, Date obTimeStamp, double humidity, double windSpeed, double temperature, String payload,
			String serviceID) {
		super(obTimeStamp, obId, streamID, serviceID);
		this.humidity = humidity;
		this.windSpeed = windSpeed;
		this.temperature = temperature;
		this.payload = payload;

		observation.addProperty(model.createProperty(RDFFileManager.ssnPrefix + "observedProperty"), payload);
		Property hasValue = model.createProperty(RDFFileManager.saoPrefix + "hasValue");
		if (payload.contains("Temperature"))
			observation.addLiteral(hasValue, this.temperature);
		else if (payload.toString().contains("Humidity"))
			observation.addLiteral(hasValue, this.humidity);
		else if (payload.toString().contains("WindSpeed"))
			observation.addLiteral(hasValue, this.windSpeed);
	}

	@Override
	public String[] getFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}
}
