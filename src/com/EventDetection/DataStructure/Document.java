package com.EventDetection.DataStructure;

/**
 * @author Junting Ye <yejuntingben@gmail.com>
 */
public class Document {
    public String Id;// Hbase ID
    public String title;// title
    public String text;// news or blog or tweets
    public String[] segText;// text after tokenized
    public long unixtime;// unix time
    public String formattime;// formatted time
    public int totalcount;// words count in the document
    public int topicType = -1;
    public boolean oldDoc = false;
}
