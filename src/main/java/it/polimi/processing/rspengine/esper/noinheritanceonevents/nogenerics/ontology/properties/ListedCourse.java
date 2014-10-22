package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.work.Course;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;

public class ListedCourse extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6861836888833237331L;

	public ListedCourse() {
		super(Person.class, Course.class, "has a master degree from");
	}

}
