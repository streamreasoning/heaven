package it.polimi.heaven.rsp.rsp;

import it.polimi.heaven.rsp.rsp.querying.ContinousQueryExecution;
import it.polimi.heaven.rsp.rsp.querying.Query;
import it.polimi.streaming.EventProcessor;
import it.polimi.streaming.Stimulus;


/**
 * @author Riccardo
 */
public interface RSPEngine extends EventProcessor<Stimulus> {

    ContinousQueryExecution registerQuery(Query q);

    // TODO is reasoning enabled
    // TODO is external time control enabled
}
