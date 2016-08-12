package it.polimi.heaven.core.teststand.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Thi class encapsulate the serialization of an RDF triple redefining equals
 * concept
 * 
 * @author Riccardo
 * 
 */
@Getter
@NoArgsConstructor
public final class RDFLine implements Line {

	private final String[] triple = new String[3];

	public RDFLine(String s, String p, String o) {
		this.triple[0] = s;
		this.triple[1] = p;
		this.triple[2] = o;
	}

	public RDFLine(String[] t) {
		this.triple[0] = t[0];
		this.triple[1] = t[1];
		this.triple[2] = t[2];
	}

	@Override
	public int hashCode() {
		int hashCode0 = this.triple[0] != null ? this.triple[0].hashCode() : 0;
		int hashCode1 = this.triple[1] != null ? this.triple[1].hashCode() : 0;
		int hashCode2 = this.triple[2] != null ? this.triple[2].hashCode() : 0;
		return (31 * (hashCode0 + hashCode1 + hashCode2));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RDFLine other = (RDFLine) obj;
		String[] t = other.getTriple();
		return t[0].equals(triple[0]) && t[0].equals(triple[0]) && t[0].equals(triple[0]);
	}

}
