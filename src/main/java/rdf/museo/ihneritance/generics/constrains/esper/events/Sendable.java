package rdf.museo.ihneritance.generics.constrains.esper.events;

import rdf.museo.ihneritance.generics.rdfs.RDFProperty;
import rdf.museo.ihneritance.generics.rdfs.RDFResource;

public interface Sendable<S extends RDFResource, P extends RDFProperty<?, ?>, O extends RDFResource> {

	public S getS();

	public P getP();

	public O getC();

}
