package com.test.learnTableapi;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlOverviewITCase {
	// 客户表数据
	public static List<Row> customer_data = Arrays.asList(
		Row.of("c_001", "Kevin", "from JinLin")
		, Row.of("c_002", "Sunny", "from JinLin")
		, Row.of("c_003", "JinCheng", "from HeBei"));
	public static List<Row> order_data = Arrays.asList(
		Row.of("o_001", "c_002", "2018-11-05 10:01:01", "iphone")
		, Row.of("o_002", "c_001", "2018-11-05 10:01:55", "ipad")
		, Row.of("o_003", "c_001", "2018-11-05 10:03:44", "flink book"));
	// 商品销售表数据
	public static List<Row> item_data = Arrays.asList(
		Row.of(1510365660000L, 20, "ITEM001", "Electronic")
		, Row.of(1510365720000L, 50, "ITEM002", "Electronic")
		, Row.of(1510365780000L, 30, "ITEM003", "Electronic")
		, Row.of(1510365780000L, 60, "ITEM004", "Electronic")
		, Row.of(1510365900000L, 10, "ITEM005", "Electronic")
		, Row.of(1510365960000L, 70, "ITEM006", "Electronic")
		, Row.of(1510366020000L, 80, "ITEM007", "Electronic")
		, Row.of(1510366080000L, 50, "ITEM008", "Electronic"));
	// 页面访问表数据
	public static List<Row> pageAccess_data = Arrays.asList(
		Row.of(1510365660000L, "ShangHai", "U0010")
		, Row.of(1510365660000L, "BeiJing", "U1001")
		, Row.of(1510366200000L, "BeiJing", "U2032")
		, Row.of(1510366260000L, "BeiJing", "U1100")
		, Row.of(1510373400000L, "ShangHai", "U0011"));
	// 页面访问量表数据2
	public static List<Row> pageAccessCount_data = Arrays.asList(
		Row.of(1510365660000L, "ShangHai", 100)
		, Row.of(1510365660000L, "BeiJing", 86)
		, Row.of(1510365960000L, "BeiJing", 210)
		, Row.of(1510366200000L, "BeiJing", 33)
		, Row.of(1510373400000L, "ShangHai", 129));
	// 页面访问表数据3
	public static List<Row> pageAccessSession_data = Arrays.asList(
		Row.of(1510365660000L, "ShangHai", "U0011")
		, Row.of(1510365720000L, "ShangHai", "U0012")
		, Row.of(1510365720000L, "ShangHai", "U0013")
		, Row.of(1510365900000L, "ShangHai", "U0015")
		, Row.of(1510366200000L, "ShangHai", "U0011")
		, Row.of(1510366200000L, "BeiJing", "U2010")
		, Row.of(1510366260000L, "ShangHai", "U0011")
		, Row.of(1510373760000L, "ShangHai", "U0410"));

	public static void main(String[] args) {
		String sql = "select * from order_tab";
//		procTimePrint(sql);
		sql = "SELECT * FROM customer_tab INNER JOIN order_tab ON customer_tab.c_id = order_tab.c_id";
		procTimePrint(sql);

	}

	public static void procTimePrint(String sql){
		StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
		StreamTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(sEnv);
		sEnv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
		// 将order_tab, customer_tab 注册到catalog
		DataStreamSource<Row> dataStreamSource_c = sEnv.fromCollection(customer_data);
		DataStreamSource<Row> dataStreamSource_o = sEnv.fromCollection(order_data);

		tableEnv.registerDataStream("customer_tab",dataStreamSource_c,"c_id,c_name,c_desc");
		tableEnv.registerDataStream("order_tab",dataStreamSource_o,"o_id,c_id,o_time,o_desc");
		Table table = tableEnv.sqlQuery(sql);
		DataStream<Tuple2<Boolean,Row>> dataStream = tableEnv.toRetractStream(table,Row.class);
		dataStream.addSink(new RetractingSink());
		try {
			sEnv.execute("test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void rowTimePrint(String sql){
		StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
		StreamTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(sEnv);
		sEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		// 将order_tab, customer_tab 注册到catalog
		DataStream<Row> item = sEnv.fromCollection(item_data).assignTimestampsAndWatermarks(new AssignerEventTimeFunction());
		DataStream<Row> pageAccess = sEnv.fromCollection(pageAccess_data).assignTimestampsAndWatermarks(new AssignerEventTimeFunction());
		DataStream<Row> pageAccessCount = sEnv.fromCollection(pageAccessCount_data).assignTimestampsAndWatermarks(new AssignerEventTimeFunction());
		DataStream<Row> pageAccessSession = sEnv.fromCollection(pageAccessSession_data).assignTimestampsAndWatermarks(new AssignerEventTimeFunction());

		tableEnv.registerDataStream("item_tab",item,"onSellTime.rowtime,price,itemID,itemType");
		tableEnv.registerDataStream("pageAccess_tab",pageAccess,"accessTime.rowtime,region,userId");
		tableEnv.registerDataStream("pageAccessCount_tab",pageAccessCount,"accessTime.rowtime,region,accessCount");
		tableEnv.registerDataStream("pageAccessSession_tab",pageAccessSession,"accessTime.rowtime,region,userId");
		Table table = tableEnv.sqlQuery(sql);
		DataStream<Tuple2<Boolean,Row>> dataStream = tableEnv.toRetractStream(table,Row.class);
		dataStream.addSink(new RetractingSink());
		try {
			sEnv.execute("test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	// 自定义sink
	static class RetractingSink extends RichSinkFunction<Tuple2<Boolean, Row>> {
		List<String> retractedResults = new ArrayList<>();
		@Override
		public void invoke(Tuple2<Boolean, Row> value, Context context) throws Exception {
			synchronized (retractedResults) {
				if (value.f0){
					retractedResults.add(value.f1.toString());
				}else {
					int index = retractedResults.indexOf(value.f1.toString());
					if (index >= 0){
						retractedResults.remove(value.f1.toString());
					} else {
						throw new RuntimeException("Tried to retract a value that wasn't added first. " +
							"This is probably an incorrectly implemented test. " +
							"Try to set the parallelism of the sink to 1.");
					}
				}
			}
//			System.out.println("ddddd: " + value.toString());
		}

		@Override
		public void close() throws Exception {
			for (String s : retractedResults){
				System.out.println(s);
			}
			super.close();
		}
	}
	// 自定义Water mark 生成器
	static class AssignerEventTimeFunction implements AssignerWithPunctuatedWatermarks<Row> {
		private long maxOutOfOrderness = 0L;
		private long currentMaxTimestamp;
		@Nullable
		@Override
		public Watermark checkAndGetNextWatermark(Row lastElement, long extractedTimestamp) {
			return new Watermark(currentMaxTimestamp - maxOutOfOrderness);
		}

		@Override
		public long extractTimestamp(Row element, long previousElementTimestamp) {
			long timestamp = (Long) element.getField(0);
			currentMaxTimestamp = Math.max(currentMaxTimestamp,timestamp);
			return timestamp;
		}
	}


}
