package it.polimi.utils;

public class DatabaseUtils {

	public static final String EXPERIMENT_TABLE = "CREATE TABLE IF NOT EXISTS EXPERIMENT " + "(" + " EXP_ID       TEXT                 NOT NULL, "
			+ "	TS_INIT      TEXT                 NOT NULL, " + "	TS_END       TEXT                 NOT NULL, "
			+ " ENGINE       TEXT   			  NOT NULL, " + " INPUT_FILE   TEXT   			  NOT NULL, " + " RESULT_FILE  TEXT                 NOT NULL, "
			+ " LOG_FILE     TEXT                 NOT NULL, " + " PRIMARY      KEY( EXP_ID,TS_INIT )        ) ";
	public static final String EXPERIMENT_INSERT = "INSERT INTO EXPERIMENT ( EXP_ID, TS_INIT, TS_END, ENGINE, INPUT_FILE,RESULT_FILE,LOG_FILE ) ";
	public static final String COMPARATION_TABLE = "CREATE TABLE IF NOT EXISTS COMPARATION " + "(" + " EXP_ID       TEXT                 NOT NULL, "
			+ "	EVENT_ID     TEXT                 NOT NULL, " + "	SOUND        INTEGER              NOT NULL, "
			+ " COMPLETE     INTEGER   	          NOT NULL, " + " MEMORY       REAL                 NOT NULL, "
			+ " LATENCY      INTEGER              NOT NULL, " + " PRIMARY      KEY( EXP_ID,EVENT_ID )       ) ";
	public static final String COMPARATION_INSERT = "INSERT INTO COMPARATION (EXP_ID, EVENT_ID, SOUND, COMPLETE,MEMORY, LATENCY) ";

}
