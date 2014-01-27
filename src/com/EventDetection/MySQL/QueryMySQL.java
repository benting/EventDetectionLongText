package com.EventDetection.MySQL;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.EventDetection.DataStructure.Document;
import com.EventDetection.DataStructure.Event;
import com.EventDetection.HBase.QueryContent;
import com.EventDetection.Segment.Segment;
import com.EventDetection.Util.ParseTime;
/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */
public class QueryMySQL {
    /**
     * Get the documents within given time interval
     * @param mysql, instance of MySQL API
     * @param sqlName, SQL command
     * @param startTime, start time of the time interval
     * @param timeInterval, the length of time interval
     * @return max batch ID
     */
    public List<Document> readDocsWithinInterval(MySQLAPI mysql,
	    String sqlName, long startTime, int timeInterval) {
	List<Document> intervalDocs = new ArrayList<Document>();
	String mainTableName = sqlName;
	try {
	    String start = ParseTime.long2String(startTime);
	    String end = ParseTime.long2String(startTime + timeInterval);
	    String sql = "select * from " + mainTableName
		    + " where post_time >= '" + start + "' AND post_time < '"
		    + end + "'";
	    ResultSet rs = mysql.executeQuery(sql);
	    while (rs.next()) {
		Document doc = new Document();
		doc.Id = rs.getString("Hbase_id");
		doc.title = rs.getString("title");
		doc.formattime = rs.getString("post_time");
		doc.topicType = Integer.parseInt(rs.getString("topic_id"));
		doc.unixtime = ParseTime.string2Long(doc.formattime);
		intervalDocs.add(doc);
	    }
	    this.sortDocs(intervalDocs);
	    return intervalDocs;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }
    
    private void sortDocs(List<Document> intervalDocs) {
	Collections.sort(intervalDocs, new Comparator<Document>() {
	    public int compare(Document a, Document b) {
		if (a.unixtime > b.unixtime)
		    return 1;
		else if (a.unixtime == b.unixtime)
		    return 0;
		else
		    return -1;
	    }
	});
    }

    /**
     * Get the set of events given a batch ID
     * @param batchId, batch ID
     * @param eventIds, the set of event IDs
     * @param testFlag, whether to use a test table in database
     * @param mysql, instance of MySQL API
     * @param recoverBatchNum, the number of batches to be recovered from database
     * @return true if no exceptions occur
     */
    public boolean getEventIdsWithBatchId(int batchId,
	    ArrayList<Integer> eventIds, boolean testFlag, MySQLAPI mysql,
	    int recoverBatchNum) {
	String eventTableName = "event";
	if (testFlag)
	    eventTableName += "_copy";
	String sql = "SELECT event_id FROM " + eventTableName
		+ " WHERE batch_id = " + batchId;
	try {
	    ResultSet rs = mysql.executeQuery(sql);
	    while (rs.next())
		eventIds.add(Integer.parseInt(rs.getString("event_id")));
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * Get the documents given an event ID
     * @param events, the list of restored events
     * @param batchId, the batch ID of the given event
     * @param eventId, given event ID
     * @param seg, Chinese segmenter
     * @param testFlag, whether to use a test table in database
     * @param mysql, instance of MySQL API
     * @return true if no exceptions occur
     */
    public boolean restoreEventWithId(ArrayList<Event> events, int batchId,
	    int eventId, Segment seg, boolean testFlag, MySQLAPI mysql) {
	String eventTableName = "document_event_pair";
	if (testFlag)
	    eventTableName += "_copy";
	String sql = "SELECT * FROM " + eventTableName + " WHERE event_id = "
		+ eventId;
	Event event = new Event();
	try {
	    ResultSet rs = mysql.executeQuery(sql);
	    while (rs.next()) {
		Document doc = new Document();
		doc.Id = rs.getString("hbase_id");
		doc.title = rs.getString("title");
		doc.formattime = rs.getString("post_time");
		doc.topicType = Integer.parseInt(rs.getString("topic_Type"));
		doc.unixtime = ParseTime.string2Long(doc.formattime);
		doc.oldDoc = true;
		if (event.starttime > doc.unixtime)
		    event.starttime = doc.unixtime;
		if (event.endtime < doc.unixtime)
		    event.endtime = doc.unixtime;
		event.docs.add(doc);
	    }
	    event.id = eventId;
	    event.batchID = batchId;
	    QueryContent rc = new QueryContent();
	    rc.getLongText(event.docs);
	    for (int i = 0; i < event.docs.size(); i++)
		seg.getSegs(event.docs.get(i));
	    events.add(event);
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }
}
