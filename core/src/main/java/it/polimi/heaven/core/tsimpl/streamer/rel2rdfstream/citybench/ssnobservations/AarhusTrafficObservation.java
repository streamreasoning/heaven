package it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations;

import it.polimi.heaven.core.ts.events.TripleContainer;

import java.util.Date;
import java.util.Set;

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
		// TODO Auto-generated method stub
		return null;
	}

}
