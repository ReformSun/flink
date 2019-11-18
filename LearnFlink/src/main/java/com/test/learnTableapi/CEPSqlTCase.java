package com.test.learnTableapi;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * PARTITION BY  分区的列 可选
 * ORDER BY 可指定多列，但是必须以EVENT TIME列或者PROCESS TIME列作为排序的首列，可选项。
 * MEASURES 定义如何根据匹配成功的输入事件构造输出事件。
 * ONE ROW PER MATCH 对于每一次成功的匹配，只会产生一个输出事件。
 * ONE ROW PER MATCH WITH TIMEOUT ROWS 除了匹配成功的时候产生输出外，超时的时候也会产生输出。超时时间由PATTERN语句中的WITHIN语句定义。
 * ALL ROWS PER MATCH 对于每一次成功的匹配，对应于每一个输入事件，都会产生一个输出事件。
 * ALL ROWS PER MATCH WITH TIMEOUT ROWS
 *
 * https://help.aliyun.com/document_detail/73845.html?spm=a2c4g.11186623.6.652.39e03315wHKZry
 */
public class CEPSqlTCase {
	public static List<Row> table1 = Arrays.asList(
		Row.of( "ACME",1510365660000L,30,1)
		, Row.of( "ACME", 1510365720000L,31,2)
		, Row.of( "ACME", 1510365780000L,3,1)
		, Row.of( "ACME", 1510365840000L,34,3)
		, Row.of( "ACME", 1510365900000L,20,2)
		, Row.of( "ACME", 1510365960000L,50,1)
		, Row.of( "ACME", 1510366020000L,10,1)
		, Row.of( "ACME", 1510366080000L,92,2)
		, Row.of( "ACME", 1510366140000L,35,2)
		, Row.of( "ACME", 1510366200000L,7,2)
		, Row.of( "ACME", 1510365660000L,30,1));


	public static void main(String[] args) {
//		testMethod1();
		testMethod2();
	}

	/**
	 *
	 */
	public static void testMethod1(){
		String sql = "SELECT *\n" +
			"FROM Ticker\n" +
			"    MATCH_RECOGNIZE (\n" +
			"        PARTITION BY symbol\n" +
			"        ORDER BY rowtime\n" +
			"        MEASURES\n" +
			"            START_ROW.rowtime AS start_tstamp,\n" +
			"            LAST(PRICE_DOWN.rowtime) AS bottom_tstamp,\n" +
			"            LAST(PRICE_UP.rowtime) AS end_tstamp\n" +
			"        ONE ROW PER MATCH\n" +
			"        AFTER MATCH SKIP TO LAST PRICE_UP\n" +
			"        PATTERN (START_ROW PRICE_DOWN+ PRICE_UP)\n" +
			"        DEFINE\n" +
			"            PRICE_DOWN AS\n" +
			"                (LAST(PRICE_DOWN.price, 1) IS NULL AND PRICE_DOWN.price < START_ROW.price) OR\n" +
			"                    PRICE_DOWN.price < LAST(PRICE_DOWN.price, 1),\n" +
			"            PRICE_UP AS\n" +
			"                PRICE_UP.price > LAST(PRICE_DOWN.price, 1)\n" +
			"    ) MR";
		rowTimePrint(sql);
	}
	public static void testMethod2(){
	    String sql = "SELECT *\n" +
			"FROM Ticker\n" +
			"    MATCH_RECOGNIZE (\n" +
			"        PARTITION BY symbol\n" +   // 按照symbol进行分区 也就是把相同的symbol值放到同一个计算节点上
			"        ORDER BY rowtime\n" +      // 在窗口内对rowtime时间进行排序
			"        MEASURES\n" +              // 定义如何根据匹配成功的输入事件构造输出事件。
			"            FIRST(A.rowtime) AS start_tstamp,\n" + // 第一次的事件时间为开始时间
			"            LAST(A.rowtime) AS end_tstamp,\n" + // 最新的事件时间为结束时间
			"            AVG(A.price) AS avgPrice\n" +       // 把价格的平均值设为avgPrice
			"        ONE ROW PER MATCH\n" +                  // 匹配成功输出一条
			"        AFTER MATCH SKIP TO FIRST B\n" +
			"        PATTERN (A+ B)\n" +
			"        DEFINE\n" +
			"            A AS AVG(A.price) < 15\n" +
			"    ) MR";
	    rowTimePrint(sql);
	}

	public static void rowTimePrint(String sql){
		StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
		StreamTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(sEnv);
		sEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		DataStream<Row> dataStreamSource_1 = sEnv.fromCollection(table1).assignTimestampsAndWatermarks(new AssignerEventTimeFunction());

		tableEnv.registerDataStream("Ticker",dataStreamSource_1,"symbol,rowtime.rowtime,price,tax");

		Table table = tableEnv.sqlQuery(sql);
		DataStream<Tuple2<Boolean,Row>> dataStream = tableEnv.toRetractStream(table,Row.class);
		dataStream.addSink(new SqlOverviewITCase.RetractingSink());
		try {
			sEnv.execute("test");
		} catch (Exception e) {
			e.printStackTrace();
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
			long timestamp = (Long) element.getField(1);
			currentMaxTimestamp = Math.max(currentMaxTimestamp,timestamp);
			return timestamp;
		}
	}
}
