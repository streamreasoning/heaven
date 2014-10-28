package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization.ResearchGroup;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.work.Research;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class ResearchProject extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResearchProject() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#researchProject", "http://swat.cse.lehigh.edu/onto/univ-bench.owl#ResearchGroup",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Research", ResearchGroup.class, Research.class);
	}

	public ResearchProject(String property, String domainValue, String rangeValue, Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
