package rdf.museo.propertybased.events;

import rdf.museo.rdf.RDFProperty;
import rdf.museo.rdf.RDFResource;

public interface Sendable<S extends RDFResource, P extends RDFProperty<?, ?>, O extends RDFResource> {

	public S getS();

	public P getP();

	public O getC();

}
