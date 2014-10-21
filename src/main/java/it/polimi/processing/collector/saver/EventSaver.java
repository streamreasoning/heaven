package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.Collectable;
import it.polimi.processing.collector.CollectableData;
import it.polimi.processing.enums.ExecutionStates;

import java.sql.SQLException;

public interface EventSaver {

	public boolean save(Collectable e);

	public boolean save(CollectableData d);

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
