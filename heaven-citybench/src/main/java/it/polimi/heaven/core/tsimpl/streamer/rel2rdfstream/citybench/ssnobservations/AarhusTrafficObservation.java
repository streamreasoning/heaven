package it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations;

import it.polimi.heaven.core.ts.data.TripleContainer;
import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.CityBenchUtils;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

//Fields  status,avgMeasuredTime,avgSpeed,extID,medianMeasuredTime,TIMESTAMP,vehicleCount,_id,REPORT_ID

@Data
@EqualsAndHashCode(callSuper = false)
public class AarhusTrafficObservation extends SensorObservation {

	private static final long serialVersionUID = 1L;
	private String obId;
	private Date sysTimestamp;
	private Date obTimeStamp;

	private String status;
	private double avgMeasuredTime, avgSpeed, medianMeasuredTime;
	private int vehicleCount, extID, reportID;

	public AarhusTrafficObservation(String obId, String streamID, Date obTimeStamp, String status, double avgMeasuredTime, double avgSpeed,
			double medianMeasuredTime, int vehicleCount, int extID, int reportID) {
		super(obTimeStamp, obId, streamID);
		this.status = status;
		this.avgMeasuredTime = avgMeasuredTime;
		this.avgSpeed = avgSpeed;
		this.medianMeasuredTime = medianMeasuredTime;
		this.vehicleCount = vehicleCount;
		this.extID = extID;
		this.reportID = reportID;
	}

	@Override
	public Set<TripleContainer> getTriples() {
		Set<TripleContainer> s = super.getTriples();
		String id = CityBenchUtils.defaultPrefix + obId + UUID.randomUUID();
		s.add(new TripleContainer(id, CityBenchUtils.saoHasValue, "\"" + vehicleCount + "\"" + "^^xsd:integer"));
		s.add(new TripleContainer(id, CityBenchUtils.saoHasValue, "\"" + medianMeasuredTime + "\"" + "^^xsd:double"));
		s.add(new TripleContainer(id, CityBenchUtils.saoHasValue, "\"" + avgSpeed + "\"" + "^^xsd:double"));
		s.add(new TripleContainer(id, CityBenchUtils.saoHasValue, "\"" + vehicleCount + "\"" + "^^xsd:integer"));
		return s;
	}

}
