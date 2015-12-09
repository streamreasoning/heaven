package it.polimi.heaven.citybench.ssnobservations;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.insight_centre.aceis.io.rdf.RDFFileManager;

import com.hp.hpl.jena.rdf.model.Property;

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
	private double congestion_level;
	private double estimatedTime;
	private String payload;

	public AarhusTrafficObservation(String obId, String streamID, Date obTimeStamp, String status, double avgMeasuredTime, double avgSpeed,
			double medianMeasuredTime, int vehicleCount, int extID, int reportID, double congestion_level, double estimatedTime, String payload,
			String serviceID) {
		super(obTimeStamp, obId, streamID, serviceID);
		this.status = status;
		this.avgMeasuredTime = avgMeasuredTime;
		this.avgSpeed = avgSpeed;
		this.medianMeasuredTime = medianMeasuredTime;
		this.vehicleCount = vehicleCount;
		this.extID = extID;
		this.reportID = reportID;
		this.payload = payload;
		this.congestion_level = congestion_level;
		this.estimatedTime = estimatedTime;

		Property hasValue = model.createProperty(RDFFileManager.saoPrefix + "hasValue");
		if (this.payload.contains("AvgSpeed"))
			this.observation.addLiteral(hasValue, this.avgSpeed);
		else if (payload.contains("VehicleCount")) {
			this.observation.addLiteral(hasValue, this.vehicleCount);
		} else if (payload.contains("MeasuredTime"))
			this.observation.addLiteral(hasValue, this.avgMeasuredTime);
		else if (payload.contains("EstimatedTime"))
			this.observation.addLiteral(hasValue, this.estimatedTime);
		else if (payload.contains("CongestionLevel"))
			this.observation.addLiteral(hasValue, this.congestion_level);
	}

}
