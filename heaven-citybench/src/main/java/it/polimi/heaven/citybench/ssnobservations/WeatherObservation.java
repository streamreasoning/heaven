package it.polimi.heaven.citybench.ssnobservations;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.insight_centre.aceis.io.rdf.RDFFileManager;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class WeatherObservation extends SensorObservation {

	private static final long serialVersionUID = 1L;
	private List<String> payloads;

	private double humidity, windSpeed, temperature;

	public WeatherObservation(String obId, String streamID, long obTimeStamp, double humidity, double windSpeed, double temperature, List<String> payloads,
			String serviceID) {
		super(obTimeStamp, obId, streamID, serviceID);
		this.humidity = humidity;
		this.windSpeed = windSpeed;
		this.temperature = temperature;
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

            if (payload.contains("Temperature"))
                observation.addLiteral(hasValue, this.temperature);
            else if (payload.toString().contains("Humidity"))
                observation.addLiteral(hasValue, this.humidity);
            else if (payload.toString().contains("WindSpeed"))
                observation.addLiteral(hasValue, this.windSpeed);
        }


	}

}
