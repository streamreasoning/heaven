package it.polimi.heaven.core.ts.rspengine;

import it.polimi.heaven.core.ts.events.engine.Response;

import java.util.List;

public interface Decoder<T> {

	public List<T> decode(Response r);
}
