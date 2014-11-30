package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.enums.ExecutionState;

import java.sql.SQLException;

public interface EventSaver {

	public boolean save(CollectableData d, String where);

	/**
	 * @return Timestamp of the execution
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ExecutionState init();

	/**
	 * @return Timestamp of the execution
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ExecutionState close();

}
