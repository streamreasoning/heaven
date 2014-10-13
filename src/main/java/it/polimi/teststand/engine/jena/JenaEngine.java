/**
 * Licensed to the Apache Software Foundation (ASF) under one
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

package it.polimi.teststand.engine.jena;

import it.polimi.enums.ExecutionStates;
import it.polimi.events.Experiment;
import it.polimi.events.StreamingEvent;
import it.polimi.events.result.StreamingEventResult;
import it.polimi.teststand.core.TestStand;
import it.polimi.teststand.engine.RSPEngine;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.InfModelImpl;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class JenaEngine extends RSPEngine {

	private static final String RULE_SET = "src/main/resource/data/inference/rules.rules";
	private static final String UNIV_BENCH_RDFS = "src/main/resource/data/inference/univ-bench-rdfs-without-datatype-materialized.rdfs";
	private static Model tbox_star, abox;
	private static InfModel abox_star;
	int i = 0;
	private Experiment currentExperiment;

	public JenaEngine(TestStand<RSPEngine> stand) {
		super(stand);
		super.stand = stand;
		this.name = "jena";
		FileManager.get().addLocatorClassLoader(
				JenaEngine.class.getClassLoader());

		// CARICO LA TBOX CHIUSA
		tbox_star = FileManager.get().loadModel(UNIV_BENCH_RDFS, null,
				"RDF/XML");
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		this.currentExperiment = stand.getCurrentExperiment();
		if (currentExperiment == null) {
			return false;
		} else {
			abox = ModelFactory.createMemModelMaker().createDefaultModel();

			for (String[] eventTriple : e.getEventTriples()) {
				Statement s = createStatement(eventTriple);
				abox.add(s);
			}

			Reasoner reasoner = getRDFSSimpleReasoner();
			// Reasoner reasoner = getReducedReasoner();

			InfGraph graph = reasoner.bindSchema(tbox_star.getGraph()).bind(
					abox.getGraph());
			abox_star = new InfModelImpl(graph);

			Set<String[]> statements = new HashSet<String[]>();
			StmtIterator iterator = abox_star.difference(tbox_star)
					.listStatements();
			while (iterator.hasNext()) {

				Triple t = iterator.next().asTriple();
				statements
						.add(new String[] { t.getSubject().toString(),
								t.getPredicate().toString(),
								t.getObject().toString() });
			}

			try {
				return collector.store(new StreamingEventResult(statements, e,
						currentExperiment.getOutputFileName()));
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}

		}
	}

	// TODO discuss about what reasoner
	private Reasoner getRDFSSimpleReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel,
				ReasonerVocabulary.RDFS_SIMPLE);
		return reasoner;
	}

	@Override
	public ExecutionStates startProcessing() {
		if (isStartable()) {
			return status = ExecutionStates.READY;
		} else

			return status = ExecutionStates.ERROR;
	}

	@Override
	public ExecutionStates stopProcessing() {
		if (isOn()) {
			return status = ExecutionStates.CLOSED;
		} else
			return status = ExecutionStates.ERROR;
	}

	@Override
	public ExecutionStates init() {
		Logger.getRootLogger().info("Nothing to do");
		return status = ExecutionStates.READY;
	}

	@Override
	public ExecutionStates close() {
		Logger.getRootLogger().info("Nothing to do");
		return status = ExecutionStates.CLOSED;
	}

	private Statement createStatement(String[] eventTriple) {
		Resource subject = ResourceFactory.createResource(eventTriple[0]);
		Property predicate = ResourceFactory.createProperty(eventTriple[1]);
		RDFNode object = ResourceFactory.createResource(eventTriple[2]);
		Statement s = ResourceFactory.createStatement(subject, predicate,
				object);
		return s;
	}

	@SuppressWarnings("unused")
	private Reasoner getReducedReasoner() {

		// TODO puo' essere un modo intelligente per applicar e regole come fa
		// dynamite secondo parametri diversi a set di triple diversi,
		// utilizzando differenti reasoner

		List<Rule> rules = Rule.rulesFromURL(RULE_SET);

		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		reasoner.setOWLTranslation(true); // not needed in RDFS case
		reasoner.setTransitiveClosureCaching(true);
		return reasoner;
	}

	public boolean isStartable() {
		return ExecutionStates.READY.equals(status)
				|| ExecutionStates.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionStates.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionStates.READY.equals(status);
	}
}
