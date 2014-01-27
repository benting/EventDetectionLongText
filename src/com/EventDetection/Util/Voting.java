package com.EventDetection.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.EventDetection.DataStructure.Document;
import com.EventDetection.DataStructure.Event;
/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */
public class Voting {
    /**
     * Vote for the label of the event, using the labels of documents
     * @param  events, the list of events to be voted
     * @return 
     */
    public void Vote4TopicType(ArrayList<Event> events) {
	for (Event event : events) {
	    Map<Integer, Integer> hm = new HashMap<Integer, Integer>();
	    for (Document doc : event.docs) {
		if (hm.containsKey(doc.topicType))
		    hm.put(doc.topicType, hm.get(doc.topicType) + 1);
		else
		    hm.put(doc.topicType, 1);
	    }
	    Iterator iter = hm.entrySet().iterator();
	    int max = 0;
	    int maxType = -1;
	    while (iter.hasNext()) {
		Map.Entry entry = (Map.Entry) iter.next();
		Object key = entry.getKey();
		Object val = entry.getValue();
		if ((Integer) val > max) {
		    maxType = (Integer) key;
		    max = (Integer) val;
		}
	    }
	    event.topicType = maxType;
	}
    }
}
