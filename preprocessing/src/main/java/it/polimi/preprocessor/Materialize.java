/**
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.polimi.preprocessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lombok.extern.log4j.Log4j;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;

@Log4j
public class Materialize {

	public static void main(String[] args) throws Exception {
		Reasoner reasoner;

		Model m = FileManager.get().loadModel("src/main/resources/data/inference/univ-bench-rdfs.rdf", null, "RDF/XML");

		reasoner = ReasonerRegistry.getRDFSReasoner();
		InfModel infmodel = ModelFactory.createInfModel(reasoner, m);

		File file = new File("src/main/resources/data/inference/univ-bench-rdfs-materialized.rdf");

		try (FileOutputStream fop = new FileOutputStream(file)) {
			if (!file.exists()) {
				file.createNewFile();
			}
			infmodel.write(fop);

			fop.flush();
			fop.close();

		} catch (IOException e) {
			log.error(e.getMessage());
		}

		log.info("Done");
	}

}
