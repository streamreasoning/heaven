/**
 * Licensed to the Apache Software Foundation (ASF) under one
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

package it.polimi.processing.rspengine.jena;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.TestStandEvent;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.teststand.core.TestStand;
import it.polimi.utils.FileUtils;
import it.polimi.utils.RDFSUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j;

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

@Log4j
public class JenaEngineRhoDF extends RSPEngine<TestStandEvent> {

	private final Model tBoxStar;
	private Model abox;
	private InfModel aboxStar;
	int i = 0;
	private final String aBoxRuleset;

	public JenaEngineRhoDF(String name, String tbox, String ruleset_abox, ResultCollector<EventResult> collector) {
		super(name, collector);
		String localTbox = (tbox != null && !tbox.isEmpty()) ? tbox : FileUtils.UNIV_BENCH_RHODF_MODIFIED;
		this.aBoxRuleset = (ruleset_abox != null && !ruleset_abox.isEmpty()) ? ruleset_abox : FileUtils.RHODF_RULE_SET_RUNTIME;
		FileManager.get().addLocatorClassLoader(JenaEngineRhoDF.class.getClassLoader());

		// CARICO LA TBOX CHIUSA
		tBoxStar = FileManager.get().loadModel(localTbox, null, "RDF/XML");
	}

	public JenaEngineRhoDF(String name, TestStand<RSPEngine<TestStandEvent>> stand) {
		this(name, "", "", stand);
	}

	@Override
	public boolean sendEvent(TestStandEvent e) {
		abox = ModelFactory.createMemModelMaker().createDefaultModel();

		for (String[] eventTriple : e.getEventTriples()) {
			log.debug(Arrays.deepToString(eventTriple));
			Statement s = createStatement(eventTriple);
			abox.add(s);
		}

		Reasoner reasoner = getRHODFReasoner();

		InfGraph graph = reasoner.bindSchema(tBoxStar.getGraph()).bind(abox.getGraph());
		aboxStar = new InfModelImpl(graph);

		Set<String[]> statements = new HashSet<String[]>();
		Model difference = aboxStar.difference(tBoxStar);
		StmtIterator iterator = difference.listStatements();

		while (iterator.hasNext()) {
			Triple t = iterator.next().asTriple();
			String[] statementStrings = new String[] { t.getSubject().toString(), t.getPredicate().toString(), t.getObject().toString() };
			statements.add(statementStrings);
			log.debug(Arrays.deepToString(statementStrings));
		}

		try {
			return collector.store(collector.newEventInstance(statements, e));
		} catch (IOException e1) {
			log.error(e1.getMessage());
			return false;
		}
	}

	@Override
	public ExecutionStates startProcessing() {
		if (isStartable()) {
			this.status = ExecutionStates.READY;
		} else {
			this.status = ExecutionStates.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionStates stopProcessing() {
		if (isOn()) {
			this.status = ExecutionStates.CLOSED;

		} else {

			this.status = ExecutionStates.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionStates init() {
		log.info("Initializing " + name + "..Nothing to do");
		this.status = ExecutionStates.READY;
		return status;
	}

	@Override
	public ExecutionStates close() {
		log.info("Closing " + name + "..Nothing to do");
		this.status = ExecutionStates.CLOSED;
		return status;
	}

	private Statement createStatement(String[] eventTriple) {
		Resource subject = ResourceFactory.createResource(eventTriple[0]);
		Property predicate = (eventTriple[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(eventTriple[1]) : RDF.type;
		RDFNode object = ResourceFactory.createResource(eventTriple[2]);
		return ResourceFactory.createStatement(subject, predicate, object);
	}

	private Reasoner getRHODFReasoner() {
		return new GenericRuleReasoner(Rule.rulesFromURL(aBoxRuleset));
	}

	public boolean isStartable() {
		return ExecutionStates.READY.equals(status) || ExecutionStates.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionStates.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionStates.READY.equals(status);
	}
}
