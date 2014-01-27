package com.EventDetection.MySQL;

import java.sql.ResultSet;

import com.EventDetection.Util.ParseTime;
/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */
public class EnvironmentVariables {
    /**
     * Get the most recent (max) event ID in database
     * @param testFlag, whether to use test table in MySQL
     * @param mysql, instance of MySQL API
     * @return max event ID
     */
    public int getMostRecentEventId(boolean testFlag, MySQLAPI mysql) {
	String eventTableName = "event";
	if (testFlag)
	    eventTableName += "_copy";
	String sql = "SELECT MAX(event_id) FROM " + eventTableName;
	int count = 0;
	try {
	    ResultSet rs = mysql.executeQuery(sql);
	    if (rs.next()) {
		String res = rs.getString(1);
		if (res == null)
		    return count;
		count = Integer.parseInt(res);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return count;
    }
    
    /**
     * Get the most recent (max) batch ID in database
     * @param testFlag, whether to use test table in MySQL
     * @param mysql, instance of MySQL API
     * @return max batch ID
     */
    public int getMostRecentBatchId(boolean testFlag, MySQLAPI mysql) {
	String eventTableName = "event";
	if (testFlag)
	    eventTableName += "_copy";
	String sql = "SELECT MAX(batch_Id) FROM " + eventTableName;
	int count = -1;
	try {
	    ResultSet rs = mysql.executeQuery(sql);
	    if (rs.next()) {
		String batchId = rs.getString(1);
		if (batchId == null)
		    return count;
		count = Integer.parseInt(batchId);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return count;
    }

    /**
     * Get the most recent time of crawled news document in database
     * @param testFlag, whether to use test table in MySQL
     * @param mysql, instance of MySQL API
     * @return Unix time of document
     */
    public long getMostRecentTime(boolean testFlag, MySQLAPI mysql) {
	String eventTableName = "event";
	if (testFlag)
	    eventTableName += "_copy";
	String sql = "SELECT MAX(end_Time) FROM " + eventTableName;
	long count = -1;
	try {
	    ResultSet rs = mysql.executeQuery(sql);
	    if (rs.next()) {
		String date = rs.getString(1);
		if (date == null)
		    return count;
		count = ParseTime.string2Long(date);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return count;
    }
    
    /**
     * Get the count of processed documents in database
     * @param testFlag, whether to use test table in MySQL
     * @param mysql, instance of MySQL API
     * @return the count of documents
     */
    public int getProcessedDocsNum(boolean testFlag, MySQLAPI mysql) {
	String mainTableName = "document_event_pair";
	if (testFlag)
	    mainTableName += "_copy";
	String sql = "SELECT COUNT(event_id) FROM " + mainTableName;
	int count = 0;
	try {
	    ResultSet rs = mysql.executeQuery(sql);
	    if (rs.next()) {
		String count_s = rs.getString(1);
		if (count_s == null)
		    return count;
		count = Integer.parseInt(count_s);
	    }
	    return count;
	} catch (Exception e) {
	    e.printStackTrace();
	    return count;
	}
    }
}
