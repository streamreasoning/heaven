package it.polimi.utils;

public class CommonInputOrder {

	public static final int FILE = 0;
	public static final int EVENTS = 1 + FILE;
	public static final int EXPERIMENT_TYPE = 1 + EVENTS;
	public static final int EXPERIMENTNUMBER = 1 + EXPERIMENT_TYPE;
	public static final int EXECUTIONNUMBER = 1 + EXPERIMENTNUMBER;
	public static final int CURRENT_ENGINE = 1 + EXECUTIONNUMBER;
	public static final int COMMENTS = 1 + CURRENT_ENGINE;
	public static final int EVENTBUILDER = 1 + COMMENTS;
	public static final int INITSIZE = 1 + EVENTBUILDER;
	public static final int X = 1 + INITSIZE;
	public static final int Y = 1 + X;

}
