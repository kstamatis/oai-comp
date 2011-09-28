package gr.ekt.transformationengine.dataloaders;

import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import java.util.Vector;

import org.jzkit.search.provider.iface.IRQuery;
import org.jzkit.search.provider.iface.SearchException;
import org.jzkit.search.provider.iface.Searchable;
import org.jzkit.search.provider.z3950.Z3950ServiceFactory;
import org.jzkit.search.util.RecordModel.ArchetypeRecordFormatSpecification;
import org.jzkit.search.util.RecordModel.InformationFragment;
import org.jzkit.search.util.ResultSet.IRResultSet;
import org.jzkit.search.util.ResultSet.IRResultSetException;
import org.jzkit.search.util.ResultSet.IRResultSetStatus;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import gr.ekt.transformationengine.core.DataLoader;
import gr.ekt.transformationengine.core.OAIRecordSet;
import gr.ekt.transformationengine.records.MarcXMLRecord;

/**
 * 
 * @author Kosta Stamatis (kstamatis@ekt.gr) 
 * @author Nikos Houssos (nhoussos@ekt.gr) 
 * @copyright 2011 - National Documentation Center
 */
public class Z3950DataLoader extends DataLoader {

	private static ApplicationContext app_context = null;

	/**
	 * 
	 */
	public Z3950DataLoader() {
		// TODO Auto-generated constructor stub
	}

	public OAIRecordSet loadData(String set, String metadataPrefix) {
		return null;
	}
	/* (non-Javadoc)
	 * @see gr.ekt.transformationengine.core.DataLoader#loadData()
	 */
	@Override
	public OAIRecordSet loadData() {
		// TODO Auto-generated method stub
		OAIRecordSet recordSet = new OAIRecordSet();

		try {
			app_context = new ClassPathXmlApplicationContext( "TestApplicationContext.xml" );
			System.err.println("Got app context: "+app_context);
			if ( app_context == null ) 
				throw new RuntimeException("Unable to locate TestApplicationContext.xml definition file");

			System.err.println("Setting up Z3950 factory");
			Z3950ServiceFactory factory = new Z3950ServiceFactory("147.102.210.209",21211);
			factory.setApplicationContext(app_context);
			factory.setDefaultRecordSyntax("UNIMARC");
			factory.setDefaultElementSetName("F");
			factory.getRecordArchetypes().put("Default","unimarc::F");
			factory.getRecordArchetypes().put("FullDisplay","unimarc::F");
			factory.getRecordArchetypes().put("BriefDisplay","unimarc::B");
			factory.getRecordArchetypes().put("Holdings","unimarc::F");

			System.err.println("Build IR Query");
			IRQuery query = new IRQuery();
			query.collections = new Vector();
			query.collections.add("ADVANCE");
			query.query = new org.jzkit.search.util.QueryModel.PrefixString.PrefixString("@attrset bib-1 @attr 1=1016 TEE");

			// Quick subtest...
			//System.err.println("Test: convert to type 1 query");
			//Z3950QueryModel zqm = Type1QueryModelBuilder.buildFrom(app_context, query.query, "utf-8");
			//System.err.println("result of conversion back :"+zqm.toInternalQueryModel(app_context));


			System.err.println("Obtain instance from factory");
			Searchable s = factory.newSearchable();
			s.setApplicationContext(app_context);

			System.err.println("Evaluate query...");
			IRResultSet result = s.evaluate(query);

			System.err.println("Waiting for result set to complete, current status = "+result.getStatus());
			// Wait without timeout until result set is complete or failure
			result.waitForStatus(IRResultSetStatus.COMPLETE|IRResultSetStatus.FAILURE,0);

			System.err.println("Iterate over results (status="+result.getStatus()+"), count="+result.getFragmentCount());

			Enumeration e = new org.jzkit.search.util.ResultSet.ReadAheadEnumeration(result, new ArchetypeRecordFormatSpecification("Default"));
			for ( int i=0; ( ( e.hasMoreElements() ) && ( i < 60 ) ); i++) {
				System.err.println("Processing z3950 result "+i);
				InformationFragment f = (InformationFragment) e.nextElement();
				MarcReader reader = new MarcStreamReader(new ByteArrayInputStream((byte[])f.getOriginalObject()), "UTF-8");
				while (reader.hasNext()) {
					org.marc4j.marc.Record record = reader.next();
					recordSet.addRecord(new MarcXMLRecord(record));
				}
			}

			System.err.println("All done - testLOC()");
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (SearchException e) {
			e.printStackTrace();
		} catch (IRResultSetException e) {
			e.printStackTrace();
		}


		return null;
	}

}
