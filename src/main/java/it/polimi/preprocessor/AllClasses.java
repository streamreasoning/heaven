package it.polimi.preprocessor;

import it.polimi.utils.FileUtils;

import java.io.FileOutputStream;
import java.io.IOException;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class AllClasses {
	static FileOutputStream fop;

	public static void main(String[] args) throws IOException {
		OntModel inputOriginal = ModelFactory.createOntologyModel();
		inputOriginal.read(FileUtils.UNIV_BENCH_RDFS_MODIFIED);

		ExtendedIterator<OntClass> it = inputOriginal.listClasses();

		while (it.hasNext()) {
			System.out.println(it.next());
		}

		// RDFDataMgr.write(fop, input, RDFFormat.NT) ;
	}

}
