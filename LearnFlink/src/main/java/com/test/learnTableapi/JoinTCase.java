package com.test.learnTableapi;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.Arrays;
import java.util.List;

import static com.test.learnTableapi.SqlOverviewITCase.procTimePrint;

public class JoinTCase {
	/**
	 * 学习
	 * left join
	 * right join
	 * inner join
	 * full join flink不支持
	 * @param args
	 */
	public static void main(String[] args) {
//		testMethod1();
//		testMethod2();
//		testMethod3();
		testMethod5();
	}

	/**
	 * 内连接
	 * inner join
	 * inner join（内连接），在两张表进行连接查询时，只保留两张表中完全匹配的结果集。
	 */
	public static void testMethod1(){
	    String sql = "SELECT * FROM customer_tab INNER JOIN order_tab ON customer_tab.c_id = order_tab.c_id";
	    procTimePrint(sql);
	}

	/**
	 * 左连接
	 * left join
	 * left join,在两张表进行连接查询时，会返回左表所有的行，即使在右表中没有匹配的记录。
	 *
	 */
	public static void testMethod2(){
		String sql = "SELECT * FROM customer_tab left join order_tab ON customer_tab.c_id = order_tab.c_id";
		procTimePrint(sql);
	}

	/**
	 * 右连接
	 * right join
	 * right join,在两张表进行连接查询时，会返回右表所有的行，即使在左表中没有匹配的记录。
	 *
	 */
	public static void testMethod3(){
		String sql = "SELECT * FROM customer_tab right join order_tab ON customer_tab.c_id = order_tab.c_id";
		procTimePrint(sql);
	}

	/**
	 * 全连接
	 * full join
	 * 产生A和B的并集。对于没有匹配的记录，则会以null做为值
	 * 还没有支持
	 *
	 */
	public static void testMethod4(){
//		String sql = "SELECT * FROM customer_tab right join order_tab ON customer_tab.c_id = order_tab.c_id";
//		procTimePrint(sql);
	}
	/**
	 * 交叉连接
	 * CROSS JOIN
	 * 把表A和表B的数据进行一个N*M的组合，即笛卡尔积
	 * 还没有支持
	 *
	 */
	public static void testMethod5(){
		String sql = "SELECT * FROM customer_tab CROSS JOIN order_tab ON customer_tab.c_id = order_tab.c_id";
		procTimePrint(sql);
	}

	public static void testMethod6(){
		String sql = "SELECT * FROM customer_tab CROSS JOIN order_tab ON customer_tab.c_id = order_tab.c_id";
		procTimePrint(sql);
	}
}
