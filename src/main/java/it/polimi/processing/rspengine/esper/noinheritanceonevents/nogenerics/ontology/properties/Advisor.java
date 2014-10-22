package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication.Publication;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.work.Research;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;

public class Advisor extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Advisor() {
		super(Publication.class, Research.class, "was written by");
	}

}
