package com.EventDetection.Clustering;

import java.util.ArrayList;
import java.util.Hashtable;
import com.EventDetection.DataStructure.Event;
import com.EventDetection.TFIDF.Similarity;
import com.EventDetection.DataStructure.Document;

/**
 * @author Junting Ye <yejuntingben@gmail.com>
 */
public class SinglePassClustering {
    /**
     * Insert a document to existing events/clusters using single pass clustering algorithm
     * @param  document, the document to be clustered
     * @param  events, existing events
     * @param  IDF, table of inverse document frequency
     * @param  simThresh, document similarity threshold
     * @param  lastBatchId, the batch ID of the most recent batch
     * @return 
     */
    public void insertToClusters(Document document, ArrayList<Event> events,
	    Hashtable<String, Double> IDF, double simThresh, int lastBatchId) {
	Similarity sim = new Similarity();
	double maxSim = 0;
	int maxIndex = 0;
	for (int i = 0; i < events.size(); i++) {
	    ArrayList<Document> cluster = events.get(i).docs;
	    double totalSim = 0;
	    if (events.get(i).batchID < lastBatchId)
		continue;
	    for (int j = 0; j < cluster.size(); j++) {
		double sime = sim.calculate(document, cluster.get(j), IDF);
		totalSim += sime;
	    }
	    totalSim = totalSim / cluster.size();
	    if (totalSim > maxSim) {
		maxIndex = i;
		maxSim = totalSim;
	    }
	}
	if (maxSim > simThresh) {
	    events.get(maxIndex).batchID = lastBatchId + 1;
	    events.get(maxIndex).docs.add(document);
	    events.get(maxIndex).endtime = document.unixtime;
	} else {
	    Event newEvent = new Event();
	    newEvent.docs.add(document);
	    newEvent.starttime = document.unixtime;
	    newEvent.endtime = document.unixtime;
	    newEvent.id = Event.ID++;
	    newEvent.batchID = lastBatchId + 1;
	    newEvent.newEvent = true;
	    events.add(newEvent);
	}
    }
}