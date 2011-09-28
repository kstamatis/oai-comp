package gr.ekt.transformationengine.core;

import gr.ekt.transformationengine.conditions.Condition;
import gr.ekt.transformationengine.core.DataLoader;

import gr.ekt.transformationengine.dataloaders.OAIDataLoadingSpec;
import gr.ekt.transformationengine.exceptions.UnimplementedAbstractMethod;
import gr.ekt.transformationengine.exceptions.UnknownClassifierException;
import gr.ekt.transformationengine.exceptions.UnknownInputFileType;
import gr.ekt.transformationengine.exceptions.UnsupportedComparatorMode;
import gr.ekt.transformationengine.exceptions.UnsupportedCriterion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author Kosta Stamatis (kstamatis@ekt.gr) 
 * @author Nikos Houssos (nhoussos@ekt.gr) 
 * @copyright 2011 - National Documentation Center
 */
public class OAITransformationEngine  {


	DataLoader dataLoader = null;
	OAIOutputGenerator outputGenerator = null; //the output Generator
	TransformationWorkflow workflow = null;
	List<Condition> processingConditions = null;
	List<Condition> outputConditions = null;
	OAIRecordSet transformationResults = new OAIRecordSet(); 

	// Define a static logger variable
	static Logger logger = Logger.getLogger(OAITransformationEngine.class);

	public OAITransformationEngine(){
		this.processingConditions = new ArrayList<Condition>();
		this.outputConditions = new ArrayList<Condition>();
	}
	
	public OAITransformationEngine(OAIOutputGenerator outputGenerator){

		this.outputGenerator = outputGenerator;
		this.processingConditions = new ArrayList<Condition>();
		this.outputConditions = new ArrayList<Condition>();

	}

	public Map<String, Object> transform(String set, String metadataPrefix) throws UnknownClassifierException , UnknownInputFileType ,
	UnimplementedAbstractMethod, UnsupportedComparatorMode, UnsupportedCriterion {
		return this.transform(set, metadataPrefix, 0);
	}

	public Map<String, Object> transform(String set, String metadataPrefix, int offset) throws UnknownClassifierException , UnknownInputFileType ,
	UnimplementedAbstractMethod, UnsupportedComparatorMode, UnsupportedCriterion {
       
		((OAIDataLoadingSpec)dataLoader.getLoadingSpec()).setStartRecord(offset);
		
		OAIRecordSet recordSet = null;
		OAIRecordSet resultSet = null;
		
		boolean readyToProcess = false;
		boolean readyToOutput = false;
		
		while (!readyToOutput) {
			while (!readyToProcess) {
				recordSet = new OAIRecordSet(dataLoader.loadData(set, metadataPrefix));
				
				readyToProcess = true;
				for (Condition c : processingConditions) {
					if (c.check(recordSet) == false) {
						readyToProcess = false;
						dataLoader.setLoadingSpec(dataLoader.getLoadingSpec().generateNextLoadingSpec());
						break;
					}
				}
			}

			logger.info("Workflow started");

			logger.debug("Call process() for current processing step");
			resultSet = new OAIRecordSet(workflow.process(recordSet));
			transformationResults.addRecords(resultSet);

			logger.debug("Items in ResultSet: " + resultSet.getSize());

			readyToOutput = true;
			for (Condition c : outputConditions) {
				if (c.check(resultSet) == false) {
					readyToOutput = false;
					readyToProcess = false;
					dataLoader.setLoadingSpec(dataLoader.getLoadingSpec().generateNextLoadingSpec());
					break;
				}
			}
		}
		
		ArrayList<String> records = outputGenerator.generateOutput(transformationResults);

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("records", records.iterator());
		returnMap.put("resumptionMap", resultSet.getResumptionMap());
		logger.debug("Transformation Engine ended");

		return returnMap;
	}

	public void setOutputGenerator(OAIOutputGenerator outputGenerator) {
		this.outputGenerator = outputGenerator;
	}

	public void setDataLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	public TransformationWorkflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(TransformationWorkflow workflow) {
		this.workflow = workflow;
	}

	public List<Condition> getProcessingConditions() {
		return processingConditions;
	}

	public void setProcessingConditions(List<Condition> processingConditions) {
		this.processingConditions = processingConditions;
	}

	public List<Condition> getOutputConditions() {
		return outputConditions;
	}

	public void setOutputConditions(List<Condition> outputConditions) {
		this.outputConditions = outputConditions;
	}

	public OAIRecordSet getTransformationResults() {
		return transformationResults;
	}

	public void setTransformationResults(OAIRecordSet transformationResults) {
		this.transformationResults = transformationResults;
	}

	public DataLoader getDataLoader() {
		return dataLoader;
	}

	public OAIOutputGenerator getOutputGenerator() {
		return outputGenerator;
	}
}