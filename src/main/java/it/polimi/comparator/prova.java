package it.polimi.comparator;

import it.polimi.output.filesystem.FileManager;

import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.rdf.model.Model;

public class prova {

	public static void main(String[] args) {

			Model a = RDFDataMgr
				.loadDataset(
						FileManager.OUTPUT_FILE_PATH
								+ "jena/_Result_file1.trig")
				.getNamedModel("http://example.org/-1477357464");
			Model b = RDFDataMgr.loadDataset(
					FileManager.OUTPUT_FILE_PATH
							+ "/plain/_Result_file1.trig")
					.getNamedModel(
							"http://example.org/-1477357464");
			Model difference = a
					.difference(
							b);
			System.out.println(difference.isEmpty());
	}
}
