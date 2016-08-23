package it.polimi.heaven.core.teststand.collector;

import it.polimi.heaven.core.teststand.events.HeavenEvent;
import it.polimi.heaven.core.teststand.events.HeavenResult;
import it.polimi.services.Memory;
import it.polimi.streaming.EventProcessor;
import it.polimi.streaming.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
@NoArgsConstructor
public class Receiver implements EventProcessor<Response> {

    private EventProcessor<HeavenEvent> collector;

    @Override
    public boolean process(Response response) {
        return collector.process(new HeavenResult(response, System.currentTimeMillis(), Memory.getMemoryUsage()));
    }

    @Override
    public boolean setNext(EventProcessor<?> ep) {
        try {
            this.collector = (EventProcessor<HeavenEvent>) ep;
            return true;
        } catch (ClassCastException cce) {
            log.error(cce.getMessage());
            return false;
        }
    }

    @Override
    public void startProcessing() {

    }

    @Override
    public void stopProcessing() {

    }
}
