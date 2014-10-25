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

package it.polimi.processing.rspengine.jena;

import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.events.result.StreamingEventResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.teststand.core.TestStand;
import it.polimi.utils.FileUtils;
import it.polimi.utils.RDFSUtils;

import java.io.IOException;
import java.util.Arrays;
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
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

public class JenaEngineReducedRules extends RSPEngine {

	private static Model tbox_star, abox;
	private static InfModel abox_star;
	int i = 0;
	private Experiment currentExperiment;

	public JenaEngineReducedRules(String name, TestStand<RSPEngine> stand) {
		super(name, stand);
		super.stand = stand;
		FileManager.get().addLocatorClassLoader(
				JenaEngineReducedRules.class.getClassLoader());

		// CARICO LA TBOX CHIUSA
		tbox_star = FileManager.get().loadModel(
				FileUtils.UNIV_BENCH_RDFS_MODIFIED, null, "RDF/XML");
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		this.currentExperiment = stand.getCurrentExperiment();
		if (currentExperiment == null) {
			return false;
		} else {
			abox = ModelFactory.createMemModelMaker().createDefaultModel();

			for (String[] eventTriple : e.getEventTriples()) {
				Logger.getRootLogger().debug(Arrays.deepToString(eventTriple));
				Statement s = createStatement(eventTriple);
				abox.add(s);
			}

			Reasoner reasoner = getReducedReasoner();

			InfGraph graph = reasoner.bindSchema(tbox_star.getGraph()).bind(
					abox.getGraph());
			abox_star = new InfModelImpl(graph);

			Set<String[]> statements = new HashSet<String[]>();
			StmtIterator iterator = abox_star.difference(tbox_star)
					.listStatements();
			while (iterator.hasNext()) {

				Triple t = iterator.next().asTriple();
				String[] statementStrings = new String[] {
						t.getSubject().toString(), t.getPredicate().toString(),
						t.getObject().toString() };
				statements.add(statementStrings);
				Logger.getLogger("obqa").debug(
						Arrays.deepToString(statementStrings));
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
		Property predicate = (eventTriple[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory
				.createProperty(eventTriple[1]) : RDF.type;
		RDFNode object = ResourceFactory.createResource(eventTriple[2]);
		Statement s = ResourceFactory.createStatement(subject, predicate,
				object);
		return s;
	}

	private Reasoner getReducedReasoner() {

		// TODO puo' essere un modo intelligente per applicar e regole come fa
		// dynamite secondo parametri diversi a set di triple diversi,
		// utilizzando differenti reasoner

		List<Rule> rules = Rule.rulesFromURL(FileUtils.RULE_SET);

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
