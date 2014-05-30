package rdf.museo.simple;

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

public class StaticExternal {
	protected static Configuration cepConfig;
	protected static ConsoleAppender appender;
	protected static EPServiceProvider cep;
	protected static EPRuntime cepRT;
	protected static EPAdministrator cepAdm;

	public static void main(String argv[]) throws InterruptedException {

		PatternLayout sl = new PatternLayout(
				"%d{HH:mm:ss.SS} - %t-%x-%-5p-%-10c:%m%n");
		appender = new ConsoleAppender(sl);
		Logger.getRootLogger().addAppender(appender);
		Logger.getRootLogger().setLevel((Level) Level.INFO);

		ConfigurationMethodRef ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();
		cepConfig.addMethodRef(StaticExternal.class, ref);

		cepConfig.addEventType("TEvent", TEvent.class.getName());
		cepConfig.getEngineDefaults().getViewResources().setShareViews(false);
		cepConfig.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cep = (EPServiceProviderSPI) EPServiceProviderManager.getProvider(
				StaticExternal.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		cepRT.sendEvent(new CurrentTimeEvent(0));

		String input = "on TEvent "
				+ "insert into RDFS3Input select s as s, c as c, p as p "
				+ "insert into RDFS9Input select s as s, c as c, p as p "
				+ "insert into QueryOut select s as s, c as c, p as p "
				+ "output all ";

		String rdfs3 = "on RDFS3Input "
				+ "insert into QueryOut select c as s, 'typeOf' as p, rdf.museo.StaticExternal.range(s,p,c) as c "
				+ "insert into RDFS9Input select c as s, 'typeOf' as p, rdf.museo.StaticExternal.range(s,p,c) as c "
				+ "insert into QueryOut select s as s, 'typeOf' as p, rdf.museo.StaticExternal.domain(s,p,c) as c "
				+ "insert into RDFS9Input select s as s, 'typeOf' as p, rdf.museo.StaticExternal.domain(s,p,c) as c "
				+ "output all";

		String rdfs9 = "on RDFS9Input "
				+ "insert into QueryOut select s as s, p, rdf.museo.StaticExternal.subClassOf(c) as c where p='typeOf' ";

		String queryOut = "insert into OutEvent "
				+ "select * from QueryOut "

				+ "where rdf.museo.StaticExternal.subClassOf(c)='Sculptor' or c='Sculptor' ";
		String queryOut1 = "insert into OutEvent " + "select * from QueryOut "
				+ "where rdf.museo.StaticExternal.subClassOf(s)='Piece'";
		// all artist that are both scluptors and painters with joins, windows
		// are mandatory
		String queryOut2 = "insert into OutEvent "
				+ "select distinct qo1.s from QueryOut(p='typeOf', c!='Artist' and c!='Piece').win:time(500 msec) as qo1, QueryOut(p='typeOf',c!='Artist' and c!='Piece').win:time(500 msec) as qo2  "
				+ "where qo1.s=qo2.s  and qo1.c!=qo2.c ";

		cepAdm.createEPL(input);
		cepAdm.createEPL(rdfs3);
		cepAdm.createEPL(rdfs9);
		cepAdm.createEPL(queryOut2).addListener(
				new LoggingListener(false, cepConfig,
						(EPServiceProviderSPI) cep, (String[]) null));

		// after statements
		cepRT.sendEvent(new TEvent("Leonardo", "paints", "Gioconda"));
		cepRT.sendEvent(new TEvent("Raffaello", "sculpts", "David"));
		cepRT.sendEvent(new TEvent("Rodin", "creates", "The Kiss"));
		cepRT.sendEvent(new TEvent("Leonardo", "sculpts", "Cavallo"));
		cepRT.sendEvent(new CurrentTimeEvent(500));

	}

	public static class TEvent {
		String s, p, c;

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

		@Override
		public String toString() {
			return "TEvent [s=" + s + ", p=" + p + ", c=" + c + "]";
		}

		public TEvent(String s, String p, String c) {
			this.s = s;
			this.p = p;
			this.c = c;
		}

	}

	public static String subClassOf(String s) {
		if ("Sculptor".equals(s) || "Painter".equals(s) || "Artist".equals(s)) {
			return "Artist";
		} else if ("Paint".equals(s) || "Sculpt".equals(s) || "Piece".equals(s)) {
			return "Piece";
		} else {
			return typeOf(s);
		}
	}

	public static String typeOf(String s) {
		if ("The Kiss".equals(s))
			return "Paint";
		else if ("Cavallo".equals(s))
			return "Sculpt";
		else if ("Gioconda".equals(s))
			return "Piece";
		else if ("David".equals(s))
			return "Sculpt";
		else if ("Leonardo".equals(s))
			return "Artist";
		else if ("Rodin".equals(s))
			return "Artist";
		else if ("Raffaello".equals(s))
			return "Artist";
		else
			return "Unknown Type " + s;
	}

	public static String range(String s, String p, String c) {
		if ("sculpts".equals(p))
			return "Sculpt";
		else if ("paints".equals(p))
			return "Paint";
		else if ("creates".equals(p))
			return "Piece";
		else if ("is".equals(p))
			return c;
		else if ("typeOf".equals(p))
			return c;
		else
			return "Unknown Range " + p;

	}

	public static String domain(String s, String p, String c) {
		if ("sculpts".equals(p))
			return "Sculptor";
		else if ("paints".equals(p))
			return "Painter";
		else if ("creates".equals(p))
			return "Artist";
		else if ("is".equals(p))
			return typeOf(s);
		else if ("typeOf".equals(p))
			return typeOf(s);
		else
			return "Unknown Domain: " + p;

	}
}
