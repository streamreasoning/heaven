package it.polimi.comparator;

import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.query.Dataset;

public class Prova {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Dataset jena = RDFDataMgr
				.loadDataset("src/main/resource/data/output/jena/_Result_file1.trig");
		Dataset plain = RDFDataMgr
				.loadDataset("src/main/resource/data/output/plain/_Result_file1.trig");
		// .getNamedModel(")));

		System.out
				.println(jena
						.getNamedModel(
								"[[http://www.Department1.University0.edu, http://swat.cse.lehigh.edu/onto/univ-bench.owl#subOrganizationOf, http://www.University0.edu]]")
						.isIsomorphicWith(
								plain.getNamedModel("[[http://www.Department1.University0.edu, http://swat.cse.lehigh.edu/onto/univ-bench.owl#subOrganizationOf, http://www.University0.edu]]")));
	}

}
