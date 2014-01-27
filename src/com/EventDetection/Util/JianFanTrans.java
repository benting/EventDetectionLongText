package com.EventDetection.Util;

import com.EventDetection.Util.ZHConverter;
/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */
public class JianFanTrans {
    /**
     * Transform simple Chinese to traditional Chinese
     * @param  simpStr, simple Chinese string
     * @return traditional Chinse string
     */
    public static String simToTra(String simpStr) {
	ZHConverter converter = ZHConverter
		.getInstance(ZHConverter.TRADITIONAL);
	String traditionalStr = converter.convert(simpStr);
	return traditionalStr;
    }
    
    /**
     * Transform traditional Chinese to simple Chinese
     * @param  tradStr, traditional Chinese string
     * @return simple Chinse string
     */
    public static String traToSim(String tradStr) {
	ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
	String simplifiedStr = converter.convert(tradStr);
	return simplifiedStr;
    }
}