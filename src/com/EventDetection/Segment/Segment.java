package com.EventDetection.Segment;

import com.EventDetection.DataStructure.Document;
import ICTCLAS.I3S.AC.ICTCLAS50;

public class Segment {
    public ICTCLAS50 ins_ICTCLA = new ICTCLAS50();
    
    /**
     * Initiate a Chinese segmenter instance
     * @return 
     */
    public void start() {
	ins_ICTCLA.paragraphProcess_Start();
	String dictFile = "././dict.txt";
	byte[] usrdirb = dictFile.getBytes();
	ins_ICTCLA.ICTCLAS_ImportUserDictFile(usrdirb, 0);
    }
    
    /**
     * Segment the content of a given document
     * @param  doc, the document to be segmented
     * @return 
     */
    public boolean getSegs(Document doc) {
	String segmented = "";
	try {
	    segmented = ins_ICTCLA.paragraphProcessWithTag(doc.text.replaceAll(
		    " ", ""));
	    String[] temp = segmented.split(" ");
	    int removecount = 0;
	    for (int i = 0; i < temp.length; i++) {
		if (temp[i].contains("/w") || temp[i].contains("/x")) {
		    removecount++;
		    temp[i] = "";
		    continue;
		}
		int index = temp[i].lastIndexOf("/");
		if (index >= 0)
		    temp[i] = temp[i].substring(0, index);
	    }
	    doc.totalcount = temp.length - removecount;
	    doc.segText = temp;
	    return true;
	} catch (Exception ex) {
	    System.out.println(ex.getMessage());
	    return false;
	}
    }
    /**
     * Destroy a Chinese segmenter instance
     * @return 
     */
    public void end() {
	ins_ICTCLA.paragraphProcess_End();
    }
}
