package gr.ekt.transformationengine.records;

import java.util.ArrayList;
import java.util.List;

import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.search.HarvestedItemInfo;

import gr.ekt.transformationengine.core.Record;

/**
 * 
 * @author Kosta Stamatis (kstamatis@ekt.gr) 
 * @author Nikos Houssos (nhoussos@ekt.gr) 
 * @copyright 2011 - National Documentation Center
 */
public class DSpaceRecord extends Record {

	private HarvestedItemInfo dspaceHarvestedItemInfo;
	
	/**
	 * 
	 */
	public DSpaceRecord() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see gr.ekt.transformationengine.core.Record#getByName(java.lang.String)
	 */
	@Override
	public List<String> getByName(String elementName) {
		// TODO Auto-generated method stub
		
		ArrayList<String> toReturn = new ArrayList<String>();
		
		if (elementName.equals("TITLE")){
			DCValue[] values = dspaceHarvestedItemInfo.item.getMetadata("dc", "title", null, Item.ANY);
			for (DCValue value : values){
				toReturn.add(value.value);
			}
		}
		
		return toReturn;
	}

	/* (non-Javadoc)
	 * @see gr.ekt.transformationengine.core.Record#printByName(java.lang.String)
	 */
	@Override
	public void printByName(String elementName) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see gr.ekt.transformationengine.core.Record#removeField(java.lang.String)
	 */
	@Override
	public void removeField(String fieldName) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see gr.ekt.transformationengine.core.Record#addField(java.lang.String, java.util.ArrayList)
	 */
	@Override
	public void addField(String fieldName, ArrayList<String> fieldValues) {
		// TODO Auto-generated method stub

	}

	public HarvestedItemInfo getDspaceHarvestedItemInfo() {
		return dspaceHarvestedItemInfo;
	}

	public void setDspaceHarvestedItemInfo(HarvestedItemInfo dspaceHarvestedItemInfo) {
		this.dspaceHarvestedItemInfo = dspaceHarvestedItemInfo;
	}
}
