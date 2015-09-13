package it.polimi.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.log4j.Log4j;

@Log4j
public class SQLListeService {
	private static final DateFormat DT = new SimpleDateFormat("yyyy-MM-dd");

	private static final String JDBC_SQLITE = "jdbc:sqlite:";
	private static final String name = "experiments" + (DT.format(new Date()))
			+ ".db";
	private static final String JDBC_SQLITE_OBQAATCEP_DB = JDBC_SQLITE
			+ "data/output/database/" + name;
	private static final String ORG_SQLITE_JDBC = "org.sqlite.JDBC";
	private static Connection c;
	private static Statement stmt = null;

	public static final String EXPERIMENT_TABLE = "CREATE TABLE IF NOT EXISTS EXPERIMENT "
			+ "("
			+ " EXP_ID       TEXT                 NOT NULL, "
			+ "	TS_INIT      TEXT                 NOT NULL, "
			+ "	TS_END       TEXT                 NOT NULL, "
			+ " ENGINE       TEXT   			  NOT NULL, "
			+ " INPUT_FILE   TEXT   			  NOT NULL, "
			+ " RESULT_FILE  TEXT                 NOT NULL, "
			+ " LOG_FILE     TEXT                 NOT NULL, "
			+ " PRIMARY      KEY( EXP_ID,TS_INIT )        ) ";
	public static final String BASELINE_EXPERIMENT_TABLE = "CREATE TABLE IF NOT EXISTS BASELINE_EXPERIMENTS "
			+ "("
			+ " EXP_ID         TEXT                 NOT NULL, "
			+ " EXP_NUM        INTEGER                 NOT NULL, "
			+ " EXP_DATE       TEXT                 NOT NULL, "
			+ "	TS_INIT        TEXT                 NOT NULL, "
			+ "	TS_END         TEXT                 NOT NULL, "
			+ " ENGINE         TEXT   			  NOT NULL, "
			+ " EXP_TYPE       TEXT                 NOT NULL, "
			+ " TIMECONTROL    TEXT                 NOT NULL, "
			+ " INPUT_FILE     TEXT   			  NOT NULL, "
			+ " OUTPUT_FILE  TEXT                 NOT NULL, "
			+ " PRIMARY      KEY( EXP_ID,EXP_NUM,TS_INIT )        ) ";
	public static final String BASELINE_INSERT = "INSERT INTO BASELINE_EXPERIMENTS ( EXP_ID, EXP_NUM, EXP_DATE, TS_INIT, TS_END, ENGINE, EXP_TYPE, TIMECONTROL,INPUT_FILE,OUTPUT_FILE) ";
	public static final String EXPERIMENT_INSERT = "INSERT INTO EXPERIMENT ( EXP_ID, TS_INIT, TS_END, ENGINE, INPUT_FILE,RESULT_FILE,LOG_FILE ) ";
	public static final String COMPARATION_TABLE = "CREATE TABLE IF NOT EXISTS COMPARATION "
			+ "("
			+ " EXP_ID       TEXT                 NOT NULL, "
			+ "	EVENT_ID     TEXT                 NOT NULL, "
			+ "	SOUND        INTEGER              NOT NULL, "
			+ " COMPLETE     INTEGER   	          NOT NULL, "
			+ " MEMORY       REAL                 NOT NULL, "
			+ " LATENCY      INTEGER              NOT NULL, "
			+ " PRIMARY      KEY( EXP_ID,EVENT_ID )       ) ";
	public static final String COMPARATION_INSERT = "INSERT INTO COMPARATION (EXP_ID, EVENT_ID, SOUND, COMPLETE,MEMORY, LATENCY) ";


	public static boolean openDatabase(String dbPath, String dbName) {

		try {
			Class.forName(ORG_SQLITE_JDBC);
			c = DriverManager.getConnection(JDBC_SQLITE+dbPath+dbName);
			log.info("Database successfully opened");
			stmt = c.createStatement();
			stmt.executeUpdate(EXPERIMENT_TABLE);
			log.info("Experiment Table successfully created ");
			stmt.executeUpdate(BASELINE_EXPERIMENT_TABLE);
			log.info("Baseline Table successfully created ");
			stmt.close();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
			return false;
		} catch (SQLException e) {
			log.error(e.getMessage());
			return false;
		}
		return true;

	}

	public static boolean write(String sql) {
		try {
			stmt = c.createStatement();
			log.debug(sql);
			if (sql != null && stmt != null && c != null && !c.isClosed()) {
				stmt.executeUpdate(sql);
				stmt.close();
				return true;
			}
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		}
		return false;
	}

	public static boolean close() {
		try {
			c.close();
			log.info("Database successfully closed");
			return true;
		} catch (SQLException e) {
			log.error(e.getMessage());
			return false;
		}
	}

}
