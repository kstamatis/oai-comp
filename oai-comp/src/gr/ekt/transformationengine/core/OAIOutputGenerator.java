package gr.ekt.transformationengine.core;

import java.util.ArrayList;
import java.util.List;

import gr.ekt.transformationengine.core.RecordSet;

/**
 * 
 * @author Kosta Stamatis (kstamatis@ekt.gr) 
 * @author Nikos Houssos (nhoussos@ekt.gr) 
 * @copyright 2011 - National Documentation Center
 */
public abstract class OAIOutputGenerator {
	
    /**
     * This method generates the output file having a RecordSet
     * @param recordSet
     * @param outputFile
     * @return
     */
	public abstract ArrayList<String> generateOutput(RecordSet recordSet);
    
    /**
     * This method generates the output file having a RecordSet recursively obtained from a list
     * @param recordSetList
     * @param outputFile
     * @return
     */
    public ArrayList<String> generateOutput(List<RecordSet> recordSetList){
    	ArrayList<String> returnValue = new ArrayList<String>();
    	for (RecordSet recordSet : recordSetList){
    		returnValue.addAll(generateOutput(recordSet));
    	}
    	return returnValue;
    }
}
