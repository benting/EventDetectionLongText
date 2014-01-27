package com.EventDetection.HBase;

import java.util.List;
import java.util.Map;

import com.EventDetection.DataStructure.Document;
import com.EventDetection.Util.JianFanTrans;

/**
 * @author Junting Ye <yejuntingben@gmail.com>
 */
public class QueryContent extends HBaseAPI {/**
     * Return the text content in HBase using text Id
     * @param  docs, the list of documents
     * @return 
     */
    public void getLongText(List<Document> docs) {
	this.initDataBase();
	for (int i = 0; i < docs.size(); i++) {
	    String key = docs.get(i).Id;
	    Map<String, String> value = this.readDataFromHbase(key);
	    String text = value.get("content");
	    docs.get(i).text = JianFanTrans.traToSim(text);
	    if (docs.get(i).text == null || docs.get(i).text.length() < 200)// 除去数据源中的短文本
	    {
		docs.remove(i);
		i--;
	    }
	}
	this.closeHbase();
    }
}
