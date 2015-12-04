package it.polimi.heaven.core.ts.streamer;

import java.io.FileReader;

public interface Streamer {

	public boolean start(FileReader in);
}
