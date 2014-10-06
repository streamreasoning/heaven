package it.polimi.teststand.engine.esper.plain;

import it.polimi.teststand.engine.jena.JenaEngine;
import it.polimi.teststand.utils.RDFSUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

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

		FileManager.get().addLocatorClassLoader(
				JenaEngine.class.getClassLoader());

		Model tbox_star = FileManager
				.get()
				.loadModel(
						"src/main/resource/data/inference/univ-bench-rdfs-without-datatype-materialized.rdfs",
						null, "RDF/XML"); // http://en.wikipedia.org/wiki/Tbox

		OntModel om = ModelFactory.createOntologyModel(
				OntModelSpec.RDFS_MEM_RDFS_INF, tbox_star);
		ExtendedIterator<OntProperty> pl = om.listOntProperties();
		while (pl.hasNext()) {
			OntProperty next = pl.next();
			if (RDFSUtils.isSchema(next.toString())) {
				continue;
			}
			ExtendedIterator<? extends OntProperty> spl = next
					.listSuperProperties();
			Set<String> supers = new HashSet<String>();
			supers.add(next.toString());
			supers.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#Property");

			String domain = next.getDomain() != null ? next.getDomain()
					.toString() : "";
			String range = next.getRange() != null ? next.getRange().toString()
					: "";

			while (spl.hasNext()) {
				OntProperty snext = spl.next();
				if (RDFSUtils.isSchema(snext.toString())) {
					continue;
				}
				supers.add(snext.toString());
				if (domain.isEmpty()) {
					domain = snext.getDomain() != null ? snext.getDomain()
							.toString() : "";
				}
				if (range.isEmpty()) {
					range = snext.getRange() != null ? snext.getRange()
							.toString() : "";
				}
			}
			if (domain.isEmpty()) {
				domain = "http://www.w3.org/2000/01/rdf-schema#Resource";
			}
			if (range.isEmpty()) {
				range = "http://www.w3.org/2000/01/rdf-schema#Resource";
			}
			String[] s = new String[supers.size()];
			properties.put(next.toString(), supers.toArray(s));
			propertiesDomain.put(next.toString(), domain);
			propertiesRange.put(next.toString(), range);
			numProperties++;
		}

		for (String k : properties.keySet()) {
			Logger.getRootLogger().debug(k + "   " + Arrays.deepToString(properties.get(k))
					+ " DOMAIN " + propertiesDomain.get(k) + " RANGE "
					+ propertiesRange.get(k));

		}
		Logger.getRootLogger().debug("NUM PROPERTIES :" + numProperties);
	}

	private static void inizializeOntology() {
		ontology = new HashMap<String, String[]>();

		FileManager.get().addLocatorClassLoader(
				JenaEngine.class.getClassLoader());

		Model tbox_star = FileManager
				.get()
				.loadModel(
						"src/main/resource/data/inference/univ-bench-rdfs-without-datatype-materialized.rdfs",
						null, "RDF/XML"); // http://en.wikipedia.org/wiki/Tbox

		OntModel om = ModelFactory.createOntologyModel(
				OntModelSpec.RDFS_MEM_RDFS_INF, tbox_star);
		ExtendedIterator<OntClass> cl = om.listClasses();
		while (cl.hasNext()) {
			OntClass next = cl.next();
			if (RDFSUtils.isSchemaClass(next.toString())) {
				continue;
			}
			Set<String> supers = new HashSet<String>();
			supers.add("http://www.w3.org/2000/01/rdf-schema#Resource");
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
			Logger.getRootLogger().debug(k + "   " + Arrays.deepToString(ontology.get(k)));
		}

		Logger.getRootLogger().debug("NUM CLASSES " + numClasses);
	}

	public static String[] subClassOf(String[] listString) {
		Set<String> retList = new HashSet<String>();
		for (String string : listString) {
			if (ontology.containsKey(string)) {
				for (String s : ontology.get(string)) {
					retList.add(s);
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
		return new String[] { UNIV_BENCH_NS + "#" + propertiesRange.get(p) };

	}

	public static String[] domain(String p) {
		return new String[] { UNIV_BENCH_NS + "#" + propertiesDomain.get(p) };

	}

	public static final String TYPE_PROPERTY = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
}
