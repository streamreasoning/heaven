package rdf.museo.ihneritance.nogenerics.esper;

import java.util.Set;

import org.reflections.Reflections;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class ReflectionTest {

	public static void main(String[] args) {
		Reflections reflections = new Reflections(
				"rdf.museo.ihneritance.nogenerics.ontology.classes");

		Set<Class<? extends RDFResource>> allClasses = reflections
				.getSubTypesOf(RDFResource.class);

		int index=0;
		for (Class<? extends RDFResource> class1 : allClasses) {
				if("FullProfessor0/Publication8".toLowerCase().contains(class1.getSimpleName().toLowerCase())){
					if(index<"FullProfessor0/Publication8".toLowerCase().indexOf(class1.getSimpleName().toLowerCase()))
						index = "FullProfessor0/Publication8".toLowerCase().indexOf(class1.getSimpleName().toLowerCase());
				}
				
		}
		System.out.println("FullProfessor0/Publication8".substring(index)+" "+index);
		

	}

}
