package it.polimi.output.sqllite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManagerImpl extends DatabaseManager {

	private static final String JDBC_SQLITE_OBQAATCEP_DB = "jdbc:sqlite:obqaatcep.db";
	private static final String ORG_SQLITE_JDBC = "org.sqlite.JDBC";
	private long timestamp;
	private Connection c;
	private Statement stmt = null;

	@Override
	public long init() throws ClassNotFoundException, SQLException {
		timestamp = System.currentTimeMillis();
		Class.forName(ORG_SQLITE_JDBC);
		c = DriverManager.getConnection(JDBC_SQLITE_OBQAATCEP_DB);
		System.out.println("Opened database successfully");
		stmt = c.createStatement();
		stmt.executeUpdate(EXPERIMENT_TABLE);
		stmt.executeUpdate(COMPARATION_TABLE);
		stmt.close();
		System.out.println("Table created successfully");
		return timestamp;
	}

	@Override
	public boolean executeSql(String sql) {
		try {
			stmt = c.createStatement();
			if (sql != null && stmt != null && c != null && !c.isClosed()) {
				System.out.println(sql);
				stmt.executeUpdate(sql);
				stmt.close();
			} else {
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long close() throws ClassNotFoundException, SQLException {
		System.out.println("Stop the Database System");
//		stmt = c.createStatement();
//		ResultSet rs = stmt.executeQuery("SELECT * FROM EXPERIMENT;");
//		while (rs.next()) {
//			System.out.println("EXPERIMENT_ID = " + rs.getString("EXP_ID"));
//			System.out.println("TS_INIT = " + Long.parseLong(rs.getString("TS_INIT")));
//			System.out.println("TS_END = " + Long.parseLong(rs.getString("TS_END")));
//			System.out.println("INPUT_FILE = " + rs.getString("INPUT_FILE"));
//			System.out.println("RESULT_FILE = " + rs.getString("RESULT_FILE"));
//			System.out.println("FILE_LOG_FOLDER" + rs.getString("FILE_LOG_FOLDER"));
//			System.out.println();
//		}
//		rs.close();
//		stmt.close();
		c.close();
		System.out.println("Closed database successfully");
		return System.currentTimeMillis();
	}

}
