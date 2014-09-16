package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.organization.ResearchGroup;
import rdf.museo.ihneritance.nogenerics.ontology.classes.work.Research;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class ResearchProject extends RDFProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResearchProject() {
		super(ResearchGroup.class, Research.class, "has as a research project");
	}

	public ResearchProject(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
