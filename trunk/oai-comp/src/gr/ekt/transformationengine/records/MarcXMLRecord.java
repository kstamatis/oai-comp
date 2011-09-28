package gr.ekt.transformationengine.records;

import gr.ekt.transformationengine.core.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Subfield;

/**
 * 
 * @author Kosta Stamatis (kstamatis@ekt.gr) 
 * @author Nikos Houssos (nhoussos@ekt.gr) 
 * @copyright 2011 - National Documentation Center
 */
public class MarcXMLRecord extends Record {

	public org.marc4j.marc.Record marcxmlRecord = null;
	HashMap<String, Object> params = new HashMap<String, Object>();

	// Define a static logger variable
	static Logger logger = Logger.getLogger(MarcXMLRecord.class);


	public MarcXMLRecord(org.marc4j.marc.Record marcxmlRecord) {

		this.marcxmlRecord = marcxmlRecord;
	}

	public String getByDatafield(String datafieldStr, char[] codeStrArray) {

		//System.out.println(datafieldStr);
		DataField datafield = (DataField)marcxmlRecord.getVariableField(datafieldStr);

		if (datafield == null)
			return null;

		String result = "";
		for (char codeStr : codeStrArray){
			if (datafield.getSubfield(codeStr) != null)
				result += datafield.getSubfield(codeStr).getData() + " - ";
		}

		return result.replace(" - ", "").trim();

	}

	public ArrayList<String> getByDatafieldArray(String datafieldStr, char[] codeStrArray) {
		ArrayList<String> resultArray = new ArrayList<String>();

		//System.out.println(datafieldStr);
		List datafields = marcxmlRecord.getVariableFields(datafieldStr);

		for (Object obj : datafields){
			DataField datafield = (DataField)obj;

			if (datafield == null)
				return null;

			String result = "";
			for (char codeStr : codeStrArray){
				if (datafield.getSubfield(codeStr) != null)
					result += datafield.getSubfield(codeStr).getData() + " - ";
			}

			resultArray.add(result.replace(" - ", "").trim());
		}

		return resultArray;
	}

	public DataField getByDatafield(String datafieldStr) {

		//System.out.println(datafieldStr);
		DataField datafield = (DataField)marcxmlRecord.getVariableField(datafieldStr);

		if (datafield == null)
			return null;


		return datafield;
	}

	public List<DataField> getByDatafieldArray(String datafieldStr) {

		//System.out.println(datafieldStr);
		List datafields = marcxmlRecord.getVariableFields(datafieldStr);

		if (datafields == null)
			return null;


		return datafields;
	}

	public String getByDatafield(String datafieldStr, char[] codeStrArray, char code, String value) {

		//System.out.println(datafieldStr);
		DataField datafield = (DataField)marcxmlRecord.getVariableField(datafieldStr);

		if (datafield == null)
			return null;

		if (datafield.getSubfield(code) != null){
			if (!datafield.getSubfield(code).getData().equals(value))
				return null;
		}


		String result = "";
		for (char codeStr : codeStrArray){
			if (datafield.getSubfields(codeStr) != null)
				for (Object subfield : datafield.getSubfields(codeStr))
					result += ((Subfield) subfield).getData() + " | ";
		}

		return result.trim();
	}

	public String getByControlField(String tag) {

		ControlField controlField = (ControlField)marcxmlRecord.getVariableField(tag);

		if (controlField == null)
			return null;

		return controlField.getData();
	}

	public void removeDatafield(String datafieldStr) {

		//System.out.println(datafieldStr);
		DataField datafield = (DataField)marcxmlRecord.getVariableField(datafieldStr);

		marcxmlRecord.removeVariableField(datafield);
	}

	public void addDatafield(String datafieldStr, String value){
		MarcFactory factory = MarcFactory.newInstance();

		if (datafieldStr.equals("101")){
			DataField df = factory.newDataField(datafieldStr, '0', ' ');
			df.addSubfield(factory.newSubfield('a', value));

			marcxmlRecord.addVariableField(df);
		}
		if (datafieldStr.equals("100")){
			DataField df = factory.newDataField(datafieldStr, ' ', ' ');
			df.addSubfield(factory.newSubfield('a', value));

			marcxmlRecord.addVariableField(df);
		}
		if (datafieldStr.equals("102")){
			DataField df = factory.newDataField(datafieldStr, '0', ' ');
			df.addSubfield(factory.newSubfield('a', value));

			marcxmlRecord.addVariableField(df);
		}
		else if (datafieldStr.equals("314")){
			DataField df = factory.newDataField(datafieldStr, '0', ' ');
			df.addSubfield(factory.newSubfield('a', value));

			marcxmlRecord.addVariableField(df);
		}


	}

	/* (non-Javadoc)
	 * @see gr.ekt.repositories.utils.classification.core.Record#getByName(java.lang.String)
	 */
	@Override
	public List<String> getByName(String elementName) {

		ArrayList<String> result = new ArrayList<String>();

		//TO-DO

		return result;
	}

	/* (non-Javadoc)
	 * @see gr.ekt.repositories.utils.classification.core.Record#printByName(java.lang.String)
	 */
	@Override
	public void printByName(String elementName) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see gr.ekt.repositories.utils.classification.core.Record#addField(java.lang.String, java.util.ArrayList)
	 */
	@Override
	public void addField(String fieldName, ArrayList<String> fieldValues) {

	}

	/* (non-Javadoc)
	 * @see gr.ekt.repositories.utils.classification.core.Record#removeField(java.lang.String)
	 */
	@Override
	public void removeField(String fieldName) {

	}

	public org.marc4j.marc.Record getMarcxmlRecord() {
		return marcxmlRecord;
	}

	public void setMarcxmlRecord(org.marc4j.marc.Record marcxmlRecord) {
		this.marcxmlRecord = marcxmlRecord;
	}
}
