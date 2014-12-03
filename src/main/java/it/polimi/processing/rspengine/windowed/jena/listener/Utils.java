package it.polimi.processing.rspengine.windowed.jena.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;
import com.hp.hpl.jena.util.FileManager;

public class Utils {

	public static String serializeRDFFile(String filePath) throws Exception {
		File f = new File(filePath);
		Model m = ModelFactory.createDefaultModel();
		try {
			m.read(FileManager.get().open(f.getAbsolutePath()), null, "RDF/XML");
		} catch (Exception e) {
			try {
				m.read(FileManager.get().open(f.getAbsolutePath()), null, "TURTLE");
			} catch (Exception e1) {
				try {
					m.read(FileManager.get().open(f.getAbsolutePath()), null, "N-TRIPLE");
				} catch (Exception e2) {
					m.read(FileManager.get().open(f.getAbsolutePath()), null, "RDF/JSON");
				}
			}
		}
		StringWriter sw = new StringWriter();
		m.write(sw);
		return sw.toString();
	}

	public static String serializeJenaModel(Model model) throws Exception {
		StringWriter sw = new StringWriter();
		model.write(sw);
		return sw.toString();
	}

	public static String fileToString(String filePath) throws Exception {
		File f = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(f));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	public static Statement createJenaStatementFromString(String s, String separator) throws Exception {
		String list[] = s.split(separator);
		if (list[2].contains("^^")) {
			String litList[] = list[2].split("^^");
			return new StatementImpl(new ResourceImpl(list[0]), new PropertyImpl(list[1]), ModelFactory.createDefaultModel().createTypedLiteral(
					litList[0], litList[1].replace("\"", "")));
		} else {
			return new StatementImpl(new ResourceImpl(list[0]), new PropertyImpl(list[1]), new ResourceImpl(list[2]));
		}
	}

}
