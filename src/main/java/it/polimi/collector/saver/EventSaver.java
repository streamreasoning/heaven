package it.polimi.collector.saver;

import it.polimi.collector.Collectable;
import it.polimi.enums.ExecutionStates;

import java.sql.SQLException;

public interface EventSaver {

	public boolean save(Collectable e);

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


}
