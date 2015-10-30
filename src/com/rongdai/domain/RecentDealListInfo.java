package com.rongdai.domain;

import java.util.List;


/**
 * 投资项目
 * @author Acc
 *
 */
public class RecentDealListInfo {
	public String errorCode;

	public String message;

	public String geterrorCode() {
		return errorCode;
	}

	public void seterrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getmessage() {
		return message;
	}

	public void setmessage(String message) {
		this.message = message;
	}
	
	public List<RecentDealListData> data;
	
	public class RecentDealListData{
	
	
	
	/**
	 * 类型
	 */
	public String accountRecordType;
	/**
	 * 时间
	 */
	public String accountRecordDate;
	/**
	 * 金额
	 */
	public String accountRecordMoney;
	/**
	 * 手续费
	 */
	public String factorage;
	/**
	 * 还充手续费
	 */
	public String huanChongFactorage;
	



	
	}
}
