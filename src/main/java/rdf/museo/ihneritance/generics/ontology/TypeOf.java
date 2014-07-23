package rdf.museo.ihneritance.generics.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.generics.rdfs.RDFClass;
import rdf.museo.ihneritance.generics.rdfs.RDFProperty;
import rdf.museo.ihneritance.generics.rdfs.RDFResource;

public class TypeOf extends RDFProperty<RDFResource, RDFClass<?>> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeOf() {
		super(new RDFClass<RDFResource>(RDFResource.class), new RDFClass(
				RDFClass.class), "typeOf");
	}

	public TypeOf(RDFResource s, RDFClass o) {
		super(s, o, "typeOf");
	}

	@Override
	public RDFClass getRange() {
		return new RDFClass(RDFClass.class);
	}

}
