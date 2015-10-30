package com.rongdai.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class UTFDecoder {

	public String UTFDecoder(String str) {
		if(!"".equals(str)||null != str){
			try {
				str = URLDecoder.decode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;
	}
}
