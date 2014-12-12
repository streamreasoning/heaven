package it.polimi.processing.collector;

import it.polimi.processing.collector.saver.data.SQL;
import it.polimi.utils.DatabaseUtils;
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

	static {
		try {
			Class.forName(ORG_SQLITE_JDBC);
			c = DriverManager.getConnection(JDBC_SQLITE_OBQAATCEP_DB);
			log.info("Opened database successfully");
			stmt = c.createStatement();
			stmt.executeUpdate(DatabaseUtils.EXPERIMENT_TABLE);
			stmt.executeUpdate(DatabaseUtils.COMPARATION_TABLE);
			stmt.close();
			log.info("Table created successfully");
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
			log.info(sql);
			if (sql != null && stmt != null && c != null && !c.isClosed()) {
				stmt.executeUpdate(DatabaseUtils.EXPERIMENT_INSERT + sql);
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
			return true;
		} catch (SQLException e) {
			log.error(e.getMessage());
			return false;
		}
	}
}
