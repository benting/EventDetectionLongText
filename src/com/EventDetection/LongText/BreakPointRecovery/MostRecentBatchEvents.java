package com.EventDetection.LongText.BreakPointRecovery;

import java.util.ArrayList;

import com.EventDetection.DataStructure.Event;
import com.EventDetection.MySQL.MySQLAPI;
import com.EventDetection.MySQL.QueryMySQL;
import com.EventDetection.Segment.Segment;

/**
 * @author Junting Ye <yejuntingben@gmail.com>
 */
public class MostRecentBatchEvents {
    public ArrayList<Event> events = new ArrayList<Event>();
    /**
     * Retrieve events of the last batch from database
     * @param  lastBatchId, the most recent batch Id
     * @param  testFlag, whether it uses a test table in MySQL
     * @param  seg, Chinese segmenter
     * @param  mysql, MySQL instance
     * @param  recoverBatchNum, the number of most recent batches to recover from database
     * @return 
     */
    public void recover(int lastBatchId, boolean testFlag, Segment seg,
	    MySQLAPI mysql, int recoverBatchNum) {
	for (int i = lastBatchId; i > lastBatchId - recoverBatchNum; i--) {
	    ArrayList<Integer> eventIds = new ArrayList<Integer>();
	    int batchId = i;
	    QueryMySQL qMySQL = new QueryMySQL();
	    qMySQL.getEventIdsWithBatchId(batchId, eventIds, testFlag, mysql,
		    recoverBatchNum);
	    for (int j = 0; j < eventIds.size(); j++)
		qMySQL.restoreEventWithId(events, batchId, eventIds.get(j),
			seg, testFlag, mysql);
	}
    }
}
