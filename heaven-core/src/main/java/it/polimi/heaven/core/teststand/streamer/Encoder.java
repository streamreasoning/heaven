package it.polimi.heaven.core.teststand.streamer;

import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.rspengine.events.Stimulus;

public interface Encoder {

	public Stimulus[] encode(HeavenInput e);

}
