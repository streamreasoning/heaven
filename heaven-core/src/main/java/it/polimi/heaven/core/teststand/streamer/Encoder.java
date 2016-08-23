package it.polimi.heaven.core.teststand.streamer;

import it.polimi.streaming.Stimulus;
import it.polimi.heaven.core.teststand.events.HeavenInput;

public interface Encoder {

    Stimulus[] encode(HeavenInput e);

}
