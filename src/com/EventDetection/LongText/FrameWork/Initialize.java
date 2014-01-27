package com.EventDetection.LongText.FrameWork;

import java.util.ArrayList;
import java.util.List;

import com.EventDetection.Clustering.SinglePassClustering;
import com.EventDetection.DataStructure.Document;
import com.EventDetection.DataStructure.Event;
import com.EventDetection.HBase.QueryContent;
import com.EventDetection.MySQL.MySQLAPI;
import com.EventDetection.MySQL.QueryMySQL;
import com.EventDetection.MySQL.UpdateMySQL;
import com.EventDetection.Segment.Segment;
import com.EventDetection.TFIDF.TFIDF;
import com.EventDetection.Util.Voting;
/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */

public class Initialize {
    public double simThresh;
    public long startTime;
    public int timeInterval;

    public Initialize(double simThresh, long startTime, int timeInterval) {
	this.simThresh = simThresh;
	this.startTime = startTime;
	this.timeInterval = timeInterval;
    }
    /**
     * Event detection of first batch 
     * @param testFlag, whether to use test table in MySQL
     * @param seg, Chinese segmenter
     * @param mysql, instance of MySQL API
     * @param sqlName, MySQL connect command
     * @return 
     */
    public void run(boolean testFlag, Segment seg, MySQLAPI mysql,
	    String sqlName) {
	QueryMySQL qMySQL = new QueryMySQL();
	List<Document> docsWithinInterval = qMySQL.readDocsWithinInterval(
		mysql, sqlName, startTime, timeInterval);
	if (docsWithinInterval.size() == 0) {
	    Entry.lastBatchId++;
	    return;
	}
	QueryContent rc = new QueryContent();
	rc.getLongText(docsWithinInterval);
	Entry.totalDocsCount += docsWithinInterval.size();
	for (int i = 0; i < docsWithinInterval.size(); i++) {
	    Document doc = docsWithinInterval.get(i);
	    seg.getSegs(doc);
	}
	TFIDF tfidf = new TFIDF();
	tfidf.updateValue(docsWithinInterval, Entry.totalDocsCount);
	ArrayList<Event> events = new ArrayList<Event>();
	SinglePassClustering ca = new SinglePassClustering();
	for (int i = 0; i < docsWithinInterval.size(); i++)
	    ca.insertToClusters(docsWithinInterval.get(i), events, tfidf.IDF,
		    simThresh, Entry.lastBatchId);
	Voting vote = new Voting();
	vote.Vote4TopicType(events);
	UpdateMySQL uMySQL = new UpdateMySQL();
	uMySQL.updateEventIds(events, testFlag, mysql);
	uMySQL.updateEvents(events, testFlag, mysql);
	Entry.lastBatchId++;
    }
}
