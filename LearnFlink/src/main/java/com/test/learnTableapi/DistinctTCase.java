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

public class DistinctTCase {
	public static List<Row> table1 = Arrays.asList(
		Row.of( "IBM",3335)
		, Row.of( "理想", 23333)
		, Row.of( "阿里", 3112)
		, Row.of( "华为", 4334)
		, Row.of( "华为", 43334));


	public static void main(String[] args) {
		testMethod1();
	}

	/**
	 * 取出返回中的重复值
	 */
	public static void testMethod1(){
		String sql = "SELECT DISTINCT Company FROM table1";
		procTimePrint(sql);
	}

	public static void procTimePrint(String sql){
		StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
		StreamTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(sEnv);
		sEnv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

		DataStreamSource<Row> dataStreamSource_1 = sEnv.fromCollection(table1);

		tableEnv.registerDataStream("table1",dataStreamSource_1,"Company,OrderNumber");

		Table table = tableEnv.sqlQuery(sql);
		DataStream<Tuple2<Boolean,Row>> dataStream = tableEnv.toRetractStream(table,Row.class);
		dataStream.addSink(new SqlOverviewITCase.RetractingSink());
		try {
			sEnv.execute("test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
