package rdf.museo.rdf;

public class TypeOf extends RDFProperty<RDFObject> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeOf() {
		super(new RDFClass(RDFObject.class), new RDFClass(RDFObject.class),
				"typeOf");
	}

}
