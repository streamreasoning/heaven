package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFClass;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class TypeOf extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeOf() {
		super("http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
				"http://www.w3.org/2000/01/rdf-schema#Resource",
				"http://www.w3.org/1999/02/22-rdf-schema#Class",
				RDFResource.class, RDFClass.class);
	}
}
