package it.polimi.utils;

public class BaseLineInputOrder {

	public static final int CLI = 0;
	public static final int EXPERIMENTNUMBER = CLI + 1;
	public static final int EXECUTIONNUMBER = EXPERIMENTNUMBER + 1;
	public static final int JENA_CURRENTENGINE = 1 + EXECUTIONNUMBER;
	public static final int CURRENTREASONER = 1 + JENA_CURRENTENGINE;
	public static final int EVENTBUILDER = 1 + CURRENTREASONER;
	public static final int COMMENTS = 1 + EVENTBUILDER;
	public static final int INITSIZE = 1 + COMMENTS;
	public static final int HEIGHT = 1 + INITSIZE;
	public static final int WIDTH = 1 + HEIGHT;

}
