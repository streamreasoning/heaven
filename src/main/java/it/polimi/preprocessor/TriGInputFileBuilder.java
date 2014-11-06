package it.polimi.preprocessor;

import it.polimi.preprocessor.PreprocessingUtils.GrowForm;
import it.polimi.preprocessor.PreprocessingUtils.GrowPower;
import it.polimi.utils.FileUtils;
import it.polimi.utils.RDFSUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
	private static final String EVENTIDBASE = "<http://example.org/";

	public static void main(String[] args) {
		String file = "src/main/resources/data/input/big.nt";

		try {
			int growDistance = 1;
			int growFactor = 0;
			int decreaseDistance = 10;
			int decreaseFactor = -10;
			int initialGraphSize = 100;
			int maxDim = -1;
			int maxEvents = 1000;
			boolean cleanInput = true;
			boolean debug = false;
			String outputFileName = ((debug) ? "DEBUG_" : "") + BASENAME + "INIT" + initialGraphSize + "D" + growDistance + "GF" + growFactor;

			generate(file, outputFileName, cleanInput, initialGraphSize, GrowPower.LINEAR, growFactor, decreaseFactor, GrowForm.STEP, growDistance,
					decreaseDistance, maxDim, maxEvents, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void generate(String inputFile, String outputFileName, boolean clean, int initialGraphSize, String grow, int growFactor,
			int decreaseFactor, String form, int growDistance, int decreaseDistance, int maxDim, int maxEvents, boolean incremental)
			throws IOException {

		inputOriginal = FileManager.get().loadModel(inputFile, null, "RDF/XML");
		inputOnlyTypeOf = inputOriginal.query(new SimpleSelector(null, RDF.type, (RDFNode) null));
		inputNoTypeOf = inputOriginal.difference(inputOnlyTypeOf);

		if (clean) {
			cleanInput();
		}

		FileUtils.createFolders(FileUtils.PREPROCESSING_FILE_PATH);
		File file = new File(FileUtils.PREPROCESSING_FILE_PATH + outputFileName + ((incremental) ? "I" : "") + ((maxDim > 0) ? "MAX" + maxDim : "")
				+ FileUtils.TRIG_FILE_EXTENSION);
		if (!file.exists()) {
			file.createNewFile();
		}

		int eventNumber = 0;
		int graphSize = initialGraphSize;
		int distance = growDistance;

		trig = new WritingTriG(new FileWriter(file, true));

		StmtIterator inputIterator = inputNoTypeOf.listStatements();

		int roundGraphSize = graphSize;
		int currentGraphSize = 0;
		int traveledDistance = 0;
		int max = (maxDim > 0) ? maxDim : 100000000;
		int localMaxEvents = (maxEvents > 0) ? maxEvents : 1000;
		while (inputIterator.hasNext() && roundGraphSize < max && eventNumber < localMaxEvents) {

			traveledDistance = 0;

			while (traveledDistance < distance) {

				cleanTemp();

				currentGraphSize = 0;
				// write the header
				writeHeader(EVENTIDBASE, eventNumber);

				while (currentGraphSize < roundGraphSize) {

					// else? may i can
					if (inputIterator.hasNext()) {
						fillGraph(inputIterator.next());
						// considering this plus the two typeOf statmenets for a U graph
						currentGraphSize += 3;
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
			if (incremental) {
				inputIterator = inputNoTypeOf.listStatements();
				log.info("Incremental update, the Itarator is regenerated");

			}
			// next graph will be bigger
			log.info("New Size [" + roundGraphSize + "] Event Number [" + eventNumber + "]");

		}

		endWriting("Max Size Reached");

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
