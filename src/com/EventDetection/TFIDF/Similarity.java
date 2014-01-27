package com.EventDetection.TFIDF;

import java.util.ArrayList;
import java.util.Hashtable;

import com.EventDetection.DataStructure.Document;
import com.EventDetection.DataStructure.Token;
/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */
public class Similarity { 
    /**
     * Calculate the cosine similarity of two term vectors
     * @param  document1, one document
     * @param  document2, the other document
     * @param  IDF, hash table of the inverse document frequency
     * @return the similarity value of two documents
     */
    public double calculate(Document document1, Document document2,
	    Hashtable<String, Double> IDF) {
	double sim = 0;
	ArrayList<Token> tokens = new ArrayList<Token>();
	String[] segs1 = document1.segText;
	String[] segs2 = document2.segText;

	for (int i = 0; i < segs1.length; i++) {
	    if (segs1[i].equals(""))
		continue;
	    if (!containAndAdd(tokens, segs1[i], 1)) {
		Token token = new Token();
		token.text = segs1[i];
		token.str1Count++;
		tokens.add(token);
	    }
	}
	for (int i = 0; i < segs2.length; i++) {
	    if (segs2[i].equals(""))
		continue;
	    if (!containAndAdd(tokens, segs2[i], 2)) {
		Token token = new Token();
		token.text = segs2[i];
		token.str2Count++;
		tokens.add(token);
	    }
	}
	getTFIDF(tokens, IDF, document1.totalcount, document2.totalcount);
	double numerator = 0;
	double denominator1 = 0;
	double denominator2 = 0;
	for (int i = 0; i < tokens.size(); i++) {
	    Token s = tokens.get(i);
	    numerator += s.tfidf1 * s.tfidf2;
	    denominator1 += s.tfidf1 * s.tfidf1;
	    denominator2 += s.tfidf2 * s.tfidf2;
	}
	sim = numerator / (Math.sqrt(denominator1) * Math.sqrt(denominator2));
	if (sim > 0)
	    return sim;
	else
	    return 0;
    }
    
    /**
     * Calculate the TF-IDF weight value of tokens
     * @param  tokens, the list of tokens
     * @param  IDF, hash table of the inverse document frequency
     * @param  totalcount1, the total count of terms in document 1
     * @param  totalcount2, the total count of terms in document 2
     * @return 
     */
    public void getTFIDF(ArrayList<Token> tokens,
	    Hashtable<String, Double> IDF, int totalcount1, int totalcount2) {
	for (int i = 0; i < tokens.size(); i++) {
	    try {
		Token t = tokens.get(i);
		t.idf = IDF.get(t.text);
		t.tfidf1 = t.str1Count * 1.0 / totalcount1 * t.idf;
		t.tfidf2 = t.str2Count * 1.0 / totalcount2 * t.idf;
	    } catch (Exception e) {
		System.out.println(e.getStackTrace());
	    }
	}
    }
    /**
     * If the list of tokens contain a given string, then increase the count; if not, insert the token to the list
     * @param  tokens, the list of tokens
     * @param  string, the give string
     * @param  strNum, the string ID, the first string or the second string
     * @return 
     */
    private boolean containAndAdd(ArrayList<Token> tokens, String string,
	    int strNum) {
	for (int i = 0; i < tokens.size(); i++) {
	    if (tokens.get(i).text.equals(string)) {
		if (strNum == 1)
		    tokens.get(i).str1Count++;
		else
		    tokens.get(i).str2Count++;
		return true;
	    }
	}
	return false;
    }
}