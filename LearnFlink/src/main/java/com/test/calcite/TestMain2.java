package com.test.calcite;

import com.test.filesource.FileTableSource;
import com.test.util.connector.FileUtil;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.StreamQueryConfig;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;

public class TestMain2 {
	public static void main(String[] args) {
		StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
		StreamTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(sEnv);
		sEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		FileTableSource fileTableSource = FileUtil.getFileTableSource();
		tableEnv.registerTableSource("filesource", fileTableSource);

//		testMethod1(tableEnv);
//		testMethod2(tableEnv);
		testMethod3(tableEnv);

//		try {
//			sEnv.execute();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public static void testMethod1(StreamTableEnvironment tableEnv){
		StreamQueryConfig qConfig = new StreamQueryConfig();
		Table sqlResult = tableEnv.sqlQuery("SELECT AVG(user_count) as value1,TUMBLE_START(_sysTime, INTERVAL '1' MINUTE) as start_time FROM filesource WHERE user_name = '小张' GROUP BY TUMBLE(_sysTime, INTERVAL '1' MINUTE)");
		RowTypeInfo rowTypeInfo = new RowTypeInfo(Types.LONG,Types.SQL_TIMESTAMP);
		RelOptPlanner planner  = tableEnv.getPlanner();
		System.out.println(planner.toString());
	}

	public static void testMethod2(StreamTableEnvironment tableEnv){
		StreamQueryConfig qConfig = new StreamQueryConfig();
		Table sqlResult = tableEnv.sqlQuery("SELECT AVG(user_count) as value1,TUMBLE_START(_sysTime, INTERVAL '1' MINUTE) as start_time FROM filesource WHERE user_name = '小张' GROUP BY TUMBLE(_sysTime, INTERVAL '1' MINUTE)");
//		System.out.println(sqlResult.getRelNode().toString());
		System.out.println(RelOptUtil.toString(sqlResult.getRelNode()));
	}

	public static void testMethod3(StreamTableEnvironment tableEnv){
		StreamQueryConfig qConfig = new StreamQueryConfig();
		Table sqlResult = tableEnv.sqlQuery("SELECT AVG(user_count) as value1,TUMBLE_START_1(_sysTime, INTERVAL '1' MINUTE) as start_time FROM filesource WHERE user_name = '小张' " +
			"GROUP BY TUMBLE(_sysTime, INTERVAL '1' MINUTE)");
		System.out.println(RelOptUtil.toString(sqlResult.getRelNode()));
	}
}
