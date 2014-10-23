package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RDFProperty implements Serializable {

	static final long serialVersionUID = 1L;
	protected String property, rangeValue, domainValue;
	protected Class<? extends RDFResource> domain;
	protected Class<? extends RDFResource> range;

	public RDFClass getRange() {
		return new RDFClass(range, rangeValue);
	}

	public RDFClass getDomain() {
		return new RDFClass(domain, domainValue);
	}

	@Override
	public String toString() {
		return property;
	}

}
