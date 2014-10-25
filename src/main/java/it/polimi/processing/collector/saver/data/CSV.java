package it.polimi.processing.collector.saver.data;

import it.polimi.processing.collector.Collectable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CSV implements CollectableData {

	private Collectable event;

	@Override
	public String getData() {
		return event.getCSV();
	}

	@Override
	public String getName() {
		return event.getName();
	}

}
