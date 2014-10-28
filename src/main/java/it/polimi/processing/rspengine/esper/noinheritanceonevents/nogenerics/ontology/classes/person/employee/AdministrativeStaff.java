package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Employee;

public class AdministrativeStaff extends Employee {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdministrativeStaff(String object) {
		super(object);
	}

	public AdministrativeStaff() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#AdministrativeStaff");
	}
}
