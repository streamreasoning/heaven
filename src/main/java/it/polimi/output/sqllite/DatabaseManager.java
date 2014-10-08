package it.polimi.output.sqllite;

import it.polimi.enums.ExecutionStates;

import java.sql.SQLException;

/**
 * @author Riccardo
 * 
 *         This interface publish the main method of a Data Base Manager System
 * 
 * 
 */
public interface DatabaseManager {

	/**
	 * @return Timestamp of the execution
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ExecutionStates init() throws ClassNotFoundException, SQLException;

	/**
	 * @return Timestamp of the execution
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ExecutionStates close() throws ClassNotFoundException, SQLException;

	/**
	 * @param sql
	 *            the query that have to be executed
	 * @return the status of the execution true means succesful
	 */
	public boolean executeSql(String sql);

}
