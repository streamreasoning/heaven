package it.polimi.preprocessor;

import it.polimi.utils.FileUtils;
import it.polimi.utils.RDFSUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

public class NTInputFileBuilder {
	static FileOutputStream fop;

	public static void main(String[] args) throws IOException {
		Model inputOriginal = FileManager.get().loadModel("src/main/resources/data/input/University0_1.nt", null, "RDF/XML");

		Model typeOf = inputOriginal.query(new SimpleSelector(null, RDF.type, (RDFNode) null));
		Model input = inputOriginal.difference(typeOf);

		write("type.nt", typeOf);

		Model temp;
		for (String dp : RDFSUtils.DATATYPEPROPERTIES) {
			temp = inputOriginal.query(new SimpleSelector(null, ResourceFactory.createProperty(dp), (RDFNode) null));

			input = input.difference(temp);

			write(FileUtils.PREPROCESSING_DATATYPE_FILE_PATH + dp.split("#")[1] + ".nt", temp);

		}

		for (String dp : RDFSUtils.EXCLUDED_PROPERTIES) {

			temp = inputOriginal.query(new SimpleSelector(null, ResourceFactory.createProperty(dp), (RDFNode) null));

			input = input.difference(temp);

			write(FileUtils.PREPROCESSING_EXCLUDED_FILE_PATH + dp.split("#")[1] + ".nt", temp);

		}

		File file = new File(FileUtils.PREPROCESSING_FILE_PATH + "University0_0_clean.nt");
		if (!file.exists()) {
			file.createNewFile();
		}

		fop = new FileOutputStream(file, true);
		int count = 0;
		StmtIterator inputIterator = input.listStatements();
		while (inputIterator.hasNext()) {
			Statement nextStatement = inputIterator.next();
			Statement typeStatmentSubj = null, typeStatmentObj = null;
			if (!nextStatement.getSubject().isLiteral() && !nextStatement.getObject().isLiteral()) {
				StmtIterator typeIterSubj = typeOf.listStatements(nextStatement.getSubject(), RDF.type, (RDFNode) null);
				StmtIterator typeIterObj = typeOf.listStatements(nextStatement.getObject().asResource(), RDF.type, (RDFNode) null);
				while (typeIterSubj.hasNext() || typeIterObj.hasNext()) {
					typeStatmentSubj = (typeIterSubj.hasNext()) ? typeIterSubj.nextStatement() : typeStatmentSubj;
					typeStatmentObj = (typeIterObj.hasNext()) ? typeIterObj.nextStatement() : typeStatmentObj;

					if (typeStatmentObj != null && typeIterSubj != null) {
						fop.write(("<" + typeStatmentSubj.getSubject() + "> <" + typeStatmentSubj.getPredicate() + "> <"
								+ typeStatmentSubj.getObject() + "> .").getBytes());
						fop.write(System.getProperty("line.separator").getBytes());
						fop.write(("<" + typeStatmentObj.getSubject() + "> <" + typeStatmentObj.getPredicate() + "> <" + typeStatmentObj.getObject() + "> .")
								.getBytes());
						fop.write(System.getProperty("line.separator").getBytes());
						fop.write(("<" + nextStatement.getSubject() + "> <" + nextStatement.getPredicate() + "> <" + nextStatement.getObject() + "> .")
								.getBytes());
						fop.write(System.getProperty("line.separator").getBytes());
						count += 1;
					}
				}
			}

		}
		fop.flush();

		fop.close();
		System.out.println(count);
		// RDFDataMgr.write(fop, input, RDFFormat.NT) ;
	}

	public static FileOutputStream write(String fileName, Model m) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file, true);

		RDFDataMgr.write(fop, m, RDFFormat.NT);
		return fop;
	}

}
