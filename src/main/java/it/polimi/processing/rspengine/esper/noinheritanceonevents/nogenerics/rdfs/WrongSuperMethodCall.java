package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class WrongSuperMethodCall extends RuntimeException {

	private final Exception e;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2570638070134192531L;

}
