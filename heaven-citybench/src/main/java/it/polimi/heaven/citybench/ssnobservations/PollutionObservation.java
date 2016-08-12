package it.polimi.heaven.citybench.ssnobservations;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.insight_centre.aceis.io.rdf.RDFFileManager;

import java.util.List;
import java.util.UUID;

//Pollution Data ozone,particullate_matter,carbon_monoxide,sulfure_dioxide,nitrogen_dioxide,longitude,latitude,timestamp

@Data
@EqualsAndHashCode(callSuper = true)
public class PollutionObservation extends SensorObservation {

	private static final long serialVersionUID = 1L;

	private double ozone, particullate_matter, carbon_monoxide, sulfure_dioxide, nitrogen_dioxide;
	private double lon, lat;

	private double api;

	private List<String> payloads;

	public PollutionObservation(String obId, String streamID, long obTimeStamp, double ozone, double particullate_matter, double carbon_monoxide,
			double sulfure_dioxide, double nitrogen_dioxide, double lon, double lat, List<String> payloads, String serviceID) {
		super(obTimeStamp, obId, streamID, serviceID);
		this.ozone = ozone;
		this.particullate_matter = particullate_matter;
		this.carbon_monoxide = carbon_monoxide;
		this.sulfure_dioxide = sulfure_dioxide;
		this.nitrogen_dioxide = nitrogen_dioxide;
		this.lon = lon;
		this.lat = lat;
		this.payloads = payloads;



		for (String payload : payloads) {

			String[] payload_elements = payload.split("\\|");
			String uri = RDFFileManager.defaultPrefix + obId + UUID.randomUUID();

			Resource observation = model.createResource(uri);
			observation.addProperty(RDF.type, model.createResource(RDFFileManager.ssnPrefix + "Observation"));
			observation.addProperty(model.createProperty(RDFFileManager.ssnPrefix + "observedBy"), model.createResource(serviceID));

			Property hasValue = model.createProperty(RDFFileManager.saoPrefix + "hasValue");

			String prop = payload_elements[2];
			observation.addProperty(model.createProperty(RDFFileManager.ssnPrefix + "observedProperty"), model.createResource(prop));

			observation.addLiteral(hasValue, getApi());
		}

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
