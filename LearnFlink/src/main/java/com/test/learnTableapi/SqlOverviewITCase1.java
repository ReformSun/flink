package com.test.learnTableapi;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.Arrays;
import java.util.List;

public class SqlOverviewITCase1 {
	public static List<Row> table1 = Arrays.asList(
		Row.of(1510365660000L, 20, 1, "Electronic")
		, Row.of(1510365720000L, 50, 2, "Electronic")
		, Row.of(1510365780000L, 30, 3, "Electronic")
		, Row.of(1510365780000L, 60, 4, "Electronic")
		, Row.of(1510365900000L, 10, 5, "Electronic")
		, Row.of(1510365960000L, 70, 6, "Electronic")
		, Row.of(1510366020000L, 80, 7, "Electronic")
		, Row.of(1510366080000L, 50, 8, "Electronic"));
	public static List<Row> table2 = Arrays.asList(
		Row.of(1510365660000L, "ShangHai", "U0010",1)
		, Row.of(1510365660000L, "BeiJing", "U1001",2)
		, Row.of(1510366200000L, "BeiJing", "U2032",3)
		, Row.of(1510366260000L, "BeiJing", "U1100",4)
		, Row.of(1510373400000L, "ShangHai", "U0011",5));


	public static void rowTimePrint(String sql){
		StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
		StreamTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(sEnv);
		sEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		// 将order_tab, customer_tab 注册到catalog
		DataStream<Row> item = sEnv.fromCollection(table1).assignTimestampsAndWatermarks(new SqlOverviewITCase.AssignerEventTimeFunction());
		DataStream<Row> pageAccess = sEnv.fromCollection(table2).assignTimestampsAndWatermarks(new SqlOverviewITCase.AssignerEventTimeFunction());

		tableEnv.registerDataStream("item_tab",item,"ltime.rowtime,price,item_id,itemType");
		tableEnv.registerDataStream("pageAccess_tab",pageAccess,"rtime.rowtime,region,userId,item_id");
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
