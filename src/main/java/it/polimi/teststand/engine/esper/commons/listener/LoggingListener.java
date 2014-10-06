package it.polimi.teststand.engine.esper.commons.listener;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.espertech.esper.client.EventBean;

public class LoggingListener extends GenearlListener {

	public LoggingListener() {
	}

	public void update(EventBean[] arg0, EventBean[] arg1) {
		Logger.getRootLogger().setLevel(Level.INFO);
		Logger.getRootLogger().debug("START LOG");
		for (EventBean e : arg0) {
			Logger.getRootLogger().debug(e.getUnderlying());
		}
	}

}
