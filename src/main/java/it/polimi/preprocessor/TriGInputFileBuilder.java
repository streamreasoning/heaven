package it.polimi.preprocessor;

import it.polimi.preprocessor.PreprocessingUtils.GrowForm;
import it.polimi.preprocessor.PreprocessingUtils.GrowPower;
import it.polimi.utils.FileUtils;
import it.polimi.utils.RDFSUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j;

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

@Log4j
public class TriGInputFileBuilder {
	static WritingTriG trig;
	private static Set<Statement> tempStatementGraph;
	private static Model inputOriginal, inputOnlyTypeOf, inputNoTypeOf;
	private static final String BASENAME = "inputTrig";

	public static void main(String[] args) {
		String file = "src/main/resources/data/input/big.nt";

		try {
			int growDistance = 3;
			int growFactor = 10000;
			int decreaseDistance = 10;
			int decreaseFactor = -10;
			int initialGraphSize = 1;
			boolean cleanInput = true;

			String outputFileName = BASENAME + "D" + growDistance + "GF" + growFactor + FileUtils.TRIG_FILE_EXTENSION;

			generate(file, outputFileName, cleanInput, initialGraphSize, GrowPower.LINEAR, growFactor, decreaseFactor, GrowForm.STEP, growDistance,
					decreaseDistance, 30000);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void generate(String inputFile, String outputFileName, boolean clean, int initialGraphSize, String grow, int growFactor,
			int decreaseFactor, String form, int growDistance, int decreaseDistance, int maxDim) throws IOException {

		inputOriginal = FileManager.get().loadModel(inputFile, null, "RDF/XML");
		inputOnlyTypeOf = inputOriginal.query(new SimpleSelector(null, RDF.type, (RDFNode) null));
		inputNoTypeOf = inputOriginal.difference(inputOnlyTypeOf);

		if (clean) {
			cleanInput();
		}

		FileUtils.createFolders(FileUtils.PREPROCESSING_FILE_PATH);
		File file = new File(FileUtils.PREPROCESSING_FILE_PATH + outputFileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		String eventIDBase = "<http://example.org/";
		int eventNumber = 0;
		int graphSize = initialGraphSize;
		int distance = growDistance;

		trig = new WritingTriG(new FileOutputStream(file, true));

		StmtIterator inputIterator = inputNoTypeOf.listStatements();

		int roundGraphSize = graphSize;
		int currentGraphSize = 0;
		int traveledDistance = 0;
		int max = (maxDim > 0) ? maxDim : 100000000;
		while (inputIterator.hasNext()) {

			traveledDistance = 0;
			if (roundGraphSize < max) {
				while (traveledDistance < distance) {

					cleanTemp();

					currentGraphSize = 0;
					writeHeader(eventIDBase, eventNumber); // write the header

					while (currentGraphSize < roundGraphSize) {

						if (inputIterator.hasNext()) {// else? may i can
							fillGraph(inputIterator.next());
							currentGraphSize += 3; // considering this plus the two typeOf
													// statmenets for a U graph
						} else {
							trig.EOF();
							trig.write("}");
							endWriting("No more statemens..");
							return;
						}
					}
					completeGraph();
					eventNumber++; // new event created
					traveledDistance++; // new step to the distance

				}

				roundGraphSize += growFactor;
				// next graph will be bigger
				log.info("New Size [" + roundGraphSize + "] Event Number [" + eventNumber + "]");
			} else {
				endWriting("Max Size Reached");
			}

		}

		trig.flush();

		trig.close();
	}

	private static void endWriting(String msg) throws IOException {
		log.info(msg);
		trig.flush();
		trig.close();
		return;
	}

	private static void cleanInput() {
		Model temp;
		for (String dp : RDFSUtils.DATATYPEPROPERTIES) {
			temp = inputOriginal.query(new SimpleSelector(null, ResourceFactory.createProperty(dp), (RDFNode) null));

			inputNoTypeOf = inputNoTypeOf.difference(temp);

			// write(FileUtils.PREPROCESSING_DATATYPE_FILE_PATH + dp.split("#")[1] + ".nt", temp);

		}

		for (String dp : RDFSUtils.EXCLUDED_PROPERTIES) {

			temp = inputOriginal.query(new SimpleSelector(null, ResourceFactory.createProperty(dp), (RDFNode) null));

			inputNoTypeOf = inputNoTypeOf.difference(temp);

			// write(FileUtils.PREPROCESSING_EXCLUDED_FILE_PATH + dp.split("#")[1] + ".nt", temp);

		}
	}

	private static void cleanTemp() {
		tempStatementGraph = new HashSet<Statement>();
	}

	private static void completeGraph() throws IOException {
		for (Statement nextStatement : tempStatementGraph) {
			trig.EOF();
			trig.write("<" + nextStatement.getSubject() + "> <" + nextStatement.getPredicate() + "> <" + nextStatement.getObject() + "> .");
		}
		trig.EOF();
		trig.write("}");
		trig.EOF();
	}

	private static void writeHeader(String eventIDBase, int eventNumber) throws IOException {
		trig.write((eventIDBase + eventNumber + "> {"));
	}

	private static void fillGraph(Statement nextStatement) {
		tempStatementGraph.add(nextStatement);
		Statement typeStatmentSubj = null, typeStatmentObj = null;
		if (!nextStatement.getSubject().isLiteral() && !nextStatement.getObject().isLiteral()) {
			StmtIterator typeIterSubj = inputOnlyTypeOf.listStatements(nextStatement.getSubject(), RDF.type, (RDFNode) null);
			StmtIterator typeIterObj = inputOnlyTypeOf.listStatements(nextStatement.getObject().asResource(), RDF.type, (RDFNode) null);
			while (typeIterSubj.hasNext() || typeIterObj.hasNext()) {
				typeStatmentSubj = (typeIterSubj.hasNext()) ? typeIterSubj.nextStatement() : typeStatmentSubj;
				typeStatmentObj = (typeIterObj.hasNext()) ? typeIterObj.nextStatement() : typeStatmentObj;
				avoidWGraphs(typeStatmentObj);
				avoidWGraphs(typeStatmentSubj);
			}
		}
	}

	public static void avoidWGraphs(Statement s) {

		for (Statement stmt : tempStatementGraph) {

			if (s.getPredicate().equals(RDF.type) && stmt.getPredicate().equals(RDF.type) && stmt.getSubject().equals(s.getSubject())) {
				// TODO get the lower
				log.debug("Avoid W Graphs " + s.asTriple().toString() + " " + stmt.asTriple().toString());
				return;
			}

		}

		tempStatementGraph.add(s);
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
