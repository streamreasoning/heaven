package rdf.museo.ihneritance.nogenerics.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class Person extends RDFResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Person(String name) {
		super(name);
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (!(obj instanceof RDFResource))
			return false;
		else {
			RDFResource other = (RDFResource) obj;
			return getValue().equals(other.getValue());
		}
	}

	@Override
	public RDFClass getSuper() {
		System.out.println("Person " + this.getClass().getSuperclass());
		return new RDFClass(this.getClass().getSuperclass());
	}

}
