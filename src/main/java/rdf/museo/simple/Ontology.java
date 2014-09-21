package rdf.museo.simple;

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

	static{
		properties = new HashMap<String, String[]>();
		propertiesDomain = new HashMap<String, String>();
		propertiesRange = new HashMap<String, String>();
		initializeObjectProperties();
		inizializeOntology();
	}

	private static void initializeObjectProperties() {

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
