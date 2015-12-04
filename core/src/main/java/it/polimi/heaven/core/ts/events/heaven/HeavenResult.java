package it.polimi.heaven.core.ts.events.heaven;

import it.polimi.heaven.core.ts.collector.Collectable;
import it.polimi.heaven.core.ts.events.engine.Response;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeavenResult implements HeavenEvent, Collectable {

	private String id;
	private Response response;
	private HeavenInput input;
	private double memory_after_processing;
	private long creation_timestamp;

	public HeavenResult(Response response, long creation_timestamp, double memory_after_processing) {
		this.response = response;
		this.creation_timestamp = creation_timestamp;
		this.memory_after_processing = memory_after_processing;
	}

	@Override
	public boolean save(String where) {
		if (response instanceof Collectable) {
			Collectable coll_resp = (Collectable) response;
			return coll_resp.save(where);
		}
		// TODO exception?
		return false;
	}

	public long getResponseCreationTime() {
		return response.getCreationTime();
	}

}
