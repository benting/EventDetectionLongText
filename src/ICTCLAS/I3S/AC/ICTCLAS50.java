package ICTCLAS.I3S.AC;

import java.io.*;

public class ICTCLAS50 {
    public enum eCodeType {
	CODE_TYPE_UNKNOWN, // type unknown
	CODE_TYPE_ASCII, // ASCII
	CODE_TYPE_GB, // GB2312,GBK,GB10380
	CODE_TYPE_UTF8, // UTF-8
	CODE_TYPE_BIG5// BIG5
    }

    public native boolean ICTCLAS_Init(byte[] sPath);

    public native boolean ICTCLAS_Exit();

    public native int ICTCLAS_ImportUserDictFile(byte[] sPath, int eCodeType);

    public native int ICTCLAS_SaveTheUsrDic();

    public native int ICTCLAS_SetPOSmap(int nPOSmap);

    public native boolean ICTCLAS_FileProcess(byte[] sSrcFilename,
	    int eCodeType, int bPOSTagged, byte[] sDestFilename);

    public native byte[] ICTCLAS_ParagraphProcess(byte[] sSrc, int eCodeType,
	    int bPOSTagged);

    public native byte[] nativeProcAPara(byte[] sSrc, int eCodeType,
	    int bPOStagged);

    static {
	System.loadLibrary("ICTCLAS50");
    }

    public void paragraphProcess_Start() {
	String argu = ".";
	try {
	    if (ICTCLAS_Init(argu.getBytes("UTF-8")) == false) {
		System.out.println("Init Fail!");
	    }
	    ICTCLAS_SetPOSmap(2);
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }

    public String paragraphProcess(String sInput) {
	String res = null;
	try {
	    byte nativeBytes[] = ICTCLAS_ParagraphProcess(
		    sInput.getBytes("UTF-8"), 0, 0);
	    res = new String(nativeBytes, 0, nativeBytes.length, "UTF-8");
	} catch (Exception ex) {
	}
	return res;
    }

    public String paragraphProcessWithTag(String sInput) {
	String res = null;
	try {
	    byte nativeBytes[] = ICTCLAS_ParagraphProcess(
		    sInput.getBytes("UTF-8"), 0, 1);
	    res = new String(nativeBytes, 0, nativeBytes.length, "UTF-8");
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return res;
    }

    public void paragraphProcess_End() {
	ICTCLAS_Exit();
    }

}
