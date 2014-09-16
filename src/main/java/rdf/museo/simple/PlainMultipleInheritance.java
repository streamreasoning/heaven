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
import commons.Streamer;

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

	private static Map<String, String[]> ontology;
	private static Map<String, String[]> properties;
	private static Map<String, String> propertiesRange;
	private static Map<String, String> propertiesDomain;

	private static int numProperties, numClasses = 0;

	public static void main(String argv[]) throws InterruptedException {

		inizializeOntology();

		initializeObjectProperties();

		PatternLayout sl = new PatternLayout(
				"%d{HH:mm:ss.SS} - %t-%x-%-5p-%-10c:%m%n");
		appender = new ConsoleAppender(sl);
		Logger.getRootLogger().addAppender(appender);
		Logger.getRootLogger().setLevel((Level) Level.INFO);

		final Streamer streamer = new Streamer();
		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					streamer.stream();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();

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
		int time = 0;
		cepRT.sendEvent(new CurrentTimeEvent(time));

		String input = "on TEvent "
				+ "insert into RDFS3Input select s as s, o as o, p as p, timestamp as timestamp, channel as channel "
				+ "insert into RDFS9Input select s as s, o as o, p as p, timestamp as timestamp, channel as channel "
				+ "insert into QueryOut select s as s, o as o, p as p, timestamp as timestamp, channel as channel "
				+ "output all ";

		String rdfs3 = "on RDFS3Input(p!='typeOf' and p!='type') "
				+ "insert into QueryOut select o as s, 'typeOf' as p, rdf.museo.simple.PlainMultipleInheritance.range(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "insert into RDFS9Input select o as s, 'typeOf' as p, rdf.museo.simple.PlainMultipleInheritance.range(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "insert into QueryOut select s as s, 'typeOf' as p, rdf.museo.simple.PlainMultipleInheritance.domain(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "insert into RDFS9Input select s as s, 'typeOf' as p, rdf.museo.simple.PlainMultipleInheritance.domain(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
				+ "output all";

		String rdfs9 = "on RDFS9Input(p='typeOf' or p='type') "
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
		

		while (streamer.hasNext()) {
			String[] event = Streamer.getEvent();
			cepRT.sendEvent(new TEvent(new String[] { event[0] }, event[1],
					new String[] { event[2] }, "Input", cepRT.getCurrentTime()));

			// <http://www.Department0.University0.edu/FullProfessor7>
			// <http://swat.cse.lehigh.edu/onto/univ-bench.owl#headOf>
			// <http://www.Department0.University0.edu> .
			time += 1000;
			cepRT.sendEvent(new CurrentTimeEvent(time));
		}
	}

	private static void initializeObjectProperties() {
		properties = new HashMap<String, String[]>();
		propertiesDomain = new HashMap<String, String>();
		propertiesRange = new HashMap<String, String>();

		properties.put("advisor", new String[] { "RDFProperty" });
		propertiesDomain.put("advisor", "Person");
		propertiesRange.put("advisor", "Professor");
		numProperties++;

		properties.put("affiliatedOrganizationOf",
				new String[] { "RDFProperty" });
		propertiesDomain.put("affiliatedOrganizationOf", "Organization");
		propertiesRange.put("affiliatedOrganizationOf", "Organization");
		numProperties++;
		properties.put("affiliateOf", new String[] { "RDFProperty" });
		propertiesDomain.put("affiliateOf", "Organization");
		propertiesRange.put("affiliateOf", "Person");
		numProperties++;
		properties.put("degreeFrom", new String[] { "RDFProperty" });
		propertiesDomain.put("degreeFrom", "Person");
		propertiesRange.put("degreeFrom", "University");
		numProperties++;
		properties.put("doctoralDegreeFrom", new String[] { "degreeFrom",
				"RDFProperty" });
		propertiesDomain.put("doctoralDegreeFrom", "Person");
		propertiesRange.put("doctoralDegreeFrom", "University");
		numProperties++;
		properties.put("mastersDegreeFrom", new String[] { "degreeFrom",
				"RDFProperty" });
		propertiesDomain.put("degreeFrom", "Person");
		propertiesRange.put("degreeFrom", "University");
		numProperties++;
		properties.put("undergraduateDegreeFrom", new String[] { "degreeFrom",
				"RDFProperty" });
		propertiesDomain.put("undergraduateDegreeFrom", "Person");
		propertiesRange.put("undergraduateDegreeFrom", "University");
		numProperties++;
		properties.put("hasAlumnus", new String[] { "RDFProperty" });
		propertiesDomain.put("hasAlumnus", "University");
		propertiesRange.put("hasAlumnus", "Person");
		numProperties++;

		properties.put("headOf", new String[] { "RDFProperty", "worksFor",
				"memberOf" });
		propertiesDomain.put("headOf", "Person");
		numProperties++;
		properties.put("member", new String[] { "RDFProperty" });
		propertiesDomain.put("member", "Organization");
		propertiesRange.put("member", "Person");
		numProperties++;
		properties.put("memberOf", new String[] { "RDFProperty" });
		propertiesDomain.put("memberOf", "Person");
		propertiesRange.put("memberOf", "Organization");
		numProperties++;
		properties.put("worksFor", new String[] { "RDFProperty", "memberOf",
				"memberOf" });
		propertiesDomain.put("worksFor", "Person");
		propertiesRange.put("worksFor", "University");
		numProperties++;
		properties.put("headOf", new String[] { "RDFProperty", "worksFor",
				"memberOf" });
		propertiesDomain.put("headOf", "Person");
		propertiesRange.put("headOf", "University");
		numProperties++;
		properties.put("listedCourse", new String[] { "RDFProperty" });
		propertiesDomain.put("listedCourse", "Schedule");
		propertiesRange.put("listedCourse", "Course");
		numProperties++;
		properties.put("orgPublication", new String[] { "RDFProperty" });
		propertiesDomain.put("orgPublication", "Organization");
		propertiesRange.put("orgPublication", "Pubblication");
		numProperties++;
		properties.put("publicationAuthor", new String[] { "RDFProperty" });
		propertiesDomain.put("publicationAuthor", "Publication");
		propertiesRange.put("publicationAuthor", "Person");
		numProperties++;
		// properties.put("publicationDate", new String[] {"RDFProperty"});
		// propertiesDomain.put("publicationDate","Publication" );
		// propertiesRange.put("publicationDate", "Date");
		numProperties++;
		properties.put("publicationResearch", new String[] { "RDFProperty" });
		propertiesDomain.put("publicationResearch", "Publication");
		propertiesRange.put("publicationResearch", "Research");
		numProperties++;
		properties.put("softwareDocumentation", new String[] { "RDFProperty" });
		propertiesDomain.put("softwareDocumentation", "Software");
		propertiesRange.put("softwareDocumentation", "Publication");
		numProperties++;
		// properties.put("softwareVersion", new String[] {"RDFProperty"});
		// propertiesDomain.put("softwareVersion","Software" );
		// propertiesRange.put("softwareVersion", "Number");
		numProperties++;
		properties.put("takesCourse", new String[] { "RDFProperty" });
		propertiesDomain.put("takesCourse", "Student");
		propertiesRange.put("takesCourse", "Course");
		numProperties++;
		properties.put("teacherOf", new String[] { "RDFProperty" });
		propertiesDomain.put("teacherOf", "Faculty");
		propertiesRange.put("teacherOf", "Course");
		numProperties++;
		properties.put("teachingAssistantOf", new String[] { "RDFProperty" });
		propertiesDomain.put("teachingAssistantOf",
				"University Teaching Assistant");
		propertiesRange.put("teachingAssistantOf", "Course");
		numProperties++;
		properties.put("researchProject", new String[] { "RDFProperty" });
		propertiesDomain.put("researchProject", "Research Group");
		propertiesRange.put("researchProject", "Research Work");
		numProperties++;
		// properties.put("tenured", new String[] {"RDFProperty"});
		// propertiesDomain.put("tenured","Professor" );
		// propertiesRange.put("tenured", "Boolean");
		numProperties++;

		System.out.println(numProperties);
	}

	private static void inizializeOntology() {
		ontology = new HashMap<String, String[]>();

		ontology.put("Organization", new String[] { "RDFResource" });
		numClasses++;
		ontology.put("Institute",
				new String[] { "Organization", "RDFResource" });
		numClasses++;

		ontology.put("Program", new String[] { "Organization", "RDFResource" });
		numClasses++;

		ontology.put("Research Group", new String[] { "Organization",
				"RDFResource" });
		numClasses++;

		ontology.put("School", new String[] { "Organization", "RDFResource" });
		numClasses++;

		ontology.put("University",
				new String[] { "Organization", "RDFResource" });
		numClasses++;

		ontology.put("University Department", new String[] { "Organization",
				"RDFResource" });
		numClasses++;

		ontology.put("Person", new String[] { "RDFResource" });
		numClasses++;

		ontology.put("Chair", new String[] { "Person", "Professor",
				"RDFResource" });
		numClasses++;

		ontology.put("Director", new String[] { "Person", "RDFResource" });
		numClasses++;

		ontology.put("Employee", new String[] { "Person", "RDFResource" });
		numClasses++;

		ontology.put("Faculty Member", new String[] { "Faculty Member",
				"Employee", "Person", "RDFResource" });
		numClasses++;

		ontology.put("Lecturerr", new String[] { "Faculty Member", "Employee",
				"Person", "RDFResource" });
		numClasses++;

		ontology.put("Post Doctorate", new String[] { "Faculty Member",
				"Employee", "Person", "RDFResource" });
		numClasses++;

		ontology.put("Professor", new String[] { "Faculty Member", "Employee",
				"Person", "RDFResource" });
		numClasses++;

		ontology.put("Assistant Professor", new String[] { "Professor",
				"Faculty Member", "Employee", "Person", "RDFResource" });
		numClasses++;

		ontology.put("Associate Professor", new String[] { "Professor",
				"Faculty Member", "Employee", "Person", "RDFResource" });
		numClasses++;

		ontology.put("Dean", new String[] { "Professor", "Faculty Member",
				"Employee", "Person", "RDFResource" });
		numClasses++;

		ontology.put("Full Professor", new String[] { "Professor",
				"Faculty Member", "Employee", "Person", "RDFResource" });
		numClasses++;

		ontology.put("Visiting Professor", new String[] { "Professor",
				"Faculty Member", "Employee", "Person", "RDFResource" });
		numClasses++;

		ontology.put("Administrative Staff Worker", new String[] { "Employee",
				"Person", "RDFResource" });
		numClasses++;

		ontology.put("Clerical Staff Worker", new String[] {
				"Administrative Staff Worker", "Employee", "Person",
				"RDFResource" });
		numClasses++;

		ontology.put("System Staff Worker", new String[] {
				"Administrative Staff Worker", "Employee", "Person",
				"RDFResource" });
		numClasses++;

		ontology.put("Graduated Student", new String[] { "Person",
				"RDFResource" });
		numClasses++;

		ontology.put("Student", new String[] { "Person", "RDFResource" });
		numClasses++;

		ontology.put("Undergraduated Student", new String[] { "Person",
				"RDFResource" });
		numClasses++;

		ontology.put("University Research Assistant", new String[] { "Person",
				"RDFResource" });
		numClasses++;

		ontology.put("University Teaching Assistant", new String[] { "Person",
				"RDFResource" });
		numClasses++;

		ontology.put("Pubblication", new String[] { "RDFResource" });
		numClasses++;

		ontology.put("Book", new String[] { "Pubblication", "RDFResource" });
		numClasses++;

		ontology.put("Article", new String[] { "Pubblication", "RDFResource" });
		numClasses++;

		ontology.put("Conference Paper", new String[] { "Article",
				"Pubblication", "RDFResource" });
		numClasses++;

		ontology.put("Journal Article", new String[] { "Article",
				"Pubblication", "RDFResource" });
		numClasses++;

		ontology.put("Technical Report", new String[] { "Article",
				"Pubblication", "RDFResource" });
		numClasses++;

		ontology.put("Manual", new String[] { "Pubblication", "RDFResource" });
		numClasses++;

		ontology.put("Published Specification", new String[] { "Pubblication",
				"RDFResource" });
		numClasses++;

		ontology.put("Software", new String[] { "Pubblication", "RDFResource" });
		numClasses++;

		ontology.put("Unnofficial Pubblication", new String[] { "Pubblication",
				"RDFResource" });
		numClasses++;

		ontology.put("Schedule", new String[] { "Pubblication", "RDFResource" });
		numClasses++;

		ontology.put("Work", new String[] { "RDFResource" });
		numClasses++;

		ontology.put("Research Work", new String[] { "Work", "RDFResource" });
		numClasses++;

		ontology.put("Teaching Course", new String[] { "Teaching Course",
				"Work", "RDFResource" });
		numClasses++;

		ontology.put("Graduated Level Course", new String[] {
				"Teaching Course", "Work", "RDFResource" });
		numClasses++;

		System.out.println(numClasses);
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

	public static String[] subPropertyOf(String s) {
		return properties.get(s);
	}

	public static String[] range(String p) {

		return new String[] { propertiesRange.get(p) };

	}

	public static String[] domain(String p) {

		return new String[] { propertiesDomain.get(p) };

	}

}
