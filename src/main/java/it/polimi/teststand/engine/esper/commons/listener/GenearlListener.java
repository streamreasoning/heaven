package it.polimi.teststand.engine.esper.commons.listener;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public abstract class GenearlListener implements UpdateListener {

	protected PatternLayout sl = new PatternLayout(
			"%C{1}-%d{HH:mm:ss.SS} - %t-%x-%-5p-%-10c:%m%n");
	protected ConsoleAppender appender = new ConsoleAppender(sl);
	protected Logger _logger = Logger.getRootLogger();

	public GenearlListener(Level l) {
		_logger.addAppender(appender);
		_logger.setLevel((Level) l);
	}

	@Override
	public abstract void update(EventBean[] newEvents, EventBean[] oldEvents);

}
