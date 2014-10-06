package it.polimi.teststand.engine.esper.commons.listener;

import org.apache.log4j.Level;

import com.espertech.esper.client.EventBean;

public class LoggingListener extends GenearlListener {

	public LoggingListener() {
		super(Level.INFO);
	}

	public void update(EventBean[] arg0, EventBean[] arg1) {
		_logger.debug("START LOG");
		for (EventBean e : arg0) {
			_logger.debug(e.getUnderlying());
		}
	}

}
