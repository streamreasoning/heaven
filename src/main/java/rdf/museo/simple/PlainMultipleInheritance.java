package rdf.museo.simple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import rdf.museo.simple.events.Out;
import rdf.museo.simple.events.TEvent;

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

public class PlainMultipleInheritance {
	protected static Configuration cepConfig;
	protected static ConsoleAppender appender;
	protected static EPServiceProvider cep;
	protected static EPRuntime cepRT;
	protected static EPAdministrator cepAdm;

	protected static String[] actionsResource = { "creates", "sculpts",
			"paints" };

	private static Map<String, String[]> ontology;

	public static void main(String argv[]) throws InterruptedException {

		ontology = new HashMap<String, String[]>();
		ontology.put("Artist", new String[] { "Person", "RDFResource" });
		ontology.put("Worker", new String[] { "Person", "RDFResource" });
		ontology.put("Maker",
				new String[] { "Worker", "Person", "RDFResource" });
		ontology.put("Painter", new String[] { "Person", "Artist",
				"RDFResource" });
		ontology.put("Sculptor", new String[] { "Person", "Artist", "Maker",
				"Worker", "RDFResource" });

		ontology.put("Paint", new String[] { "RDFResource", "Piece" });
		ontology.put("Sculpt", new String[] { "RDFResource", "Piece" });
		ontology.put("Piece", new String[] { "RDFResource" });

		PatternLayout sl = new PatternLayout(
				"%d{HH:mm:ss.SS} - %t-%x-%-5p-%-10c:%m%n");
		appender = new ConsoleAppender(sl);
		Logger.getRootLogger().addAppender(appender);
		Logger.getRootLogger().setLevel((Level) Level.INFO);

		ConfigurationMethodRef ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();
		cepConfig.addMethodRef(PlainMultipleInheritance.class, ref);

		cepConfig.addEventType("TEvent", TEvent.class.getName());
		cepConfig.addEventType("Out", Out.class.getName());

		cepConfig.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cep = (EPServiceProviderSPI) EPServiceProviderManager.getProvider(
				PlainMultipleInheritance.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		cepRT.sendEvent(new CurrentTimeEvent(0));

		String input = "on TEvent "
				+ "insert into RDFS3Input select s as s, o as o, p as p, timestamp as timestamp, channel as channel "
				+ "insert into RDFS9Input select s as s, o as o, p as p, timestamp as timestamp, channel as channel "
				+ "insert into QueryOut select s as s, o as o, p as p, timestamp as timestamp, channel as channel "
				+ "output all ";

		String rdfs3 = "on RDFS3Input(p!='typeOf') "
				+ "insert into QueryOut select o as s, 'typeOf' as p, rdf.museo.simple.PlainMultipleInheritance.range(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "insert into RDFS9Input select o as s, 'typeOf' as p, rdf.museo.simple.PlainMultipleInheritance.range(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "insert into QueryOut select s as s, 'typeOf' as p, rdf.museo.simple.PlainMultipleInheritance.domain(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "insert into RDFS9Input select s as s, 'typeOf' as p, rdf.museo.simple.PlainMultipleInheritance.domain(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "output all";

		String rdfs9 = "on RDFS9Input(p='typeOf') "
				+ "insert into QueryOut select s as s, p, rdf.museo.simple.PlainMultipleInheritance.subClassOf(o)  as o, timestamp as timestamp , channel || 'RDSF9' as channel ";

		String queryOut = "insert into Out "
				+ "select  timestamp, s, p, o, channel from QueryOut.win:time_batch(1000 msec) ";

		cepAdm.createEPL(input);
		cepAdm.createEPL(rdfs3);
		cepAdm.createEPL(rdfs9);
		cepAdm.createEPL(queryOut).addListener(
				new LoggingListener("Queryout", false, false, false, cepConfig,
						(EPServiceProviderSPI) cep, (String[]) null));

		// after statements
		cepRT.sendEvent(new TEvent(new String[] { "Leonardo" }, "paints",
				new String[] { "Gioconda" }, "Input", cepRT.getCurrentTime()));
		cepRT.sendEvent(new CurrentTimeEvent(1000));

	}

	public static String[] subClassOf(String[] listString) {
		Set<String> retList = new HashSet<String>();
		for (String string : listString) {
			if (ontology.containsKey(string)) {
				for (String s : ontology.get(string)) {
					retList.add(s);
				}
			} else
				retList.add("RDFResource");
		}
		String[] arr = retList.toArray(new String[retList.size()]);
		return arr;
	}

	public static String subPropertyOf(String s) {
		if (Arrays.asList(actionsResource).contains(s)) {
			return "creates";
		}
		return "RDFResource";
	}

	public static String[] range(String p) {

		if ("sculpts".equals(p))
			return new String[] { "Sculpt" };
		else if ("paints".equals(p))
			return new String[] { "Paint" };
		else if ("creates".equals(p))
			return new String[] { "Piece" };
		else
			return new String[] { "RDFResource" };

	}

	public static String[] domain(String p) {

		if ("sculpts".equals(p))
			return new String[] { "Sculptor" };
		else if ("paints".equals(p))
			return new String[] { "Painter" };
		else if ("creates".equals(p))
			return new String[] { "Artist" };
		else
			return new String[] { "RDFResource" };

	}

}
