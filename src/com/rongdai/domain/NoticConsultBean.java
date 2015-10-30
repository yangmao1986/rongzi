package com.rongdai.domain;

import java.util.List;

/**
 * 公告咨询
 * 
 * @author Administrator
 * 
 */
public class NoticConsultBean {
	public List<NoticConsultInfo> data;
	public String message;
	public String errorCode;

	public class NoticConsultInfo {
		public String newsId;
		public String newsImg;
		public String newsKeyWords;
		public String newsTitle;
	}
}
