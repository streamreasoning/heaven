package rdf.museo.actionbased;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import rdf.museo.ontology.Artist;
import rdf.museo.ontology.Creates;
import rdf.museo.ontology.Paint;
import rdf.museo.ontology.Painter;
import rdf.museo.ontology.Paints;
import rdf.museo.ontology.Sculpt;
import rdf.museo.ontology.Sculptor;
import rdf.museo.ontology.Sculpts;
import rdf.museo.querybased.events.RDFS3;
import rdf.museo.querybased.events.RDFS9;
import rdf.museo.querybased.events.RDFSInput;
import rdf.museo.querybased.events.RDFSOut;
import rdf.museo.rdf.TypeOf;

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

public class Inherritance {
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
		cepConfig.addMethodRef(Inherritance.class, ref);

		// eventi in classi diverse perche' altrimenti non vengono distinti a
		// livello di ELP, indagare
		cepConfig.addEventType("RDFSInput", RDFSInput.class.getName());
		cepConfig.addEventType("RDFS3Input", RDFS3.class.getName());
		cepConfig.addEventType("RDFS9Input", RDFS9.class.getName());
		cepConfig.addEventType("QueryOut", RDFSOut.class.getName());

		Creates creates = new Creates();
		Sculpts sculpts = new Sculpts();
		Paints paints = new Paints();
		TypeOf typeof = new TypeOf();
		cepConfig.addVariable("typeof", TypeOf.class, typeof, true);
		cepConfig.addVariable("creates", Creates.class, creates, true);
		cepConfig.addVariable("sculpts", Sculpts.class, sculpts, true);
		cepConfig.addVariable("paints", Paints.class, paints, true);

		cepConfig.addVariable("artist", Class.class, Artist.class, true);
		cepConfig.addVariable("sculptor", Class.class, Sculptor.class, true);
		cepConfig.addVariable("painter", Class.class, Painter.class, true);

		cepConfig.getEngineDefaults().getViewResources().setShareViews(false);
		cepConfig.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cep = (EPServiceProviderSPI) EPServiceProviderManager.getProvider(
				Inherritance.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		cepRT.sendEvent(new CurrentTimeEvent(0));

		String input = "on RDFSInput "
				+ "insert into RDFS3Input select s as s, c as c, p as p "
				+ "insert into RDFS9Input select s as s, c as c, p as p "
				+ "insert into QueryOut select s as s, c as c, p as p "
				+ "output all ";

		String rdfs3 = "on RDFS3Input "
				+ "insert into QueryOut select c as s, typeof as p, p.range as c "
				+ "insert into RDFS9Input select c as s,  typeof as p, p.range as c "
				+ "insert into QueryOut select s as s, typeof as p, p.domain as c "
				+ "insert into RDFS9Input select s as s, typeof as p, p.domain as c "
				+ "output all";

		String rdfs9 = "on RDFS9Input "
				+ "insert into QueryOut select s as s, p, c as c where p=typeof ";

		String queryOut = "insert into OutEvent "
				+ "select distinct s from QueryOut(instanceof(s, rdf.museo.events.ontology.Sculptor) ).win:time_batch(1000 msec)";

		cepAdm.createEPL(input);
		cepAdm.createEPL(rdfs3);
		cepAdm.createEPL(rdfs9);
		cepAdm.createEPL(queryOut).addListener(
				new LoggingListener(false, cepConfig,
						(EPServiceProviderSPI) cep, (String[]) null));

		// after statements

		cepRT.sendEvent(new RDFSInput(new Painter("Leonardo"), paints,
				new Paint("Gioconda")));
		cepRT.sendEvent(new RDFSInput(new Sculptor("Michelangelo"), sculpts,
				new Sculpt("David")));
		cepRT.sendEvent(new RDFSInput(new Artist("Rodin"), creates, new Sculpt(
				"Kiss")));

		cepRT.sendEvent(new CurrentTimeEvent(500));

		cepRT.sendEvent(new RDFSInput(new Painter("Munch"), paints, new Paint(
				"Urlo")));
		cepRT.sendEvent(new RDFSInput(new Sculptor("Bernini"), sculpts,
				new Sculpt("Apollo e Dafne")));
		cepRT.sendEvent(new RDFSInput(new Artist("Canova"), typeof,
				new Sculptor("Canova")));
		cepRT.sendEvent(new RDFSInput(new Painter("Michelangelo"), paints,
				new Paint("giudizio universale")));

		cepRT.sendEvent(new CurrentTimeEvent(1000));

	}

}
