package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
public class RDFResource implements Serializable {

	protected String value;

	public RDFResource() {
		this.value = "http://www.w3.org/2000/01/rdf-schema#Resource";
	}

	@Override
	public String toString() {
		return value;
	}

	// TODO perche' non e' possibile inserire il parametro?
	public RDFClass getSuper() {
		return new RDFClass(this.getClass(),
				"http://www.w3.org/2000/01/rdf-schema#Resource");// TODO
	};

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (!(obj instanceof RDFResource))
			return false;
		else {
			RDFResource other = (RDFResource) obj;
			return getValue().equals(other.getValue());
		}
	}

}
