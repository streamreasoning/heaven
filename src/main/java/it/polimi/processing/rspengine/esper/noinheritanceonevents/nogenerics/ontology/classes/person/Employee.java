package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person;

public class Employee extends Person {

	private static final long serialVersionUID = 1L;

	public Employee(String object) {
		super(object);
	}

	public Employee() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Employee");
	}

}
