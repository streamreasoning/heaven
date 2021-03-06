package it.polimi.heaven.citybench.ssnobservations;
import it.polimi.rdf.Line;
import lombok.Data;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.Serializable;

@Data
public abstract class SensorObservation implements Serializable, Line {
	private static final long serialVersionUID = 1L;
	protected long sysTimestamp;
	protected long obTimeStamp;
	protected String obId;
	protected String streamID;
	protected String serviceID;

	protected Model model;

	public SensorObservation(long obTimeStamp, String obId, String streamID, String serviceID) {
		this.obTimeStamp = obTimeStamp;
		this.obId = obId;
		this.sysTimestamp = System.currentTimeMillis();
		this.streamID = streamID;
		this.serviceID = serviceID;
		this.model = ModelFactory.createDefaultModel();

	}

	public Model toModel() {
		return model;
	}

}
