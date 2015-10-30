package com.rongdai.domain;


import java.util.List;



/**
 * 查询个人中心的赚和投,余额
 * @author Acc
 *
 */
public class investAndEarn {

	public String message;
	public String errorCode;
	
	public investAndEarnData data;
	public static class investAndEarnData {

		/**
		 * 余额
		 **/
		public String canUseBal;
		/**
		 * 赚
		 **/
		public String investmentCount;
		/**
		 * 投
		 **/
		public String earn;
		
		/**
		 * 待收本金
		 **/
		public String waitInvestmentCount;
		
		/**
		 * 待收收益
		 **/
		public String waitEarn;
		
	}
	
}
