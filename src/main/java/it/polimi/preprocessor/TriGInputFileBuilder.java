package it.polimi.preprocessor;

import it.polimi.utils.FileUtils;
import it.polimi.utils.RDFSUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
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
	private static Map<String, Model> inputOriginalMap, inputOnlyTypeOfMap, inputNoTypeOfMap;
	private static Model inputOriginal, inputOnlyTypeOf, inputNoTypeOf;

	private static final String BASENAME = "inputTrig";
	private static final String EVENTIDBASE = "<http://example.org/";

	private static int growDistance = 1; // Number of GraphInTime to go up with graphSize
	private static int growFactor = 0; // = Max(size(G(t+growDistance)-G(t))) it influences only
										// the
										// spo
	// triple
	private static int startEventNumber = 0;
	private static int decreaseDistance = 10; // same of growDistance but decreasing
	private static int decreaseFactor = -10; // same of growfacotr but decreasing
	private static int initialGraphSize = 50; // start dimension of graphInTime
	private static int maxDim = -1; // max Dimension of GraphInTime -1 means disabled
	private static int maxEvents = 1000; // max events number for file (-1 means disabled)

	private static int sinkNumber = 1; // number of sinkFile from wich statements are taken, must
										// be <= of
	private static int sinkUnivDimension = 10; // dimension, in terms of universities of the nt lubm
												// file
	private static boolean cleanInput = false; // set the rebuild, at each iteration, of the
												// iterator
												// from
	// with take the spo triple (doesn't consume the sink)
	private static boolean debug = false; // append useful debug logs
	private static boolean regeneration = true;
	private static String path = FileUtils.PREPROCESSING_FILE_PATH + "UNIV10IGEN/";

	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		// String text = in.readLine();

		try {
			startEventNumber = 0;
			maxEvents = 1000;
			initialGraphSize = 500;
			sinkNumber = 2;
			growDistance = 1;
			growFactor = 0;
			generate();

		} catch (IOException e) {
			log.error(e.getMessage());
		}

	}

	private static void generate() throws IOException {
		populateSinks();

		trig = new WritingTriG(new FileWriter(outputFilePreparation(), true));
		StmtIterator inputIterator = getIterator();

		// local variable
		int eventNumber = startEventNumber;
		int graphSize = initialGraphSize;
		int distance = growDistance;
		int roundGraphSize = graphSize;
		int currentGraphSize = 0;
		int traveledDistance = 0;
		int localMaxEvents = (maxEvents > 0) ? maxEvents : (int) (inputNoTypeOf.size() / initialGraphSize);
		int maxSiz = (maxDim > 0) ? maxDim : (int) (inputNoTypeOf.size() / localMaxEvents);
		maxSiz = growFactor == 0 ? initialGraphSize + 1 : maxSiz;
		int realGF = 0;

		if (regeneration)
			log.info("Regeneration is active: max size [" + maxSiz + "] max number of events is [" + localMaxEvents + "]");

		while (inputIterator.hasNext() && roundGraphSize < maxSiz && eventNumber < localMaxEvents) {

			traveledDistance = 0;
			while (traveledDistance < distance) {
				log.info("Expected Graph Size[" + roundGraphSize + "]");

				tempStatementGraph = new HashSet<Statement>();

				currentGraphSize = 0;
				writeHeader(EVENTIDBASE, eventNumber);
				realGF = tempStatementGraph.size();
				while (currentGraphSize < roundGraphSize) {
					if (inputIterator.hasNext()) {
						currentGraphSize += fillGraph(inputIterator.next());
						// considering this plus the two typeOf statmenets for a U graph
					} else {
						trig.eof();
						trig.write("}");
						endWriting("No more statemens..");
						return;
					}
					log.debug("Current Graph Size [" + currentGraphSize + "]  RoundMaxGraphSize [" + roundGraphSize + "]");

				}

				eventNumber++; // new event created
				traveledDistance++; // new step to the distance
				log.info("Distance [ " + traveledDistance + " ] Event [ " + eventNumber + " ] Graph Size [ " + currentGraphSize + " ]");

				completeGraph();

			}

			log.debug("The TempStatment Graph has [ " + (tempStatementGraph.size() - realGF) + " ] new Statements");

			roundGraphSize += growFactor;

			inputIterator = regenerate(inputIterator);
			// next graph will be bigger

			log.debug("New Size [" + roundGraphSize + "] Event Number [" + eventNumber + "]");

		}

		endWriting("Max Size Reached");

	}

	private static StmtIterator regenerate(StmtIterator inputIterator) {
		if (regeneration) {
			inputIterator = getIterator();
			log.info("Incremental update, the Itarator is regenerated");

		}
		return inputIterator;
	}

	private static StmtIterator getIterator() {
		loadModel();
		return inputNoTypeOf.listStatements();
	}

	private static void loadModel() {

		Random r = new Random();
		int seed = r.nextInt(sinkNumber);

		String filenameOrURI = "_CLND_UNIV" + sinkUnivDimension + "INDEX" + seed * sinkUnivDimension + "SEED" + seed;
		String filenameWithPath = path + filenameOrURI + ".nt";

		if (inputOriginalMap.containsKey(filenameOrURI)) {

			inputOriginal = inputNoTypeOfMap.get(filenameOrURI);
			inputOnlyTypeOf = inputOnlyTypeOfMap.get(filenameOrURI);
			inputNoTypeOf = inputNoTypeOfMap.get(filenameOrURI);

		} else {
			inputOriginal = FileManager.get().loadModel(filenameWithPath, null, "RDF/XML");
			inputOnlyTypeOf = inputOriginal.query(new SimpleSelector(null, RDF.type, (RDFNode) null));
			inputNoTypeOf = inputOriginal.difference(inputOnlyTypeOf);

			if (cleanInput) {
				log.info("Cleaning the Model");
				inputNoTypeOf = cleanInput(inputNoTypeOf, inputOriginal);
			}

			inputOriginalMap.put(filenameOrURI, inputOriginal);
			inputOnlyTypeOfMap.put(filenameOrURI, inputOnlyTypeOf);
			inputNoTypeOfMap.put(filenameOrURI, inputNoTypeOf);

		}

		log.info("Load New Model [" + filenameOrURI + "]");

	}

	private static File outputFilePreparation() throws IOException {
		FileUtils.createFolders(FileUtils.PREPROCESSING_FILE_PATH);

		// OUTPUT FILE NAME
		String outputFileName = ((debug) ? "DEBUG_" : "") +

		BASENAME +

		"INIT" + initialGraphSize +

		"D" + growDistance +

		"GF" + growFactor +

		"SN" + sinkNumber +

		((regeneration) ? "R" : "") +

		((maxDim > 0) ? "MAX" + maxDim : "");

		//

		File file = new File(FileUtils.PREPROCESSING_FILE_PATH + outputFileName + FileUtils.TRIG_FILE_EXTENSION);

		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	private static void populateSinks() {
		log.info("Populate Sinks");
		inputNoTypeOfMap = new HashMap<String, Model>();
		inputOnlyTypeOfMap = new HashMap<String, Model>();
		inputOriginalMap = new HashMap<String, Model>();
		loadModel();
		log.info("Sinks Are Populated");
	}

	private static void endWriting(String msg) throws IOException {
		log.info(msg);
		trig.flush();
		trig.close();
		return;
	}

	private static Model cleanInput(Model noTypeOf, Model original) {
		Model temp;
		for (String dp : RDFSUtils.DATATYPEPROPERTIES) {
			temp = original.query(new SimpleSelector(null, ResourceFactory.createProperty(dp), (RDFNode) null));

			noTypeOf = noTypeOf.difference(original);

			// write(FileUtils.PREPROCESSING_DATATYPE_FILE_PATH + dp.split("#")[1] + ".nt", temp);

		}

		for (String dp : RDFSUtils.EXCLUDED_PROPERTIES) {

			temp = original.query(new SimpleSelector(null, ResourceFactory.createProperty(dp), (RDFNode) null));

			noTypeOf = noTypeOf.difference(temp);

			// write(FileUtils.PREPROCESSING_EXCLUDED_FILE_PATH + dp.split("#")[1] + ".nt", temp);

		}

		return noTypeOf;
	}

	private static void completeGraph() throws IOException {
		log.info("complete the graph");
		for (Statement nextStatement : tempStatementGraph) {
			trig.eof();
			trig.write("<" + nextStatement.getSubject() + "> <" + nextStatement.getPredicate() + "> <" + nextStatement.getObject() + "> .");
		}
		trig.eof();
		trig.write("}");
		trig.eof();
	}

	private static void writeHeader(String eventIDBase, int eventNumber) throws IOException {
		trig.write(eventIDBase + eventNumber + "> {");
	}

	private static int fillGraph(Statement nextStatement) {
		int count = 0;
		tempStatementGraph.add(nextStatement);
		count++;
		Statement typeStatmentSubj = null, typeStatmentObj = null;
		if (!nextStatement.getSubject().isLiteral() && !nextStatement.getObject().isLiteral()) {
			StmtIterator typeIterSubj = inputOnlyTypeOf.listStatements(new SimpleSelector(nextStatement.getSubject(), RDF.type, (RDFNode) null));
			StmtIterator typeIterObj = inputOnlyTypeOf.listStatements(new SimpleSelector(nextStatement.getObject().asResource(), RDF.type,
					(RDFNode) null));
			if (typeIterSubj.hasNext() || typeIterObj.hasNext()) {
				typeStatmentSubj = (typeIterSubj.hasNext()) ? typeIterSubj.nextStatement() : typeStatmentSubj;
				typeStatmentObj = (typeIterObj.hasNext()) ? typeIterObj.nextStatement() : typeStatmentObj;
				count += avoidWGraphs(typeStatmentObj);
				count += avoidWGraphs(typeStatmentSubj);
			}
		}
		return count;
	}

	private static int avoidWGraphs(Statement s) {
		for (Statement stmt : tempStatementGraph) {
			if (s.getPredicate().equals(RDF.type) && stmt.getPredicate().equals(RDF.type) && stmt.getSubject().equals(s.getSubject())) {
				// TODO get the lower
				log.debug("Avoid W Graphs " + s.asTriple().toString() + " " + stmt.asTriple().toString());
				return 0;
			}

		}
		tempStatementGraph.add(s);
		return 1;
	}

	private static FileOutputStream write(String fileName, Model m) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file, true);

		RDFDataMgr.write(fop, m, RDFFormat.NT);
		return fop;
	}

}
