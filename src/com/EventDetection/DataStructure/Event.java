package com.EventDetection.DataStructure;

import java.util.ArrayList;

/**
 * @author Junting Ye <yejuntingben@gmail.com>
 */
public class Event {
    public static int ID;
    public int id;
    public int batchID;
    public long starttime = Long.MAX_VALUE;
    public long endtime = Long.MIN_VALUE;
    public boolean newEvent = false;
    public ArrayList<Document> docs = new ArrayList<Document>();
    public int topicType = -1;
}
