package rdf.museo.rdf;

public class TypeOf extends RDFProperty<RDFResource, RDFResource> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeOf() {
		super(new RDFClass(RDFResource.class), new RDFClass(RDFClass.class),
				"typeOf");
	}

}
