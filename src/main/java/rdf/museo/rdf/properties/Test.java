package rdf.museo.rdf.properties;

import rdf.museo.ontology.Artist;
import rdf.museo.ontology.Painter;
import rdf.museo.rdf.RDFClass;
import rdf.museo.rdf.RDFResource;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RDFResource r = new RDFResource("prova");
		RDFResource r1 = new RDFClass<RDFClass>(RDFClass.class);
		RDFResource r2 = new Artist("artist");
		RDFClass<RDFResource> r3 = new RDFClass<RDFResource>(RDFResource.class);
		RDFClass<Artist> r4 = new RDFClass<Artist>(Artist.class);
		RDFResource r5 = new Painter("painter");
		Painter r6 = new Painter("painter");
		System.out.println(r.getClass());
		System.out.println(r1.getClass());
		System.out.println(r2.getClass());
		System.out.println(r3.getClass());
		System.out.println(r4.getClass());
		System.out.println(r5.getClass());
		System.out.println(r6.getClass());
		System.out.println();
		System.out.println(r1.getClass().getSuperclass());
		System.out.println(r2.getClass().getSuperclass());
		System.out.println(r3.getClass().getSuperclass());
		System.out.println(r4.getClass().getSuperclass());
		System.out.println(r5.getClass().getSuperclass());

		System.out.println(r6 instanceof RDFResource);
		System.out.println(r6 instanceof Artist);
		System.out.println(r6 instanceof Painter);

	}
}
