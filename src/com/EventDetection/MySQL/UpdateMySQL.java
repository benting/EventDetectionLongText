package com.EventDetection.MySQL;

import java.util.ArrayList;

import com.EventDetection.DataStructure.Document;
import com.EventDetection.DataStructure.Event;
import com.EventDetection.Util.ParseTime;
/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */
public class UpdateMySQL {
    /**
     * Insert the information of documents of all current events into table "document_event_pair"
     * @param events, events to be stored in database
     * @param testFlag, whether to use a test table in database
     * @param mysql, instance of MySQL API
     * @return true if no exceptions occur
     */
    public boolean updateEventIds(ArrayList<Event> events, boolean testFlag,
	    MySQLAPI mysql) {
	try {
	    for (int i = 0; i < events.size(); i++)
		updateEventId(events.get(i), testFlag, mysql);
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }
    
    /**
     * Insert the information of documents of one event into database
     * @param event, the event to be stored in database
     * @param testFlag, whether to use a test table in database
     * @param mysql, instance of MySQL API
     * @return true if no exceptions occur
     */
    public boolean updateEventId(Event event, boolean testFlag, MySQLAPI mysql) {
	String mainTableName = "document_event_pair";
	if (testFlag)
	    mainTableName += "_copy";
	for (int j = 0; j < event.docs.size(); j++) {
	    Document document = event.docs.get(j);
	    if (document.oldDoc)
		continue;
	    String sql_doc = "insert into "
		    + mainTableName
		    + "(hbase_id, event_id, title, post_time, topic_Type)	values('"
		    + document.Id + "'," + event.id + ",'" + document.title
		    + "','" + document.formattime + "'," + document.topicType
		    + ")";
	    try {
		mysql.executeUpdate(sql_doc);
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	    }
	}
	return true;
    }
    
    /**
     * Update/Insert the information of events into table "event"
     * @param events, events to be stored in database
     * @param testFlag, whether to use a test table in database
     * @param mysql, instance of MySQL API
     * @return true if no exceptions occur
     */
    public boolean updateEvents(ArrayList<Event> events, boolean testFlag,
	    MySQLAPI mysql) {
	try {
	    for (int i = 0; i < events.size(); i++)
		updateEvent(events.get(i), testFlag, mysql);
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * Update/Insert the information of one event into table "event"
     * @param events, events to be stored in database
     * @param testFlag, whether to use a test table in database
     * @param mysql, instance of MySQL API
     * @return true if no exceptions occur
     */
    public boolean updateEvent(Event event, boolean testFlag, MySQLAPI mysql) {
	String eventTableName = "event";
	if (testFlag)
	    eventTableName += "_copy";
	int eventId = event.id;
	String sql_event = null;
	if (!event.newEvent)
	    sql_event = "update " + eventTableName + " set long_textNum = "
		    + event.docs.size() + ", batch_Id = " + event.batchID
		    + ", end_Time = \'" + ParseTime.long2String(event.endtime)
		    + "\', topic_Type = " + event.topicType
		    + " where event_id = " + eventId;
	else
	    sql_event = "insert into "
		    + eventTableName
		    + "(event_id, batch_Id, start_Time, end_Time, title, Hbase_id, long_textNum, topic_Type)	values("
		    + eventId + "," + event.batchID + ",'"
		    + ParseTime.long2String(event.starttime) + "','"
		    + ParseTime.long2String(event.endtime) + "','"
		    + event.docs.get(0).title + "','" + event.docs.get(0).Id
		    + "'," + event.docs.size() + "," + event.topicType + ")";
	try {
	    mysql.executeUpdate(sql_event);
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }
}
