package it.polimi.heaven.core.ts.streamer;

import it.polimi.heaven.core.ts.events.engine.Stimulus;
import it.polimi.heaven.core.ts.events.heaven.HeavenInput;

public interface Encoder {

	public Stimulus[] encode(HeavenInput e);

}
