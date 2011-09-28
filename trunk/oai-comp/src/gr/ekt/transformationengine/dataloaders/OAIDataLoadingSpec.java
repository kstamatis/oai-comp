package gr.ekt.transformationengine.dataloaders;

/**
 * 
 * @author Nikos Houssos (nhoussos@ekt.gr) 
 * @author Kosta Stamatis (kstamatis@ekt.gr)
 * @copyright 2011 - National Documentation Center
 */
public class OAIDataLoadingSpec extends DataLoadingSpec {

	static final int DEFAULT_MAX_RECORDS = 100;
	int maxRecords;
	int startRecord;
	String from = null;
	String to = null;
	
	/**
	 * 
	 */
	public OAIDataLoadingSpec() {
		this.maxRecords = DEFAULT_MAX_RECORDS;
		this.startRecord = 1;
	}

	/**
	 * @param maxRecords
	 */
	public OAIDataLoadingSpec(int maxRecords, int startRecord) {
		super();
		
		this.maxRecords = maxRecords;
		this.startRecord = startRecord;
	}
	
	public DataLoadingSpec generateNextLoadingSpec() {
		OAIDataLoadingSpec toReturn = new OAIDataLoadingSpec();
		toReturn.setStartRecord(this.getStartRecord() + getMaxRecords());
		
		return toReturn;
	}

	public int getMaxRecords() {
		return maxRecords;
	}

	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}

	public int getStartRecord() {
		return startRecord;
	}

	public void setStartRecord(int startRecord) {
		this.startRecord = startRecord;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
}
