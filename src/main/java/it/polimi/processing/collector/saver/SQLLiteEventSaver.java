package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.utils.DatabaseUtils;
import it.polimi.utils.FileUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class SQLLiteEventSaver implements EventSaver {

	private static final String JDBC_SQLITE = "jdbc:sqlite:";
	private final String name;
	private final String JDBC_SQLITE_OBQAATCEP_DB;
	private static final String ORG_SQLITE_JDBC = "org.sqlite.JDBC";
	private long timestamp;
	private Connection c;
	private Statement stmt = null;
	private ExecutionStates status;

	/**
	 * Initialize the mains tables of the system
	 * 
	 * @see it.polimi.output.filesystem.DatabaseManager#init()
	 **/
	public SQLLiteEventSaver() {
		this.name = "obqaatcep.db";
		this.JDBC_SQLITE_OBQAATCEP_DB = JDBC_SQLITE + FileUtils.DATABASEPATH + name;
	}

	public SQLLiteEventSaver(String name) {
		this.name = name;
		this.JDBC_SQLITE_OBQAATCEP_DB = JDBC_SQLITE + FileUtils.DATABASEPATH + name;
	}

	public SQLLiteEventSaver(String name, String path) {
		this.name = name;
		this.JDBC_SQLITE_OBQAATCEP_DB = JDBC_SQLITE + path + name;
	}

	@Override
	public ExecutionStates init() {
		timestamp = System.currentTimeMillis();
		try {
			Class.forName(ORG_SQLITE_JDBC);
			c = DriverManager.getConnection(JDBC_SQLITE_OBQAATCEP_DB);
			log.info("Opened database successfully");
			stmt = c.createStatement();
			stmt.executeUpdate(DatabaseUtils.EXPERIMENT_TABLE);
			stmt.executeUpdate(DatabaseUtils.COMPARATION_TABLE);
			stmt.close();
			log.info("Table created successfully");
			status = ExecutionStates.READY;
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
			status = ExecutionStates.ERROR;
		} catch (SQLException e) {
			log.error(e.getMessage());
			status = ExecutionStates.ERROR;
		}

		return this.status;

	}

	@Override
	public ExecutionStates close() {
		log.info("Stop the Database System");
		try {
			queryAllComp();
			c.close();
			log.info("Closed database successfully");
			status = ExecutionStates.CLOSED;
		} catch (SQLException e) {
			status = ExecutionStates.ERROR;
			log.error(e.getMessage());
		}
		return status;

	}

	public void queryAll() throws SQLException {
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM EXPERIMENT;");
		while (rs.next()) {
			log.info("EXPERIMENT_ID = " + rs.getString("EXP_ID"));
			log.info("TS_INIT = " + Long.parseLong(rs.getString("TS_INIT")));
			log.info("TS_END = " + Long.parseLong(rs.getString("TS_END")));
			log.info("INPUT_FILE = " + rs.getString("INPUT_FILE"));
			log.info("RESULT_FILE = " + rs.getString("RESULT_FILE"));
			log.info("FILE_LOG_FOLDER" + rs.getString("FILE_LOG_FOLDER"));
		}
		rs.close();
		stmt.close();
	}

	public void queryAllComp() throws SQLException {
		log.info("QUERY ALL");
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM COMPARATION;");
		while (rs.next()) {
			log.debug("EXPERIMENT_ID = " + rs.getString("EXP_ID"));
			log.debug("EVENT_ID = " + rs.getString("EVENT_ID"));
			log.debug("SOUND = " + rs.getBoolean("SOUND"));
			log.debug("COMPLETE = " + rs.getBoolean("COMPLETE"));
			log.debug("MEMORY = " + rs.getDouble("MEMORY"));
			log.debug("LATENCY = " + rs.getLong("LATENCY"));

		}
		rs.close();
		stmt.close();
	}

	@Override
	public boolean save(CollectableData d, String where) {
		if (ExecutionStates.READY.equals(status) && d != null) {
			String sql = DatabaseUtils.EXPERIMENT_INSERT + d.getData();
			log.debug(" EVENT SQL VALUE " + sql);
			try {
				stmt = c.createStatement();
				if (sql != null && stmt != null && c != null && !c.isClosed()) {

					stmt.executeUpdate(sql);
					stmt.close();
					return true;
				}
			} catch (SQLException ex) {
				log.error(ex.getMessage());
			}
		} else {
			log.warn("Not ready to write db");
		}
		return false;
	}

}
