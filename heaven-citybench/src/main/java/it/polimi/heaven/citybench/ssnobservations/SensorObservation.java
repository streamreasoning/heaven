package it.polimi.heaven.citybench.ssnobservations;

import it.polimi.heaven.core.teststand.data.Line;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

import org.insight_centre.aceis.io.rdf.RDFFileManager;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

@Data
public abstract class SensorObservation implements Serializable, Line {
	private static final long serialVersionUID = 1L;
	protected long sysTimestamp;
	protected long obTimeStamp;
	protected String obId;
	protected String streamID;
	protected String serviceID;
	protected Resource observation;
	protected Model model;

	public SensorObservation(long obTimeStamp, String obId, String streamID, String serviceID) {
		this.obTimeStamp = obTimeStamp;
		this.obId = obId;
		this.sysTimestamp = System.currentTimeMillis();
		this.streamID = streamID;
		this.serviceID = serviceID;
		this.model = ModelFactory.createDefaultModel();
		this.observation = model.createResource(RDFFileManager.defaultPrefix + obId + UUID.randomUUID());
		this.observation.addProperty(RDF.type, model.createResource(RDFFileManager.ssnPrefix + "Observation"));
		this.observation.addProperty(model.createProperty(RDFFileManager.ssnPrefix + "observedBy"), serviceID);

	}

	public Model toModel() {
		return model;
	}

}
