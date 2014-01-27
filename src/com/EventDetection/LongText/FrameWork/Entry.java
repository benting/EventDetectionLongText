package com.EventDetection.LongText.FrameWork;

import java.util.Date;
import java.util.HashMap;

import com.EventDetection.DataStructure.Event;
import com.EventDetection.MySQL.MySQLAPI;
import com.EventDetection.MySQL.EnvironmentVariables;
import com.EventDetection.Segment.Segment;
import com.EventDetection.TFIDF.TFIDF;
import com.EventDetection.Util.ParseTime;
/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */
public class Entry {
    public static double simThresh = 0.1;// document similarity threshold
    public static int timeInterval = 1 * 1 * 60 * 60 * 1000; 
    public static int updateInteval = 24;
    public static int recoverBatchNum = 3;
    public static String initdate = "2009-04-20 00:00:00";// date of first record in mysql
    public static boolean testFlag = false;// whether write data in test tables of mysql
    public static boolean initFlag = false; // initialize in batch
    public static long timeIndex = 0;// the time before updating mysql
    public static int lastBatchId = -1;
    public static long now = 0;
    public static int totalDocsCount = 0;
    public static String sqlName = "classification";
    
    public static void main(String[] args) {
	HashMap<String, String> argsMap = new HashMap<String, String>();
	String tfidfFileName = "DF.txt";
	if (args.length != 0 && !argsParsing(args, argsMap))
	    return;
	try {
	    MySQLAPI mysql = new MySQLAPI();
	    mysql.getConnection();
	    getEnvironmentVariables(mysql);
	    Segment seg = new Segment();
	    seg.start();
	    if (initFlag) {
		long startTime = ParseTime.string2Long(initdate);
		Initialize start = new Initialize(simThresh, startTime,
			timeInterval * updateInteval);
		start.run(testFlag, seg, mysql, sqlName);
		timeIndex = startTime + timeInterval * updateInteval;
	    }
	    Iterate iterate = new Iterate(simThresh, timeInterval
		    * updateInteval);
	    TFIDF tfidf = new TFIDF();
	    while (Entry.timeIndex < now) {
		iterate.run(testFlag, seg, mysql, sqlName, tfidf,
			recoverBatchNum);
		tfidf.storeIncDF(tfidfFileName);
	    }
	    mysql.closeAll();
	    seg.end();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /**
     * Parse input arguments when running the program
     * @param args, the list of input arguments
     * @param argsMap, structured organization of arguments
     * @return true if the input parameters are valid, otherwise false
     */
    private static boolean argsParsing(String[] args,
	    HashMap<String, String> argsMap) {
	if (args[0].equals("--help")) {
	    System.out
		    .println("\n-i initialization (default is 'false')"
			    + "\n-d use test tables in MySQL (default is 'false')"
			    + "\n-b input data base table (default is 'classification')"
			    + "\n-t time interval of update batch in hours (default is '2' hours)"
			    + "\n-l recovery size in time intervals (default is '36')"
			    + "\n-s similarity threshold of intervals (default is '0.1')"
			    + "\n-ft first timestamp of document in MySQL (default is '2009-04-20 00:00:00)'");
	    return false;
	}
	for (int i = 0; i < args.length; i = i + 2) 
	    argsMap.put(args[i], args[i + 1]);
	if (argsMap.containsKey("-i")) {
	    String paraValue = argsMap.get("-i");
	    if (paraValue.equals("true"))
		initFlag = true;
	    else if (paraValue.equals("false"))
		initFlag = false;
	    else
		System.out.println("error with para -i");
	}
	if (argsMap.containsKey("-d")) {
	    String paraValue = argsMap.get("-d");
	    if (paraValue.equals("true"))
		initFlag = true;
	    else if (paraValue.equals("false"))
		initFlag = false;
	    else
		System.out.println("error with para -d");
	}
	if (argsMap.containsKey("-b"))
	    sqlName = argsMap.get("-b");

	if (argsMap.containsKey("-t")) {
	    try {
		updateInteval = Integer.parseInt(argsMap.get("-t"));
	    } catch (NumberFormatException e) {
		System.out.println("error with para -t");
		e.printStackTrace();
	    }
	}
	if (argsMap.containsKey("-l")) {
	    try {
		recoverBatchNum = Integer.parseInt(argsMap.get("-l"));
	    } catch (NumberFormatException e) {
		System.out.println("error with para -l");
		e.printStackTrace();
	    }
	}
	if (argsMap.containsKey("-s")) {
	    try {
		simThresh = Double.parseDouble(argsMap.get("-s"));
	    } catch (NumberFormatException e) {
		System.out.println("error with para -s");
		e.printStackTrace();
	    }
	}
	return true;
    }
    /**
     * Get the needed variables from existing data, including most recent event ID, batch ID, time, total count of documents
     * @param mysql, instance of MySQL API
     * @return 
     */
    private static void getEnvironmentVariables(MySQLAPI mysql) {
	Date time = new Date();
	now = (long) time.getTime();
	EnvironmentVariables sr = new EnvironmentVariables();
	Event.ID = sr.getMostRecentEventId(testFlag, mysql);
	lastBatchId = sr.getMostRecentBatchId(testFlag, mysql);
	timeIndex = sr.getMostRecentTime(testFlag, mysql) + 1000;
	totalDocsCount = sr.getProcessedDocsNum(testFlag, mysql);
    }
}