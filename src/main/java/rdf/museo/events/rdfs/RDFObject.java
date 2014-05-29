package rdf.museo.events.rdfs;

public abstract class RDFObject {

	protected String object;

	public RDFObject(String object) {
		this.object = object;
	}

	public abstract Class<?> getType();
}
