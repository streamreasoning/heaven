package it.polimi.processing.rspengine.shared.events;

public class EsperUtils {
	public static final int WINDOW_SIZE = 1000;
	public static final int OUTPUT_RATE = 1000;
	public static final String JENA_INPUT_QUERY = " select irstream * from TEvent.win:time_batch(" + WINDOW_SIZE + "msec) output snapshot every "
			+ OUTPUT_RATE + " msec";
}
