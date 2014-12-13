package it.polimi.processing.services;

import it.polimi.processing.collector.saver.data.SQL;
import it.polimi.utils.FileUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.extern.log4j.Log4j;

@Log4j
public class SQLListeService {

	private static final String JDBC_SQLITE = "jdbc:sqlite:";
	private static final String name = "obqaatcep.db"; // TODO aggiungere alle properties
	private static final String JDBC_SQLITE_OBQAATCEP_DB = JDBC_SQLITE + FileUtils.DATABASEPATH + name;
	private static final String ORG_SQLITE_JDBC = "org.sqlite.JDBC";
	private static Connection c;
	private static Statement stmt = null;

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

	static {
		try {
			Class.forName(ORG_SQLITE_JDBC);
			c = DriverManager.getConnection(JDBC_SQLITE_OBQAATCEP_DB);
			log.info("Database successfully opened");
			stmt = c.createStatement();
			stmt.executeUpdate(EXPERIMENT_TABLE);
			log.info("Experiment Table successfully created ");
			stmt.executeUpdate(COMPARATION_TABLE);
			stmt.close();
			log.info("Comparison Table successfully created ");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	public static boolean write(SQL d) {
		try {
			stmt = c.createStatement();
			String sql = d.getData();
			log.debug(sql);
			if (sql != null && stmt != null && c != null && !c.isClosed()) {
				stmt.executeUpdate(EXPERIMENT_INSERT + sql);
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
