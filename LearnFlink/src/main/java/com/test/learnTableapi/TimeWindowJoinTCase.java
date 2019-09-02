package com.test.learnTableapi;

import static com.test.learnTableapi.SqlOverviewITCase1.rowTimePrint;

public class TimeWindowJoinTCase {
	public static void main(String[] args) {
//		testMethod1();
//		testMethod2();
//		testMethod3();
		testMethod4();
	}

	/**
	 * 查看表中商品销售时，页面访问表的数据情况
	 */
	public static void testMethod1(){
		String sql = "SELECT * FROM item_tab o, pageAccess_tab s WHERE o.onSellTime = s.accessTime";
		rowTimePrint(sql);
	}

	/**
	 *
	 */
	public static void testMethod2(){
		String sql = "SELECT o.price FROM item_tab o, pageAccess_tab s WHERE o.item_id = s.item_id AND o.ltime >= s.rtime AND o.ltime < s.rtime + INTERVAL '2' MINUTE";
		rowTimePrint(sql);
	}

	/**
	 * 这中表达的语义是非常模糊的
	 * 是没有办法查询出来的
	 */
	public static void testMethod3(){
		String sql = "SELECT o.price FROM item_tab o, pageAccess_tab s WHERE o.ltime >= s.rtime AND o.ltime < s.rtime + INTERVAL '2' MINUTE";
		rowTimePrint(sql);
	}

	/**
	 * 查看表中商品销售时，页面访问表的数据情况
	 */
	public static void testMethod4(){
		String sql = "SELECT o.price FROM item_tab o, pageAccess_tab s WHERE o.item_id = s.item_id AND o.ltime BETWEEN s.rtime - INTERVAL '10' SECOND AND s.rtime + INTERVAL '1' MINUTE";
		rowTimePrint(sql);
	}

}
