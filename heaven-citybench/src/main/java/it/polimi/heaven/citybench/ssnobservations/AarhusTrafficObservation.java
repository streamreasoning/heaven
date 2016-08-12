package it.polimi.heaven.citybench.ssnobservations;

import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j;

import org.insight_centre.aceis.io.rdf.RDFFileManager;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

//Fields  status,avgMeasuredTime,avgSpeed,extID,medianMeasuredTime,TIMESTAMP,vehicleCount,_id,REPORT_ID
@Data
@Log4j
@EqualsAndHashCode(callSuper = false)
public class AarhusTrafficObservation extends SensorObservation {

	private static final long serialVersionUID = 1L;

	private String status;
	private double avgMeasuredTime, avgSpeed, medianMeasuredTime;
	private int vehicleCount, extID, reportID;
	private double congestion_level;
	private double estimatedTime;
	private List<String> payloads;

	public AarhusTrafficObservation(String obId, String streamID, long obTimeStamp, String status, double avgMeasuredTime, double avgSpeed,
			double medianMeasuredTime, int vehicleCount, int extID, int reportID, double congestion_level, double estimatedTime,
			List<String> payloads, String serviceID) {
		super(obTimeStamp, obId, streamID, serviceID);
		this.status = status;
		this.avgMeasuredTime = avgMeasuredTime;
		this.avgSpeed = avgSpeed;
		this.medianMeasuredTime = medianMeasuredTime;
		this.vehicleCount = vehicleCount;
		this.extID = extID;
		this.reportID = reportID;
		this.payloads = payloads;
		this.congestion_level = congestion_level;
		this.estimatedTime = estimatedTime;

		for (String payload : payloads) {

			String[] payload_elements = payload.split("\\|");
			String uri = RDFFileManager.defaultPrefix + obId + UUID.randomUUID();

			Resource observation = model.createResource(uri);
			observation.addProperty(RDF.type, model.createResource(RDFFileManager.ssnPrefix + "Observation"));
			observation.addProperty(model.createProperty(RDFFileManager.ssnPrefix + "observedBy"), model.createResource(serviceID));

			Property hasValue = model.createProperty(RDFFileManager.saoPrefix + "hasValue");

			String prop = payload_elements[2];
			observation.addProperty(model.createProperty(RDFFileManager.ssnPrefix + "observedProperty"), model.createResource(prop));

			if (payload.contains("AvgSpeed"))
				observation.addLiteral(hasValue, this.avgSpeed);

			else if (payload.contains("VehicleCount")) {
				observation.addLiteral(hasValue, this.vehicleCount);
			} else if (payload.contains("MeasuredTime"))
				observation.addLiteral(hasValue, this.avgMeasuredTime);
			else if (payload.contains("EstimatedTime"))
				observation.addLiteral(hasValue, this.estimatedTime);
			else if (payload.contains("CongestionLevel")) {
				observation.addLiteral(hasValue, this.congestion_level);
			}
			log.debug(uri + " " + payload_elements[0]);
			log.debug(model);
		}

	}

}
