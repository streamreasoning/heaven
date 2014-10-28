package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

@Getter
@Setter
@EqualsAndHashCode
public class RDFProperty implements Serializable {

	static final long serialVersionUID = 1L;
	protected String property, rangeValue, domainValue;
	protected Class<? extends RDFResource> domain;
	protected Class<? extends RDFResource> range;

	public RDFProperty(String property) {
		this.property = property;
		this.domainValue = this.rangeValue = "http://www.w3.org/2000/01/rdf-schema#Resource";
		this.range = this.domain = RDFResource.class;
	}

	public RDFClass getRange() {
		Logger.getRootLogger().debug(property + " range " + rangeValue);
		return new RDFClass(range, rangeValue);
	}

	public RDFClass getDomain() {
		Logger.getRootLogger().debug(property + " domain " + domainValue);
		return new RDFClass(domain, domainValue);
	}

	@Override
	public String toString() {
		return property;
	}

	public RDFProperty(String property, String rangeValue, String domainValue, Class<? extends RDFResource> domain, Class<? extends RDFResource> range) {
		this.property = property;
		this.rangeValue = (rangeValue == null || rangeValue.isEmpty()) ? "http://www.w3.org/2000/01/rdf-schema#Resource" : rangeValue;
		this.domainValue = (domainValue == null || domainValue.isEmpty()) ? "http://www.w3.org/2000/01/rdf-schema#Resource" : domainValue;
		this.domain = (domain != null) ? domain : RDFResource.class;
		this.range = (range != null) ? range : RDFResource.class;
	}

}
