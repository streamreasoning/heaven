package it.polimi.heaven.core.teststand.collector;

import it.polimi.heaven.core.teststand.data.Experiment;
import it.polimi.heaven.core.teststand.events.HeavenEvent;
import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.events.HeavenOutput;
import it.polimi.heaven.core.teststand.events.HeavenResult;
import it.polimi.services.FileService;
import lombok.Getter;

public final class ResultCollector {

    private String filePath;
    private HeavenOutput current_heaven_output;
    private int stimulus_number, response_number;
    @Getter
    private Experiment current_experiment;

    private boolean result_log_enabled, memory_log_enabled, latency_log_enabled;
    private String experiment_name;

    public ResultCollector(boolean result_log_enabled, boolean memory_log_enabled, boolean latency_log_enabled) {
        this.result_log_enabled = result_log_enabled;
        this.memory_log_enabled = memory_log_enabled;
        this.latency_log_enabled = latency_log_enabled;
    }

    public void setExperiment(Experiment e) {
        this.current_experiment = e;
        this.filePath = current_experiment.getOutputPath() + "exp" + current_experiment.getExperimentNumber() + "/";
        this.experiment_name = current_experiment.getName();
        FileService.createFolders(filePath);

    }

    public boolean process(HeavenEvent e) {
        return (e instanceof HeavenInput) ? process((HeavenInput) e) : process((HeavenResult) e);
    }

    public boolean process(HeavenInput e) {
        stimulus_number++;
        this.current_heaven_output = new HeavenOutput();

        this.current_heaven_output.setStimulus_number(stimulus_number);
        this.current_heaven_output.setStimuli_encoding_latency(e.getEncoding_end_time() - e.getEncoding_start_time());
        this.current_heaven_output.setMemory_before_processing(e.getMemory_usage_before_processing());

        return true;
    }

    public boolean process(HeavenResult e) {
        response_number++;
        this.current_heaven_output.setId("o_" + response_number);
        this.current_heaven_output.setResponse_number(response_number);
        this.current_heaven_output.setMemory_after_processing(e.getMemory_after_processing());
        this.current_heaven_output.setQuery_latency(e.getCreation_timestamp() - e.getResponseCreationTime());
        this.current_heaven_output.setQuery(e.getResponse().getQueryString());
        // TODO response decoding
        String where = filePath + experiment_name;
        e.save(where);
        current_heaven_output.save(where);
        return true;
    }
}
