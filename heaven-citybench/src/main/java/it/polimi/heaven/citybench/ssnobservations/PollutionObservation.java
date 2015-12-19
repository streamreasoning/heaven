package it.polimi.heaven.citybench.ssnobservations;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.insight_centre.aceis.io.rdf.RDFFileManager;

import com.hp.hpl.jena.rdf.model.Property;

//Pollution Data ozone,particullate_matter,carbon_monoxide,sulfure_dioxide,nitrogen_dioxide,longitude,latitude,timestamp

@Data
@EqualsAndHashCode(callSuper = true)
public class PollutionObservation extends SensorObservation {

	private static final long serialVersionUID = 1L;

	private double ozone, particullate_matter, carbon_monoxide, sulfure_dioxide, nitrogen_dioxide;
	private double lon, lat;

	private double api;

	private String payload;

	public PollutionObservation(String obId, String streamID, long obTimeStamp, double ozone, double particullate_matter, double carbon_monoxide,
			double sulfure_dioxide, double nitrogen_dioxide, double lon, double lat, String payload, String serviceID) {
		super(obTimeStamp, obId, streamID, serviceID);
		this.ozone = ozone;
		this.particullate_matter = particullate_matter;
		this.carbon_monoxide = carbon_monoxide;
		this.sulfure_dioxide = sulfure_dioxide;
		this.nitrogen_dioxide = nitrogen_dioxide;
		this.lon = lon;
		this.lat = lat;
		this.payload = payload;

		Property hasValue = model.createProperty(RDFFileManager.saoPrefix + "hasValue");
		observation.addLiteral(hasValue, getApi());
	}

	public double getApi() {
		api = ozone;
		if (this.particullate_matter > this.api)
			api = this.particullate_matter;
		if (this.carbon_monoxide > this.api)
			this.api = this.carbon_monoxide;
		if (this.sulfure_dioxide > this.api)
			this.api = this.sulfure_dioxide;
		if (this.nitrogen_dioxide > this.api)
			this.api = this.nitrogen_dioxide;

		return api;
	}
}
