/**
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.polimi.preprocessor;

import it.polimi.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;

public class CompareringRhoDFandDefault {
	public static int i = 0;
	static Reasoner reasoner;

	public static void main(String[] args) throws Exception {
		// Streamer.stream(new ExampleONT_02());

		Model def = FileManager
				.get()
				.loadModel(
						"src/main/resources/data/inference/univ-bench-rdfs-without-datatype-materialized-modified.rdfs",
						null, "RDF/XML");

		Model rho = FileManager
				.get()
				.loadModel(
						"src/main/resources/data/inference/univ-bench-rdfs-without-datatype-materialized-rhodf-modified.rdf",
						null, "RDF/XML");

		File file = new File(
				"src/main/resources/data/inference/out_def-rho-3.rdf");

		try (FileOutputStream fop = new FileOutputStream(file)) {
			if (!file.exists()) {
				file.createNewFile();
			}
			(def.difference(rho)).write(fop);
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		file = new File("src/main/resources/data/inference/out_rho-def-3.rdf");

		try (FileOutputStream fop = new FileOutputStream(file)) {
			if (!file.exists()) {
				file.createNewFile();
			}
			(rho.difference(def)).write(fop);
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		Logger.getRootLogger().info("Done");
	}

	private static Reasoner getReducedReasoner() {

		// TODO puo' essere un modo intelligente per applicar e regole come fa
		// dynamite secondo parametri diversi a set di triple diversi,
		// utilizzando differenti reasoner

		List<Rule> rules = Rule.rulesFromURL(FileUtils.RULE_SET);

		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		reasoner.setOWLTranslation(true); // not needed in RDFS case
		reasoner.setTransitiveClosureCaching(true);
		return reasoner;
	}

}
