package it.polimi.processing.rspengine.esper.plain;

import it.polimi.processing.rspengine.jena.JenaEngine;
import it.polimi.utils.RDFSUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.AccessLevel;
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
public class Ontology {

	private static Map<String, String[]> ontology;
	private static Map<String, String[]> properties;
	private static Map<String, String> propertiesRange;
	private static Map<String, String> propertiesDomain;
	private static int numProperties, numClasses = 0;
	private static String univBenchRdfs;
	private static int rangeCalls, domainCalls = 0;

	private static void initializeObjectProperties() {

		FileManager.get().addLocatorClassLoader(JenaEngine.class.getClassLoader());

		Model tboxStar = FileManager.get().loadModel(univBenchRdfs, null, "RDF/XML"); // http://en.wikipedia.org/wiki/Tbox

		OntModel om = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF, tboxStar);
		ExtendedIterator<OntProperty> pl = om.listOntProperties();
		while (pl.hasNext()) {
			OntProperty next = pl.next();
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
			properties.put(next.toString(), supers.toArray(s));
			propertiesDomain.put(next.toString(), domain);
			propertiesRange.put(next.toString(), range);
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
			ontology.put(next.toString(), supers.toArray(s));
			numClasses++;
		}

		for (String k : ontology.keySet()) {
			log.debug(k + "   " + Arrays.deepToString(ontology.get(k)));
		}

		log.debug("NUM CLASSES " + numClasses);
	}

	public static String[] subClassOf(String[] listString) {
		Set<String> retList = new HashSet<String>();
		for (String keyClass : listString) {
			log.debug(keyClass);
			if (ontology.containsKey(keyClass)) {
				retList.addAll(Arrays.asList(ontology.get(keyClass)));
			} else {
				retList.add(RDFSUtils.RDFRESOURCE);
			}
		}
		return retList.toArray(new String[retList.size()]);
	}

	public static String[] subPropertyOf(String[] s) {
		String[] ret;
		Set<String> retList = new HashSet<String>();

		for (String p : s) {
			for (String sp : properties.get(p)) {
				if (sp != null) {
					retList.add(sp);
				} else {
					log.info("Null property");
				}
			}
		}
		ret = new String[retList.size()];
		return retList.toArray(ret);
	}

	public static String[] range(String[] p) {
		log.debug("Domain method call number [" + domainCalls++ + "]");
		String[] ret;
		Set<String> ranges = new HashSet<String>();
		for (String string : p) {
			if (p != null) {
				ranges.add(propertiesRange.get(string));
			} else {
				log.info("Null range");
			}
		}
		ret = new String[ranges.size()];
		return ranges.toArray(ret);

	}

	public static String[] domain(String[] p) {
		log.debug("Range method call number [" + rangeCalls++ + "]");
		String[] ret;
		Set<String> domains = new HashSet<String>();
		for (String string : p) {

			if (p != null) {
				domains.add(propertiesDomain.get(string));
			} else {
				log.info("Null domain");
			}
		}
		ret = new String[domains.size()];
		return domains.toArray(ret);

	}

	public static String[] range(String p) {
		log.info("Range method call number [" + rangeCalls++ + "]");
		return new String[] { propertiesDomain.get(p) };

	}

	public static String[] domain(String p) {
		log.info("Domain method call number [" + domainCalls++ + "]");
		return new String[] { propertiesDomain.get(p) };

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
		propertiesDomain = new HashMap<String, String>();
		propertiesRange = new HashMap<String, String>();
		initializeObjectProperties();
		inizializeOntology();
	}
}
