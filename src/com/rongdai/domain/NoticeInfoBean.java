package com.rongdai.domain;

import java.util.List;

/**
 * 咨询详情的java bean
 * 
 * @author Administrator
 * 
 */
public class NoticeInfoBean {
	public Data data;
	public String message;
	public String errorCode;

	@Override
	public String toString() {
		return "NoticeInfoBean [data=" + data + ", message=" + message
				+ ", errorCode=" + errorCode + "]";
	}

	public class Data {
		public String newsId;
		public String newsSendTime;
		public String newsImg;
		public String newsContent;
		public String counts;
		public String newsKeyWords;
		public String newsTitle;
		public String newsCome;
		public List<ReplyInfo> reply;

		@Override
		public String toString() {
			return "Data [newsId=" + newsId + ", newsSendTime=" + newsSendTime
					+ ", newsImg=" + newsImg + ", newsContent=" + newsContent
					+ ", counts=" + counts + ", newsKeyWords=" + newsKeyWords
					+ ", newsTitle=" + newsTitle + ", newsCome=" + newsCome
					+ ", reply=" + reply + "]";
		}

		public class ReplyInfo {
			public String imgUrl;
			public String replyId;
			public String replyDate;
			public String replyContent;
			public String replyImgUrl;
			public String oneName;
			public List<Replys> replys;
			
			
			
			@Override
			public String toString() {
				return "ReplyInfo [imgUrl=" + imgUrl + ", replyId=" + replyId
						+ ", replyDate=" + replyDate + ", replyContent="
						+ replyContent + ", replyImgUrl=" + replyImgUrl
						+ ", oneName=" + oneName + ", replys=" + replys + "]";
			}





			public class Replys{
				public String replyId;
				public String replyContent;
				public String oneName;
				public String twoName;
				@Override
				public String toString() {
					return "Replys [replyId=" + replyId + ", replyContent="
							+ replyContent + ", oneName=" + oneName
							+ ", twoName=" + twoName + "]";
				}
				
				
				
			}
		}
	}

}
