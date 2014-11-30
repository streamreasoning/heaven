package it.polimi.processing.rspengine.esper.plain;

import it.polimi.processing.rspengine.jena.JenaEngine;
import it.polimi.utils.RDFSUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompletingOntology {

	private static Map<String, String[]> ontology;
	@Getter
	private static Map<String, String[]> properties;
	private static Map<String, String[]> propertiesRange;
	private static Map<String, String[]> propertiesDomain;
	private static int numProperties, numClasses = 0;
	private static String univBenchRdfs;
	@Getter
	private static int rangeCalls, domainCalls = 0;

	private static void initializeObjectProperties() {

		FileManager.get().addLocatorClassLoader(JenaEngine.class.getClassLoader());

		Model tboxStar = FileManager.get().loadModel(univBenchRdfs, null, "RDF/XML"); // http://en.wikipedia.org/wiki/Tbox

		OntModel om = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF, tboxStar);
		ExtendedIterator<OntProperty> pl = om.listOntProperties();
		while (pl.hasNext()) {
			OntProperty next = pl.next();
			System.err.println(next.toString());
			if (RDFSUtils.isSchema(next.toString())) {
				continue;
			}
			ExtendedIterator<? extends OntProperty> spl = next.listSuperProperties();
			Set<String> supers = new HashSet<String>();
			supers.add(next.toString());

			String domain = next.getDomain() != null ? next.getDomain().toString() : "";
			String range = next.getRange() != null ? next.getRange().toString() : "";
			while (spl.hasNext()) {
				OntProperty snext = spl.next();
				if (RDFSUtils.isSchema(snext.toString())) {
					continue;
				}
				supers.add(snext.toString());
			}
			if (domain.isEmpty()) {
				domain = RDFSUtils.RDFRESOURCE;
			}
			if (range.isEmpty()) {
				range = RDFSUtils.RDFRESOURCE;
			}
			String[] s = new String[supers.size()];
			String[] nextAsKey = new String[] { next.toString() };
			String deepToString = Arrays.deepToString(nextAsKey);
			properties.put(deepToString, supers.toArray(s));
			propertiesDomain.put(deepToString, new String[] { domain });
			propertiesRange.put(deepToString, new String[] { range });
			numProperties++;

		}

		for (String k : properties.keySet()) {
			log.debug(k + "   " + Arrays.deepToString(properties.get(k)) + " DOMAIN " + propertiesDomain.get(k) + " RANGE " + propertiesRange.get(k));

		}
		log.debug("NUM PROPERTIES :" + numProperties);
	}

	private static void inizializeOntology() {
		ontology = new HashMap<String, String[]>();

		FileManager.get().addLocatorClassLoader(JenaEngine.class.getClassLoader());

		Model tBoxStar = FileManager.get().loadModel(RDFSUtils.UNIV_BENCH_RDFS, null, "RDF/XML");

		OntModel om = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF, tBoxStar);
		ExtendedIterator<OntClass> cl = om.listClasses();
		while (cl.hasNext()) {
			OntClass next = cl.next();
			if (RDFSUtils.isSchemaClass(next.toString())) {
				continue;
			}
			Set<String> supers = new HashSet<String>();
			supers.add(RDFSUtils.RDFRESOURCE);
			ExtendedIterator<OntClass> scl = next.listSuperClasses();
			while (scl.hasNext()) {
				if (RDFSUtils.isSchemaClass(scl.toString())) {
					continue;
				}
				supers.add(scl.next().toString());
			}
			String[] s = new String[supers.size()];
			String[] strings = new String[] { next.toString() };
			ontology.put(Arrays.deepToString(strings), supers.toArray(s));
			numClasses++;
		}

		for (String k : ontology.keySet()) {
			log.debug(k + "   " + Arrays.deepToString(ontology.get(k)));
		}

		log.debug("NUM CLASSES " + numClasses);
	}

	public static String[] subClassOf(String[] classes) {
		Set<String> retList = new HashSet<String>();
		log.debug(classes);
		if (ontology.containsKey(classes)) {
			return ontology.get(classes);
		} else {

			for (String c : classes) {
				for (String sc : ontology.get(new String[] { c })) {
					retList.add(sc);
				}
			}

			retList.add(RDFSUtils.RDFRESOURCE);
		}
		String[] array = retList.toArray(new String[retList.size()]);
		ontology.put(Arrays.deepToString(classes), array);
		return array;
	}

	public static String[] subPropertyOf(String[] prop) {
		String[] ret;
		Set<String> retList = new HashSet<String>();

		if (properties.containsKey(prop)) {
			return properties.get(prop);
		} else {
			for (String p : prop) {
				String[] strings = properties.get(p);
				if (strings != null) {
					for (String sp : strings) {
						if (sp != null) {
							retList.add(sp);
						} else {
							log.info("Null property");
						}
					}
				}
			}
		}

		ret = new String[retList.size()];
		String[] array = retList.toArray(ret);
		properties.put(Arrays.deepToString(prop), array);
		log.info(Arrays.deepToString(array));
		return array;
	}

	public static String[] range(String[] properties) {
		log.debug("Range method call number [" + rangeCalls++ + "]");
		if (propertiesDomain.containsKey(properties)) {
			return propertiesDomain.get(properties);
		} else {
			String[] ret;
			Set<String> ranges = new HashSet<String>();
			for (String prop : properties) {
				if (prop != null) {
					String[] domainStrings = propertiesRange.get(new String[] { prop });
					if (domainStrings != null) {
						for (String d : domainStrings) {
							ranges.add(d);
						}
					}
				} else {
					log.info("Null domain");
				}
			}
			ret = new String[ranges.size()];

			String[] array = ranges.toArray(ret);
			propertiesRange.put(Arrays.deepToString(properties), array);
			return array;
		}
	}

	public static String[] domain(String[] properties) {
		log.debug("Range method call number [" + rangeCalls++ + "]");
		if (propertiesDomain.containsKey(properties)) {
			return propertiesDomain.get(properties);
		} else {

			String[] ret;
			Set<String> domains = new HashSet<String>();
			for (String prop : properties) {
				if (prop != null) {
					String[] domainStrings = propertiesDomain.get(new String[] { prop });
					for (String d : domainStrings) {
						domains.add(d);
					}
				} else {
					log.info("Null domain");
				}
			}
			ret = new String[domains.size()];

			String[] array = domains.toArray(ret);
			if (array == null || array.length == 0) {
				throw new RuntimeException("Null Domain");
			}
			propertiesDomain.put(Arrays.deepToString(properties), array);

			return array;
		}

	}

	public static boolean containsType(String[] p) {
		for (String s : p) {
			if (RDFSUtils.TYPE_PROPERTY.equals(s)) {
				return true;
			}
		}
		return false;

	}

	public static boolean notcontainsType(String[] p) {

		return !containsType(p);

	}

	public static String[] type() {
		return RDFSUtils.TYPE_PROPERTY_ARR;
	}

	public static void init(String onto) {
		univBenchRdfs = onto;
		properties = new HashMap<String, String[]>();
		propertiesDomain = new HashMap<String, String[]>();
		propertiesRange = new HashMap<String, String[]>();
		initializeObjectProperties();
		inizializeOntology();
	}
}
