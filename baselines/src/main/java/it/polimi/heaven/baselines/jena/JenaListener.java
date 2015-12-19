package it.polimi.heaven.baselines.jena;

import it.polimi.heaven.baselines.enums.OntoLanguage;
import it.polimi.heaven.baselines.esper.RSPListener;
import it.polimi.heaven.baselines.jena.events.response.BaselineResponse;
import it.polimi.heaven.baselines.jena.events.response.ConstructResponse;
import it.polimi.heaven.baselines.jena.events.response.SelectResponse;
import it.polimi.heaven.baselines.jena.events.stimuli.BaselineStimulus;
import it.polimi.heaven.baselines.jena.query.BaselineQuery;
import it.polimi.heaven.baselines.utils.BaselinesUtils;
import it.polimi.heaven.core.enums.Reasoning;
import it.polimi.heaven.core.teststand.EventProcessor;
import it.polimi.heaven.core.teststand.data.RDFLine;
import it.polimi.heaven.core.teststand.rspengine.events.Response;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.event.map.MapEventBean;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.InfModelImpl;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

@Log4j
public class JenaListener implements RSPListener {

	protected Graph abox;
	protected Model TBoxStar;
	protected InfModel ABoxStar;

	protected Reasoner reasoner;
	protected final EventProcessor<Response> next;

	private final Set<RDFLine> ABoxTriples;

	@Setter
	@Getter
	private Reasoning reasoningType;
	@Setter
	@Getter
	private OntoLanguage ontoLang;
	private BaselineQuery bq;
	private Query q;
	private BaselineResponse current_response;
	private int response_number = 0;
	private String id_base;

	public JenaListener(EventProcessor<Response> next, BaselineQuery bq, Reasoning reasoningType, OntoLanguage ontoLang, String id_base) {
		this.next = next;
		this.bq = bq;
		this.q = QueryFactory.create(bq.getSparql_query());
		this.ontoLang = ontoLang;
		this.reasoningType = reasoningType;
		this.abox = bq.hasTBox() ? bq.getTbox().getGraph() : ModelFactory.createMemModelMaker().createDefaultModel().getGraph();
		this.TBoxStar = ModelFactory.createMemModelMaker().createDefaultModel();
		this.id_base = id_base;
		this.ABoxTriples = new HashSet<RDFLine>();
		this.reasoner = getReasoner(ontoLang);
		this.reasoner.bindSchema(TBoxStar.getGraph());
		this.ABoxStar = new InfModelImpl(reasoner.bind(abox));

	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {
		response_number++;
		IStreamUpdate(newData);

		DStreamUpdate(oldData);
		log.info("ciao");
		reasoner = getReasoner(ontoLang);
		reasoner.bindSchema(TBoxStar.getGraph());
		InfGraph graph = reasoner.bind(abox);
		ABoxStar = new InfModelImpl(graph);
		ABoxStar.rebind(); // forcing the reasoning to be executed

		if (q.isSelectType()) {
			QueryExecution exec = QueryExecutionFactory.create(q, ABoxStar);
			current_response = new SelectResponse("http://streamreasoning.org/heaven/", bq, exec.execSelect());

		} else if (q.isConstructType()) {
			QueryExecution exec = QueryExecutionFactory.create(q, ABoxStar);
			current_response = new ConstructResponse("http://streamreasoning.org/heaven/", bq, exec.execConstruct());

		}
		if (next != null) {
			log.debug("Send Event to the Receiver");
			next.process(current_response);
		}

	}

	private Reasoner getReasoner(OntoLanguage ontoLang) {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		switch (ontoLang) {
		case FULL:
			reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_FULL);
			break;
		case SMPL:
			reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
			break;
		default:
			reasoner = new GenericRuleReasoner(Rule.rulesFromURL(BaselinesUtils.RHODF_RULE_SET_RUNTIME));
		}

		return reasoner;

	}

	private void handleSingleIStream(BaselineStimulus underlying) {
		log.debug(underlying);
		ABoxStar = ModelFactory.createInfModel((InfGraph) underlying.addTo(ABoxStar.getGraph()));
		ABoxTriples.addAll(underlying.serialize());
	}

	private void IStreamUpdate(EventBean[] newData) {
		if (newData != null) {
			log.debug("[" + newData.length + "] New Events of type [" + newData[0].getUnderlying().getClass().getSimpleName() + "]");
			for (EventBean e : newData) {
				log.debug(e.getUnderlying().toString());
				if (e instanceof MapEventBean) {
					MapEventBean meb = (MapEventBean) e;
					if (meb.getProperties() instanceof BaselineStimulus) {
						handleSingleIStream((BaselineStimulus) e.getUnderlying());
					} else {

						for (int i = 0; i < meb.getProperties().size(); i++) {
							BaselineStimulus underlying = (BaselineStimulus) meb.get("stream_" + i);
							handleSingleIStream(underlying);
						}
					}
				}
			}
		}
	}

	private void handleSingleDStream(BaselineStimulus underlying) {
		log.debug(underlying);
		ABoxStar = ModelFactory.createInfModel((InfGraph) underlying.removeFrom(ABoxStar.getGraph()));
		ABoxTriples.removeAll(underlying.serialize());
	}

	private void DStreamUpdate(EventBean[] oldData) {
		if (oldData != null && Reasoning.INCREMENTAL.equals(reasoningType)) {
			log.debug("[" + oldData.length + "] Old Events of type [" + oldData[0].getUnderlying().getClass().getSimpleName() + "]");
			for (EventBean e : oldData) {
				if (e instanceof MapEventBean) {
					MapEventBean meb = (MapEventBean) e;
					if (meb.getProperties() instanceof BaselineStimulus) {
						handleSingleDStream((BaselineStimulus) e.getUnderlying());
					} else {

						for (int i = 0; i < meb.getProperties().size(); i++) {
							BaselineStimulus underlying = (BaselineStimulus) meb.get("stream_" + i);
							handleSingleDStream(underlying);
						}
					}
				}
			}
		}
	}
}