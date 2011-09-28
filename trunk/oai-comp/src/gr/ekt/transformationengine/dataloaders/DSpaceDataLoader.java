package gr.ekt.transformationengine.dataloaders;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.dspace.content.DSpaceObject;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.search.Harvest;
import org.dspace.search.HarvestedItemInfo;

import gr.ekt.transformationengine.core.DataLoader;
import gr.ekt.transformationengine.core.OAIRecordSet;
import gr.ekt.transformationengine.records.DSpaceRecord;

/**
 * 
 * @author Kosta Stamatis (kstamatis@ekt.gr) 
 * @author Nikos Houssos (nhoussos@ekt.gr) 
 * @copyright 2011 - National Documentation Center
 */
public class DSpaceDataLoader extends DataLoader {

	private String configFile;
	
	/**
	 * 
	 */
	public DSpaceDataLoader() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see gr.ekt.transformationengine.core.DataLoader#loadData()
	 */
	@Override
	public OAIRecordSet loadData() {
		// TODO Auto-generated method stub
		OAIDataLoadingSpec dataLoadingSpec = ((OAIDataLoadingSpec)this.getLoadingSpec());
		return this.doRecordHarvest(dataLoadingSpec.getFrom(), dataLoadingSpec.getTo(), null, null, dataLoadingSpec.getStartRecord());
	}

	public OAIRecordSet loadData(String set, String metadataPrefix) {
		// TODO Auto-generated method stub
		OAIDataLoadingSpec dataLoadingSpec = ((OAIDataLoadingSpec)this.getLoadingSpec());
		return this.doRecordHarvest(dataLoadingSpec.getFrom(), dataLoadingSpec.getTo(), set, metadataPrefix, dataLoadingSpec.getStartRecord());
	}
	
	private OAIRecordSet doRecordHarvest(String from, String until, String set, String metadataPrefix, int offset)
	{
		OAIRecordSet recordSet = new OAIRecordSet();

		Context context = null;

		try {
			ConfigurationManager.loadConfig(configFile);

			context = new Context();

			// Get the relevant HarvestedItemInfo objects to make headers
			DSpaceObject scope = null;//resolveSet(context, set);

			List itemInfos = Harvest.harvest(context, scope, from, until,
					offset, ((OAIDataLoadingSpec)this.getLoadingSpec()).getMaxRecords(), // Limit amount returned from one
					// request
					true, true, false); // Need items, containers + withdrawals

			// Build list of XML records from item info objects
			Iterator i = itemInfos.iterator();


			while (i.hasNext())
			{
				HarvestedItemInfo itemInfo = (HarvestedItemInfo) i.next();

				DSpaceRecord record = new DSpaceRecord();
				record.setDspaceHarvestedItemInfo(itemInfo);
				recordSet.addRecord(record);
			}


			// If we have MAX_RECORDS records, we need to provide a resumption
				// token
				if (recordSet.getRecords().size() >= ((OAIDataLoadingSpec)this.getLoadingSpec()).getMaxRecords())
				{
					String resumptionToken = makeResumptionToken(from, until, set,
							metadataPrefix, offset + ((OAIDataLoadingSpec)this.getLoadingSpec()).getMaxRecords());

					recordSet.setResumptionMap(getResumptionMap(resumptionToken));

					//results.put("resumptionToken", resumptionToken);
				}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return recordSet;
	}
	
    private String makeResumptionToken(String from, String until, String set,
            String prefix, int offset)
    {
        StringBuffer token = new StringBuffer();

        if (from != null)
        {
            token.append(from);
        }
        else {
        	token.append("0001-01-01T00:00:00Z");
        }

        token.append("/");

        if (until != null)
        {
            token.append(until);
        }
        else {
        	token.append("9999-12-31T23:59:59Z");
        }

        token.append("/");

        if (set != null)
        {
            token.append(set);
        }

        token.append("/");

        if (prefix != null)
        {
            token.append(prefix);
        }

        token.append("/");
        token.append(String.valueOf(offset));

        return (token.toString());
    }
    
    public Map getResumptionMap(String resumptionToken) {
        return getResumptionMap(resumptionToken, -1, -1);
    }
    
    public Map getResumptionMap(String resumptionToken, int completeListSize, int cursor) {
        Map resumptionMap = null;
        if (resumptionToken != null) {
            resumptionMap = new HashMap();
            resumptionMap.put("resumptionToken", resumptionToken);
            //if (millisecondsToLive > 0) {
//              Date now = new Date();
                Date then = new Date((new Date()).getTime() + 60000);
                resumptionMap.put("expirationDate", this.createResponseDate(then));
            //}
            if (completeListSize >= 0) {
                resumptionMap.put("completeListSize", Integer.toString(completeListSize));
            }
            if (cursor >= 0) {
                resumptionMap.put("cursor", Integer.toString(cursor));
            }
        }
        return resumptionMap;
    }

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
    public String createResponseDate(Date date) {
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        formatter.setTimeZone(tz);
        sb.append(formatter.format(date));
        return sb.toString();
    }
}
