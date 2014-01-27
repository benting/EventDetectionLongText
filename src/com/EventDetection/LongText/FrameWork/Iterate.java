package com.EventDetection.LongText.FrameWork;

import java.util.List;

import com.EventDetection.Clustering.SinglePassClustering;
import com.EventDetection.DataStructure.Document;
import com.EventDetection.HBase.QueryContent;
import com.EventDetection.LongText.BreakPointRecovery.MostRecentBatchEvents;
import com.EventDetection.MySQL.MySQLAPI;
import com.EventDetection.MySQL.QueryMySQL;
import com.EventDetection.MySQL.UpdateMySQL;
import com.EventDetection.Segment.Segment;
import com.EventDetection.TFIDF.TFIDF;
import com.EventDetection.Util.ParseTime;
import com.EventDetection.Util.Voting;
/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */
public class Iterate {
    public double simThresh;
    public int timeInterval;

    public Iterate(double simThresh, int updateInteval) {
	this.simThresh = simThresh;
	this.timeInterval = updateInteval;
    }
    /**
     * Detect events iteratively after a constant time interval
     * @param testFlag, whether to use test table in MySQL
     * @param seg, Chinese segmenter
     * @param mysql, instance of MySQL API
     * @param sqlName, MySQL connect command
     * @param tfidf, instance of TFIDF, maintaining IDF table
     * @param recoverBatchNum, the number of most recent batches to be recovered
     * @return 
     */
    public void run(boolean testFlag, Segment seg, MySQLAPI mysql,
	    String sqlName, TFIDF tfidf, int recoverBatchNum) {
	QueryMySQL qMySQL = new QueryMySQL();
	List<Document> docsWithinInterval = qMySQL.readDocsWithinInterval(
		mysql, sqlName, Entry.timeIndex, timeInterval);
	if (docsWithinInterval.size() == 0) {
	    Entry.timeIndex += timeInterval;
	    Entry.lastBatchId++;
	    System.out.println("no docs\t"
		    + ParseTime.long2String(Entry.timeIndex));
	    return;
	}
	QueryContent rc = new QueryContent();
	rc.getLongText(docsWithinInterval);
	Entry.totalDocsCount += docsWithinInterval.size();
	for (int i = 0; i < docsWithinInterval.size(); i++) {
	    Document doc = docsWithinInterval.get(i);
	    seg.getSegs(doc);
	}
	tfidf.updateValue(docsWithinInterval, Entry.totalDocsCount);
	MostRecentBatchEvents mrbe = new MostRecentBatchEvents();
	mrbe.recover(Entry.lastBatchId, testFlag, seg, mysql, recoverBatchNum);
	SinglePassClustering ca = new SinglePassClustering();
	for (int i = 0; i < docsWithinInterval.size(); i++)
	    ca.insertToClusters(docsWithinInterval.get(i), mrbe.events,
		    tfidf.IDF, simThresh, Entry.lastBatchId);
	Voting vote = new Voting();
	vote.Vote4TopicType(mrbe.events);
	UpdateMySQL uMySQL = new UpdateMySQL();
	uMySQL.updateEventIds(mrbe.events, testFlag, mysql);
	uMySQL.updateEvents(mrbe.events, testFlag, mysql);
	Entry.timeIndex += timeInterval;
	Entry.lastBatchId++;
    }
}
