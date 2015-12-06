package it.polimi.heaven.core.teststand.streamer;

import it.polimi.heaven.core.teststand.events.engine.Stimulus;
import it.polimi.heaven.core.teststand.events.heaven.HeavenInput;

public interface Encoder {

	public Stimulus[] encode(HeavenInput e);

}
