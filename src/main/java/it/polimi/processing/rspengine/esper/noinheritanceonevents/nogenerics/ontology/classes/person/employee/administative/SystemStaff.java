package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.administative;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.AdministrativeStaff;

public class SystemStaff extends AdministrativeStaff {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemStaff(String object) {
		super(object);
	}

	public SystemStaff() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#SystemsStaff");
	}
}
