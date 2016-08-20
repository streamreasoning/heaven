package it.polimi.heaven.core.teststand.events;

import it.polimi.rdf.Line;
import it.polimi.streaming.Stimulus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class HeavenInput implements HeavenEvent {

    private String id;
    private String stream_name;
    private int eventNumber;
    private int experimentNumber;
    private long creation_timestamp;

    private long stimuli_application_timestamp;
    private long encoding_start_time;
    private long encoding_end_time;
    private double memory_usage_before_processing;
    private double engine_size_inmemory;
    private double teststand_size_inmemory;
    private double streamer_size_inmemory;

    private Stimulus[] stimuli;
    private Set<Line> lines;

    public HeavenInput(String id, String stream_name, int eventNumber, int experimentNumber, long stimuli_application_timestamp, Set<Line> lines) {
        this.id = id;
        this.stream_name = stream_name;
        this.eventNumber = eventNumber;
        this.experimentNumber = experimentNumber;
        this.stimuli_application_timestamp = stimuli_application_timestamp;
        this.creation_timestamp = System.currentTimeMillis();
        this.lines = lines;
    }

    public int size() {
        return lines.size();
    }

    public HeavenInput rebuild(String id, String stream_name, int eventNumber, int experimentNumber, long stimuli_application_timestamp,
                               Set<Line> lines) {
        this.id = id;
        this.stream_name = stream_name;
        this.eventNumber = eventNumber;
        this.experimentNumber = experimentNumber;
        this.stimuli_application_timestamp = stimuli_application_timestamp;
        this.creation_timestamp = System.currentTimeMillis();
        this.lines = lines;
        return this;
    }

    @Override
    public boolean save(String where) {
        return false;
    }
}
