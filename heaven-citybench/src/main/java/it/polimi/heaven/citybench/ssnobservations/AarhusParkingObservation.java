package it.polimi.heaven.citybench.ssnobservations;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.insight_centre.aceis.io.rdf.RDFFileManager;

import com.hp.hpl.jena.rdf.model.Property;

@Data()
@EqualsAndHashCode(callSuper = false)
public class AarhusParkingObservation extends SensorObservation {
	private static final long serialVersionUID = 1L;
	private int totalspaces, vehiclecount;
	private String garageCode, payload;

	public AarhusParkingObservation(int totalspaces, int vehiclecount, String garageCode, String payload, Date obTimeStamp, String obId,
			String streamID, String serviceID) {
		super(obTimeStamp, obId, streamID, serviceID);
		this.totalspaces = totalspaces;
		this.vehiclecount = vehiclecount;
		this.garageCode = garageCode;
		this.payload = payload;

		observation.addProperty(model.createProperty(RDFFileManager.ssnPrefix + "observedProperty"), model.createResource(payload));
		Property hasValue = model.createProperty(RDFFileManager.saoPrefix + "hasValue");
		observation.addLiteral(hasValue, totalspaces - vehiclecount);

	}

	public int getVacancies() {
		return (totalspaces - vehiclecount);
	}

}
