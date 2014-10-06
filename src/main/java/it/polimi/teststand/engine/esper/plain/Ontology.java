package it.polimi.teststand.engine.esper.plain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Ontology {

	private static Map<String, String[]> ontology;
	private static Map<String, String[]> properties;
	private static Map<String, String> propertiesRange;
	private static Map<String, String> propertiesDomain;
	private static int numProperties, numClasses = 0;

	public static final String UNIV_BENCH_NS = "http://swat.cse.lehigh.edu/onto/univ-bench.owl";
	public static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	static {
		properties = new HashMap<String, String[]>();
		propertiesDomain = new HashMap<String, String>();
		propertiesRange = new HashMap<String, String>();
		initializeObjectProperties();
		inizializeOntology();
	}

	private static void initializeObjectProperties() {

		properties.put("advisor", new String[] { RDF + "Property" });
		propertiesDomain.put("advisor", "Person");
		propertiesRange.put("advisor", "Professor");
		numProperties++;

		properties.put("affiliatedOrganizationOf", new String[] { RDF
				+ "Property" });
		propertiesDomain.put("affiliatedOrganizationOf", "Organization");
		propertiesRange.put("affiliatedOrganizationOf", "Organization");
		numProperties++;
		properties.put("affiliateOf", new String[] { RDF + "Property" });
		propertiesDomain.put("affiliateOf", "Organization");
		propertiesRange.put("affiliateOf", "Person");
		numProperties++;
		properties.put("degreeFrom", new String[] { RDF + "Property" });
		propertiesDomain.put("degreeFrom", "Person");
		propertiesRange.put("degreeFrom", "University");
		numProperties++;
		properties.put("doctoralDegreeFrom", new String[] { "degreeFrom",
				RDF + "Property" });
		propertiesDomain.put("doctoralDegreeFrom", "Person");
		propertiesRange.put("doctoralDegreeFrom", "University");
		numProperties++;
		properties.put("mastersDegreeFrom", new String[] { "degreeFrom",
				RDF + "Property" });
		propertiesDomain.put("degreeFrom", "Person");
		propertiesRange.put("degreeFrom", "University");
		numProperties++;
		properties.put("undergraduateDegreeFrom", new String[] { "degreeFrom",
				RDF + "Property" });
		propertiesDomain.put("undergraduateDegreeFrom", "Person");
		propertiesRange.put("undergraduateDegreeFrom", "University");
		numProperties++;
		properties.put("hasAlumnus", new String[] { RDF + "Property" });
		propertiesDomain.put("hasAlumnus", "University");
		propertiesRange.put("hasAlumnus", "Person");
		numProperties++;

		properties.put("headOf", new String[] { RDF + "Property", "worksFor",
				"memberOf" });
		propertiesDomain.put("headOf", "Person");
		numProperties++;
		properties.put("member", new String[] { RDF + "Property" });
		propertiesDomain.put("member", "Organization");
		propertiesRange.put("member", "Person");
		numProperties++;
		properties.put("memberOf", new String[] { RDF + "Property" });
		propertiesDomain.put("memberOf", "Person");
		propertiesRange.put("memberOf", "Organization");
		numProperties++;
		properties.put("worksFor", new String[] { RDF + "Property", "memberOf",
				"memberOf" });
		propertiesDomain.put("worksFor", "Person");
		propertiesRange.put("worksFor", "University");
		numProperties++;
		properties.put("headOf", new String[] { RDF + "Property", "worksFor",
				"memberOf" });
		propertiesDomain.put("headOf", "Person");
		propertiesRange.put("headOf", "University");
		numProperties++;
		properties.put("listedCourse", new String[] { RDF + "Property" });
		propertiesDomain.put("listedCourse", "Schedule");
		propertiesRange.put("listedCourse", "Course");
		numProperties++;
		properties.put("subOrganizationOf", new String[] { RDF + "Property" });
		propertiesDomain.put("subOrganizationOf", "Organization");
		propertiesRange.put("subOrganizationOf", "Organization");
		numProperties++;
		properties.put("orgPublication", new String[] { RDF + "Property" });
		propertiesDomain.put("orgPublication", "Organization");
		propertiesRange.put("orgPublication", "Pubblication");
		numProperties++;
		properties.put("publicationAuthor", new String[] { RDF + "Property" });
		propertiesDomain.put("publicationAuthor", "Publication");
		propertiesRange.put("publicationAuthor", "Person");
		numProperties++;
		// properties.put("publicationDate", new String[] {RDF+"Property"});
		// propertiesDomain.put("publicationDate","Publication" );
		// propertiesRange.put("publicationDate", "Date");
		numProperties++;
		properties
				.put("publicationResearch", new String[] { RDF + "Property" });
		propertiesDomain.put("publicationResearch", "Publication");
		propertiesRange.put("publicationResearch", "Research");
		numProperties++;
		properties.put("softwareDocumentation",
				new String[] { RDF + "Property" });
		propertiesDomain.put("softwareDocumentation", "Software");
		propertiesRange.put("softwareDocumentation", "Publication");
		numProperties++;
		// properties.put("softwareVersion", new String[] {RDF+"Property"});
		// propertiesDomain.put("softwareVersion","Software" );
		// propertiesRange.put("softwareVersion", "Number");
		numProperties++;
		properties.put("takesCourse", new String[] { RDF + "Property" });
		propertiesDomain.put("takesCourse", "Student");
		propertiesRange.put("takesCourse", "Course");
		numProperties++;
		properties.put("teacherOf", new String[] { RDF + "Property" });
		propertiesDomain.put("teacherOf", "Faculty");
		propertiesRange.put("teacherOf", "Course");
		numProperties++;
		properties
				.put("teachingAssistantOf", new String[] { RDF + "Property" });
		propertiesDomain.put("teachingAssistantOf",
				"University Teaching Assistant");
		propertiesRange.put("teachingAssistantOf", "Course");
		numProperties++;
		properties.put("researchProject", new String[] { RDF + "Property" });
		propertiesDomain.put("researchProject", "Research Group");
		propertiesRange.put("researchProject", "Research Work");
		numProperties++;
		// properties.put("tenured", new String[] {RDF+"Property"});
		// propertiesDomain.put("tenured","Professor" );
		// propertiesRange.put("tenured", "Boolean");
		numProperties++;

		System.out.println(numProperties);
	}

	private static void inizializeOntology() {
		ontology = new HashMap<String, String[]>();

		ontology.put("Organization", new String[] { RDFS + "Resource" });
		numClasses++;
		ontology.put("Institute", new String[] { "Organization",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Program", new String[] { "Organization",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Research Group", new String[] { "Organization",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("School",
				new String[] { "Organization", RDFS + "Resource" });
		numClasses++;

		ontology.put("University", new String[] { "Organization",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("University Department", new String[] { "Organization",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Person", new String[] { RDFS + "Resource" });
		numClasses++;

		ontology.put("Chair", new String[] { "Person", "Professor",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Director", new String[] { "Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Employee", new String[] { "Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Faculty Member", new String[] { "Faculty Member",
				"Employee", "Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Lecturerr", new String[] { "Faculty Member", "Employee",
				"Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Post Doctorate", new String[] { "Faculty Member",
				"Employee", "Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Professor", new String[] { "Faculty Member", "Employee",
				"Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Assistant Professor", new String[] { "Professor",
				"Faculty Member", "Employee", "Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Associate Professor", new String[] { "Professor",
				"Faculty Member", "Employee", "Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Dean", new String[] { "Professor", "Faculty Member",
				"Employee", "Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Full Professor", new String[] { "Professor",
				"Faculty Member", "Employee", "Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Visiting Professor", new String[] { "Professor",
				"Faculty Member", "Employee", "Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Administrative Staff Worker", new String[] { "Employee",
				"Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Clerical Staff Worker", new String[] {
				"Administrative Staff Worker", "Employee", "Person",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("System Staff Worker", new String[] {
				"Administrative Staff Worker", "Employee", "Person",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Graduated Student", new String[] { "Person",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Student", new String[] { "Person", RDFS + "Resource" });
		numClasses++;

		ontology.put("Undergraduated Student", new String[] { "Person",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("University Research Assistant", new String[] { "Person",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("University Teaching Assistant", new String[] { "Person",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Pubblication", new String[] { RDFS + "Resource" });
		numClasses++;

		ontology.put("Book", new String[] { "Pubblication", RDFS + "Resource" });
		numClasses++;

		ontology.put("Article", new String[] { "Pubblication",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Conference Paper", new String[] { "Article",
				"Pubblication", RDFS + "Resource" });
		numClasses++;

		ontology.put("Journal Article", new String[] { "Article",
				"Pubblication", RDFS + "Resource" });
		numClasses++;

		ontology.put("Technical Report", new String[] { "Article",
				"Pubblication", RDFS + "Resource" });
		numClasses++;

		ontology.put("Manual",
				new String[] { "Pubblication", RDFS + "Resource" });
		numClasses++;

		ontology.put("Published Specification", new String[] { "Pubblication",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Software", new String[] { "Pubblication",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Unnofficial Pubblication", new String[] { "Pubblication",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Schedule", new String[] { "Pubblication",
				RDFS + "Resource" });
		numClasses++;

		ontology.put("Work", new String[] { RDFS + "Resource" });
		numClasses++;

		ontology.put("Research Work",
				new String[] { "Work", RDFS + "Resource" });
		numClasses++;

		ontology.put("Teaching Course", new String[] { "Teaching Course",
				"Work", RDFS + "Resource" });
		numClasses++;

		ontology.put("Graduated Level Course", new String[] {
				"Teaching Course", "Work", RDFS + "Resource" });
		numClasses++;

		System.out.println(numClasses);
	}

	public static String[] subClassOf(String[] listString) {
		Set<String> retList = new HashSet<String>();
		for (String string : listString) {
			if (ontology.containsKey(string)) {
				for (String s : ontology.get(string)) {
					retList.add(UNIV_BENCH_NS + "#" + s);
				}
			} else
				retList.add(RDFS + "Resource");
		}
		String[] arr = retList.toArray(new String[retList.size()]);
		return arr;
	}

	public static String[] subPropertyOf(String s) {
		return properties.get(s);
	}

	public static String[] range(String p) {
		return new String[] { UNIV_BENCH_NS + "#"
				+propertiesRange.get(p.replace(
				UNIV_BENCH_NS + "#", "")) };

	}

	public static String[] domain(String p) {
		return new String[] { UNIV_BENCH_NS + "#"
				+ propertiesDomain.get(p.replace(UNIV_BENCH_NS + "#", "")) };

	}

	public static final String TYPE_PROPERTY = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
}
