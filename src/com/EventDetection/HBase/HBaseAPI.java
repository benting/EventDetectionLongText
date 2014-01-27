package com.EventDetection.HBase;

import java.io.IOException;
import java.util.Map;
import com.xjtudlc.jchbase.JCHBase;

/**
 * @author Junting Ye <yejuntingben@gmail.com>
 */
public class HBaseAPI {
    JCHBase hbase;
    
    public boolean initDataBase() {
	String[] hbaseZKQuorum = { "202.117.16.164", "202.117.16.166",
		"202.117.16.167", "202.117.16.168", "202.117.16.169" };
	hbase = new JCHBase("202.117.16.162", "60000", hbaseZKQuorum);
	try {
	    hbase.init("yq_crawler", "f");
	} catch (IOException e) {
	    System.err.println("hbase init error " + e.getStackTrace());
	    return false;
	}
	return true;
    }

    public boolean closeHbase() {
	try {
	    hbase.close();
	    return true;
	} catch (IOException e) {
	    System.err.println("Hbase: " + e.getStackTrace());
	    System.exit(-1);
	    return false;
	}
    }

    public Map<String, String> readDataFromHbase(String key) {
	Map<String, String> result = null;
	try {
	    result = hbase.get(key);
	    return result;
	} catch (IOException e) {
	    System.err.println("key: " + key + "error: " + e.getStackTrace());
	    return null;
	}
    }
}
