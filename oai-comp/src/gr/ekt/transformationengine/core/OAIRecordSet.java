package gr.ekt.transformationengine.core;

import java.util.Map;

/**
 * 
 * @author Kosta Stamatis (kstamatis@ekt.gr) 
 * @author Nikos Houssos (nhoussos@ekt.gr) 
 * @copyright 2011 - National Documentation Center
 */
public class OAIRecordSet extends RecordSet {

	private Map resumptionMap;
	
	/**
	 * Default constructor 
	 */
	public OAIRecordSet() {
	}

	public OAIRecordSet(RecordSet recordSet) {
		this.addRecords(recordSet);
	}
	
	public Map getResumptionMap() {
		return resumptionMap;
	}

	public void setResumptionMap(Map resumptionMap) {
		this.resumptionMap = resumptionMap;
	}
}
