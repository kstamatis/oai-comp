package gr.ekt.transformationengine;

import java.util.Iterator;
import java.util.Map;

import gr.ekt.transformationengine.core.OAITransformationEngine;
import gr.ekt.transformationengine.exceptions.UnimplementedAbstractMethod;
import gr.ekt.transformationengine.exceptions.UnknownClassifierException;
import gr.ekt.transformationengine.exceptions.UnknownInputFileType;
import gr.ekt.transformationengine.exceptions.UnsupportedComparatorMode;
import gr.ekt.transformationengine.exceptions.UnsupportedCriterion;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author Kosta Stamatis (kstamatis@ekt.gr) 
 * @author Nikos Houssos (nhoussos@ekt.gr) 
 * @copyright 2011 - National Documentation Center
 */
public class DSpaceDataLoaderTest {


	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-conf.xml");
		OAITransformationEngine te = (OAITransformationEngine) context.getBean("transformationEngine");

		try {
			Map<String, Object> result = te.transform(null, null, 2);

			Iterator iterator = (Iterator)result.get("records");
			if (iterator.hasNext()){
				System.out.println("Record = " + iterator.next());
			}

		} catch (UnknownClassifierException e) {
			e.printStackTrace();
		} catch (UnknownInputFileType e) {
			e.printStackTrace();
		} catch (UnimplementedAbstractMethod e) {
			e.printStackTrace();
		} catch (UnsupportedComparatorMode e) {
			e.printStackTrace();
		} catch (UnsupportedCriterion e) {
			e.printStackTrace();
		}
	}


}
