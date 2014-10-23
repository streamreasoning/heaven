package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.administative;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.AdministrativeStaff;

public class ClericalStaff extends AdministrativeStaff {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClericalStaff(String object) {
		super(object);
	}

	public ClericalStaff() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#ClericalStaff");
	}
}
