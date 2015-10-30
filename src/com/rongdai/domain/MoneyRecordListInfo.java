package com.rongdai.domain;

import java.util.List;


/**
 * 投资项目
 * @author Acc
 *
 */
public class MoneyRecordListInfo {
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
	
	public List<MoneyRecordListData> data;
	
	public class MoneyRecordListData{
	
	
	
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
	 * 记录名称
	 */
	public String accountRecordName;
	


	
	}
}
