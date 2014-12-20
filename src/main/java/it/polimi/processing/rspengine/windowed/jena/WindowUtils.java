package it.polimi.processing.rspengine.windowed.jena;

import it.polimi.processing.system.GetPropertyValues;

public class WindowUtils {

	public static final int beta = 100;

	private static int expectedRSPEvents = GetPropertyValues.getIntegerProperty("rsp_events_in_window");
	public static final int omega = ((beta * expectedRSPEvents) + 1);

	public static final String JENA_INPUT_QUERY_SNAPTSHOT = " select irstream * from TEvent.win:time(" + omega + " msec) output snapshot every "
			+ beta + " msec";

	public static final String JENA_INPUT_QUERY_INCREMENTAL = " select irstream * from TEvent.win:time(" + omega + "msec) output snapshot every "
			+ beta + " msec";
}
