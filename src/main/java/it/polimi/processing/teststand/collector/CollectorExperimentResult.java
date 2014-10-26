package it.polimi.processing.teststand.collector;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.EventSaver;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.result.ExperimentResultEvent;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.teststand.core.TestStand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectorExperimentResult implements
		StartableCollector<ExperimentResultEvent> {

	private long timestamp;
	private EventSaver sqlLiteSaver;
	private TestStand<RSPEngine> stand;
	private ExecutionStates status;

	public CollectorExperimentResult(TestStand<RSPEngine> stand,
			EventSaver saver) throws SQLException, ClassNotFoundException {
		this.stand = stand;
		this.sqlLiteSaver = saver;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionStates.READY;
	}

	@Override
	public boolean store(ExperimentResultEvent r) throws IOException {
		if (!ExecutionStates.READY.equals(status)) {
			return false;
		} else {
			return sqlLiteSaver.save(r.getSQL());
		}
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public ExecutionStates init() {
		try {
			if (ExecutionStates.READY.equals(sqlLiteSaver.init())) {
				return status = ExecutionStates.READY;
			} else {
				return status = ExecutionStates.ERROR;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return status = ExecutionStates.ERROR;
		}
	}

	@Override
	public ExecutionStates close() {
		try {
			if (ExecutionStates.CLOSED.equals(sqlLiteSaver.close())) {
				return status = ExecutionStates.CLOSED;
			} else {
				return status = ExecutionStates.ERROR;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return status = ExecutionStates.ERROR;
		}
	}

	@Override
	public ExperimentResultEvent newEventInstance(Set<String[]> all_triples,
			Event e) {
		// TODO Auto-generated method stub
		return null;
	}

}
