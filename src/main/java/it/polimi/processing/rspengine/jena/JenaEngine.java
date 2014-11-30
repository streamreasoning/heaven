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

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.teststand.core.TestStand;
import it.polimi.utils.FileUtils;
import it.polimi.utils.Memory;
import it.polimi.utils.RDFSUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j;

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
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

@Log4j
public class JenaEngine extends RSPEngine {

	private static Model tbox_star, abox;
	private static InfModel abox_star;
	int i = 0;
	private Experiment currentExperiment;

	public JenaEngine(String name, TestStand collector) {
		super(name, collector);
		FileManager.get().addLocatorClassLoader(JenaEngine.class.getClassLoader());

		// CARICO LA TBOX CHIUSA
		tbox_star = FileManager.get().loadModel(FileUtils.UNIV_BENCH_RDFS_MODIFIED, null, "RDF/XML");
	}

	@Override
	public boolean process(RSPEvent e) {
		if (currentExperiment != null) {

			abox = ModelFactory.createMemModelMaker().createDefaultModel();
			Statement s;
			for (String[] eventTriple : e.getEventTriples()) {
				Logger.getRootLogger().debug(Arrays.deepToString(eventTriple));
				s = createStatement(eventTriple);
				abox.add(s);
			}

			Reasoner reasoner = getRDFSSimpleReasoner();

			InfGraph graph = reasoner.bindSchema(tbox_star.getGraph()).bind(abox.getGraph());
			abox_star = new InfModelImpl(graph);

			Set<String[]> statements = new HashSet<String[]>();
			StmtIterator iterator = abox_star.difference(tbox_star).listStatements();

			Triple t;
			String[] statementStrings;
			while (iterator.hasNext()) {
				t = iterator.next().asTriple();
				statementStrings = new String[] { t.getSubject().toString(), t.getPredicate().toString(), t.getObject().toString() };
				statements.add(statementStrings);
				log.debug(Arrays.deepToString(statementStrings));
			}

			try {
				e.setAll_triples(statements);
				e.setResultTimestamp(System.currentTimeMillis());
				e.setMemoryAR(Memory.getMemoryUsage());
				if (collector.store(e)) {
					return processDone();
				}
			} catch (IOException e1) {
				log.error(e1.getMessage());
				return false;
			}

		}
		return !processDone();
	}

	private Reasoner getRDFSSimpleReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
		return reasoner;
	}

	@Override
	public ExecutionState startProcessing() {
		if (isStartable()) {
			status = ExecutionState.READY;
		} else {
			status = ExecutionState.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionState stopProcessing() {
		if (isOn()) {
			status = ExecutionState.CLOSED;
		} else {
			status = ExecutionState.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionState init() {
		status = ExecutionState.READY;
		log.info("Status [" + status + "] Initializing JenaEngine [" + name + "]");
		return status;
	}

	@Override
	public ExecutionState close() {
		status = ExecutionState.CLOSED;
		log.info("Status [" + status + "] Closing JenaEngine");
		return status;
	}

	private Statement createStatement(String[] eventTriple) {
		Resource subject = ResourceFactory.createResource(eventTriple[0]);
		Property predicate = (eventTriple[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(eventTriple[1]) : RDF.type;
		RDFNode object = ResourceFactory.createResource(eventTriple[2]);
		return ResourceFactory.createStatement(subject, predicate, object);
	}

	public boolean isStartable() {
		return ExecutionState.READY.equals(status) || ExecutionState.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionState.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionState.READY.equals(status);
	}

	@Override
	public boolean processDone() {
		return true;
	}
}
