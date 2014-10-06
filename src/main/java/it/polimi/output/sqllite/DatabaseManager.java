package it.polimi.output.sqllite;

import java.sql.SQLException;

public abstract class DatabaseManager {
	@SuppressWarnings("unused")
	private final String realTable = "CREATE TABLE EXPERIMENT " + "("
			+ " EXP1_ID        TEXT    NOT NULL, "
			+ " EXP2_ID        TEXT    NOT NULL, "
			+ " TS       	   TEXT    NOT NULL, "
			+ " ABOX_IN        TEXT    NOT NULL, "
			+ " ABOX_CL        TEXT    NOT NULL, "
			+ " PRIMARY KEY(EXP1_ID, EXP2_ID, TS)" + ")";
	@SuppressWarnings("unused")
	private final String triple = "CREATE TABLE IF NOT EXISTS EXPERIMENT "
			+ "(" + " TS       TEXT    PRIMARY KEY  NOT NULL,"
			+ " S        TEXT    NOT NULL, " + " P        TEXT    NOT NULL, "
			+ " O        TEXT    NOT NULL " + ")";

	protected String sql = "CREATE TABLE IF NOT EXISTS EXPERIMENT " + "("
			+ " EXP_ID       TEXT                 NOT NULL,"
			+ "	TS_INIT     TEXT                 NOT NULL,"
			+ "	TS_END     TEXT                 NOT NULL,"
			+ " INPUT_FILE    TEXT   			     NOT NULL,"
			+ " RESULT_FILE    TEXT                 NOT NULL, "
			+ " FILE_LOG_FOLDER    TEXT                 NOT NULL, "
			+ " PRIMARY KEY(EXP_ID,TS_INIT)  )";

	public abstract long init() throws ClassNotFoundException, SQLException;

	public abstract long close() throws ClassNotFoundException, SQLException;

	public abstract boolean executeSql(String sql);

}
