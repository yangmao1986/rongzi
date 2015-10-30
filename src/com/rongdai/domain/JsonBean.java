package com.rongdai.domain;

import java.util.List;



public class JsonBean {

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
	
	public List<loanProjectListData> data;
	
	public class loanProjectListData{
	
	
	
	/**
	 * 融资项目名称
	 */
	public String borrowName;
	/**
	 * 投资时间
	 */
	public String investmentDate;
	/**
	 * 年化率
	 */
	public String borrowYearRate;
	/**
	 * 借款期限
	 */
	public String borrowLimit;
	/**
	 * 融资状态
	 */
	public String borrowStatus;
	/**
	 * 投资金额
	 */
	public String investmentAccount;
	/**
	 * 已回款
	 */
	public String hadrRturnedMoney;
	/**
	 * 待回款
	 */
	public String waitingRturnedMoney;



	public String getborrowName() {
		return borrowName;
	}

	public void setborrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getinvestmentDate() {
		return investmentDate;
	}

	public void setinvestmentDate(String investmentDate) {
		this.investmentDate = investmentDate;
	}

	public String getborrowYearRate() {
		return borrowYearRate;
	}

	public void setborrowYearRate(String borrowYearRate) {
		this.borrowYearRate = borrowYearRate;
	}

	public String getborrowLimit() {
		return borrowLimit;
	}

	public void setborrowLimit(String borrowLimit) {
		this.borrowLimit = borrowLimit;
	}
	
	public String getborrowStatus() {
		return borrowStatus;
	}

	public void setborrowStatus(String borrowStatus) {
		this.borrowStatus = borrowStatus;
	}
	public String getinvestmentAccount() {
		return investmentAccount;
	}

	public void setinvestmentAccount(String investmentAccount) {
		this.investmentAccount = investmentAccount;
	}
	public String gethadrRturnedMoney() {
		return hadrRturnedMoney;
	}

	public void sethadrRturnedMoney(String hadrRturnedMoney) {
		this.hadrRturnedMoney = hadrRturnedMoney;
	}
	public String getwaitingRturnedMoney() {
		return waitingRturnedMoney;
	}

	public void setwaitingRturnedMoney(String waitingRturnedMoney) {
		this.waitingRturnedMoney = waitingRturnedMoney;
	}
	
	}
}
