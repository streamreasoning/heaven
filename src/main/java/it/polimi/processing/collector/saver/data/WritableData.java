package it.polimi.processing.collector.saver.data;

import it.polimi.processing.services.FileService;
import lombok.extern.log4j.Log4j;

@Log4j
public abstract class WritableData implements CollectableData {

	@Override
	public boolean save(String where) {
		log.debug("Write  [" + this.getClass().getSimpleName() + "] Data ");
		return FileService.write(where, this);
	}
}
