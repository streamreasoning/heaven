package rdf.museo.ihneritance.interfaces.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.interfaces.rdfs.RDFClass;
import rdf.museo.ihneritance.interfaces.rdfs.RDFResource;

public class Person extends RDFClass implements Serializable, RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Person(String name) {
		super(Person.class, name);
	}

	public Person(Class<? extends Person> clazz, String name) {
		super(clazz, name);
	}

	public String getName() {
		return getValue();
	}

	public void setName(String name) {
		setValue(name);
	}

	@Override
	public String toString() {
		return "Person " + getValue();
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

}
