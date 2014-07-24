package rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties;

import java.io.Serializable;

import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFClass;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFProperty;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFResource;

public class TypeOf extends RDFProperty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeOf() {
		super(RDFResource.class, RDFClass.class, "TypeOf");
	}

	public TypeOf(Class<? extends RDFResource> domain,
			Class<? extends RDFClass<? extends RDFResource>> range) {
		super(domain, range, "TypeOf");
	}

}
