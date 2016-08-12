package it.polimi.heaven.citybench.ssnobservations;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.RDF;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.insight_centre.aceis.io.rdf.RDFFileManager;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.List;
import java.util.UUID;

@Data()
@EqualsAndHashCode(callSuper = false)
public class AarhusParkingObservation extends SensorObservation {
    private static final long serialVersionUID = 1L;
    private int totalspaces, vehiclecount;
    private String garageCode;
    private List<String> payloads;

    public AarhusParkingObservation(int totalspaces, int vehiclecount, String garageCode, List<String> payloads, long obTimeStamp, String obId,
                                    String streamID, String serviceID) {
        super(obTimeStamp, obId, streamID, serviceID);
        this.totalspaces = totalspaces;
        this.vehiclecount = vehiclecount;
        this.garageCode = garageCode;
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

            observation.addLiteral(hasValue, totalspaces - vehiclecount);
        }

    }

    public int getVacancies() {
        return (totalspaces - vehiclecount);
    }

}
