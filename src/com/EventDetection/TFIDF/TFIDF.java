package com.EventDetection.TFIDF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.EventDetection.DataStructure.Document;
/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */
public class TFIDF {
    public Hashtable<String, Double> DF = new Hashtable<String, Double>();
    public Hashtable<String, Double> IDF = new Hashtable<String, Double>();

    public TFIDF() {
    }
    /**
     * Insert a document to existing events/clusters using single pass clustering algorithm
     * @param  inputPath, the input file path of DF records
     * @return 
     */
    public TFIDF(String inputPath) {
	try {
	    InputStreamReader isr = new InputStreamReader(new FileInputStream(
		    inputPath));
	    BufferedReader br = new BufferedReader(isr);
	    String record = new String();
	    while ((record = br.readLine()) != null) {
		String[] temp = record.split("\t");
		if (temp.length != 2)
		    continue;
		String key = temp[0];
		double value = Double.parseDouble(temp[1]);
		DF.put(key, value);
	    }
	    br.close();
	    isr.close();
	} catch (IOException e) {
	    System.out.println("Read file error!");
	    e.printStackTrace();
	}
    }
    /**
     * Update IDF value using incoming documents
     * @param  docs, the list of incoming documents
     * @param  totalDocCount, the total number of processed documents
     * @return 
     */
    public void updateValue(List<Document> docs, int totalDocCount) {
	for (int i = 0; i < docs.size(); i++) {
	    String[] segs = docs.get(i).segText;
	    Hashtable<String, Double> temp = new Hashtable<String, Double>();
	    for (int j = 0; j < segs.length; j++) {
		String seg = segs[j];
		if (seg.equals(""))
		    continue;
		if (!temp.containsKey(seg))
		    temp.put(seg, 1.0);
	    }
	    Enumeration<String> iter = temp.keys();
	    while (iter.hasMoreElements()) {
		String key = (String) iter.nextElement();
		if (DF.containsKey(key))
		    DF.put(key, DF.get(key) + 1.0);
		else
		    DF.put(key, 1.0);
	    }
	}
	Enumeration<String> iter = DF.keys();
	while (iter.hasMoreElements()) {
	    String key = (String) iter.nextElement();
	    double value = DF.get(key);
	    if (value == 0)
		continue;
	    double IDFValue = Math.log((totalDocCount + 0.5) / (value))
		    / Math.log(totalDocCount + 1);
	    IDF.put(key, IDFValue);
	}
    }
    /**
     * Store DF values in a file
     * @param  outputPath, the output file path
     * @return 
     */
    public void storeIncDF(String outputPath) {
	BufferedWriter output;
	try {
	    output = new BufferedWriter(new FileWriter(outputPath));
	    Enumeration<String> iter = DF.keys();
	    while (iter.hasMoreElements()) {
		String key = (String) iter.nextElement();
		double value = DF.get(key);
		output.write(key + "\t" + value + "\n");
	    }
	    output.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
