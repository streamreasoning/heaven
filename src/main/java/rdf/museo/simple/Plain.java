package rdf.museo.simple;

import java.util.Arrays;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.core.service.EPServiceProviderSPI;
import commons.LoggingListener;

/**
 * In this example rdfs property of subclass of is exploited by external static
 * functions which can be called form EPL No data or time windows are considered
 * in event consuming, se the related example for that time is externally
 * controlled all event are sent in the samte time interval
 * 
 * the query doesn't include joins
 * 
 * events are pushed, on incoming events, in 3 differents queue which are pulled
 * by refering statements
 * 
 * 
 * 
 * 
 * **/

public class Plain {
	protected static Configuration cepConfig;
	protected static ConsoleAppender appender;
	protected static EPServiceProvider cep;
	protected static EPRuntime cepRT;
	protected static EPAdministrator cepAdm;

	protected static String[] personResource = { "Person", "Artist", "Worker" };
	protected static String[] artistResource = { "Artist", "Painter",
			"Sculptor" };
	protected static String[] actionsResource = { "creates", "sculpts",
			"paints" };
	protected static String[] piecesResource = { "Piece", "Paint", "Sculpt" };

	public static void main(String argv[]) throws InterruptedException {

		PatternLayout sl = new PatternLayout(
				"%d{HH:mm:ss.SS} - %t-%x-%-5p-%-10c:%m%n");
		appender = new ConsoleAppender(sl);
		Logger.getRootLogger().addAppender(appender);
		Logger.getRootLogger().setLevel((Level) Level.INFO);

		ConfigurationMethodRef ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();
		cepConfig.addMethodRef(Plain.class, ref);

		cepConfig.addEventType("TEvent", TEvent.class.getName());
		cepConfig.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cep = (EPServiceProviderSPI) EPServiceProviderManager.getProvider(
				Plain.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		cepRT.sendEvent(new CurrentTimeEvent(0));

		String input = "on TEvent "
				+ "insert into RDFS3Input select s as s, c as c, p as p, timestamp as timestamp, channel as channel "
				+ "insert into RDFS9Input select s as s, c as c, p as p, timestamp as timestamp, channel as channel "
				+ "insert into QueryOut select s as s, c as c, p as p, timestamp as timestamp, channel as channel "
				+ "output all ";

		String rdfs3 = "on RDFS3Input "
				+ "insert into QueryOut select c as s, 'typeOf' as p, rdf.museo.simple.Plain.range(p) as c, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "insert into RDFS9Input select c as s, 'typeOf' as p, rdf.museo.simple.Plain.range(p) as c, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "insert into QueryOut select s as s, 'typeOf' as p, rdf.museo.simple.Plain.domain(p) as c, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "insert into RDFS9Input select s as s, 'typeOf' as p, rdf.museo.simple.Plain.domain(p) as c, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "output all";

		String rdfs9 = "on RDFS9Input(p='typeOf') "
				+ "insert into QueryOut select s as s, p, rdf.museo.simple.Plain.subClassOf(c) as c, timestamp as timestamp , channel || 'RDSF9' as channel ";

		String queryOut = ""
				+ "select current_timestamp(), timestamp, s, p, c, channel from QueryOut.win:time_batch(1000 msec) ";

		cepAdm.createEPL(input);
		cepAdm.createEPL(rdfs3);
		cepAdm.createEPL(rdfs9);
		cepAdm.createEPL(queryOut).addListener(
				new LoggingListener("Queryout", false, false, false, cepConfig,
						(EPServiceProviderSPI) cep, (String[]) null));

		// after statements
		cepRT.sendEvent(new TEvent("Leonardo", "paints", "Gioconda", "Input"));
		cepRT.sendEvent(new CurrentTimeEvent(1000));

	}

	public static class TEvent {
		String s, p, c;
		long timestamp;
		String channel;

		public TEvent(String s, String p, String c, String ch) {
			this.s = s;
			this.p = p;
			this.c = c;
			this.channel = ch;
			this.timestamp = cepRT.getCurrentTime();
		}

		public String getS() {
			return s;
		}

		public void setS(String s) {
			this.s = s;
		}

		public String getP() {
			return p;
		}

		public void setP(String p) {
			this.p = p;
		}

		public String getC() {
			return c;
		}

		public void setC(String c) {
			this.c = c;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}

		@Override
		public String toString() {
			return "TEvent [s=" + s + ", p=" + p + ", c=" + c + "]";
		}

	}

	public static String subClassOf(String s) {
		if (Arrays.asList(personResource).contains(s)) {
			return "Person";
		} else if (Arrays.asList(artistResource).contains(s)) {
			return "Artist";
		} else if (Arrays.asList(piecesResource).contains(s)) {
			return "Piece";
		} else
			return "RDFResource";
	}

	public static String subPropertyOf(String s) {
		if (Arrays.asList(actionsResource).contains(s)) {
			return "creates";
		}
		return "RDFResource";
	}

	public static String range(String p) {

		if ("sculpts".equals(p))
			return "Sculpt";
		else if ("paints".equals(p))
			return "Paint";
		else if ("creates".equals(p))
			return "Piece";
		else
			return "RDFResource";

	}

	public static String domain(String p) {

		if ("sculpts".equals(p))
			return "Sculptor";
		else if ("paints".equals(p))
			return "Painter";
		else if ("creates".equals(p))
			return "Artist";
		else
			return "RDFResource";

	}

	public static String typeOf(String p) {

		if (Arrays.asList(piecesResource).contains(p) || "typeOf".equals(p)
				|| "subClassOf".equals(p) || "subPropertyOf".equals(p))
			return "RDFProperty";
		else if ("RDFProperty".equals(p) || "RDFResource".equals(p)
				|| "RDFClass".equals(p))
			return "RDFClass";
		else
			return "RDFClass";

	}

}
