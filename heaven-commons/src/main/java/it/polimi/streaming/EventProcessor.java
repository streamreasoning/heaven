package it.polimi.streaming;

public interface EventProcessor<T> {

    boolean process(T event);

    boolean setNext(EventProcessor<?> ep);

    void startProcessing();

    void stopProcessing();

}
