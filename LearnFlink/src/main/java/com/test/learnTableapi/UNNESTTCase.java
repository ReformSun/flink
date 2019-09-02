package com.test.learnTableapi;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.FunctionParameter;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;


/**
 * flink 还不支持这种语句
 */
public class UNNESTTCase {
	public static List<Row> table1 = Arrays.asList(
		Row.of( 20,3,new String[]{"aa","c"})
		, Row.of( 50, 2, new String[]{"ab","d"})
		, Row.of( 30, 3, new String[]{"ah","g"})
		, Row.of( 60, 4, new String[]{"ae","ck"}));
	public static List<Row> table2 = Arrays.asList(
		Row.of( 21,3,new String[]{"appp","c"})
		, Row.of( 52, 2, new String[]{"ab","d"})
		, Row.of( 34, 3, new String[]{"aj","g"})
		, Row.of( 66, 4, new String[]{"ay","ckff"}));
	public static void main(String[] args) {
//		testMethod1();
//		testMethod2();
//		testMethod3();
		testMethod4();
	}

	public static void testMethod1(){
	    String sql = "SELECT a,b,f FROM table1 CROSS JOIN UNNEST(table1.c) AS t (f)";
//		String sql = "SELECT e,f,a FROM table2 CROSS JOIN UNNEST(table2.g) AS t (a)";
		procTimePrint(sql);
	}

	/**
	 * 还不能跨表
	 */
	public static void testMethod2(){
		String sql = "SELECT a,b,h FROM table1 CROSS JOIN UNNEST(table2.g) AS t (h)";
		procTimePrint(sql);
	}

	/**
	 * 不同表之间的LATERAL TABLE 有bug
	 */
	public static void testMethod3(){
//		String sql = "SELECT a,b,h FROM table1 , LATERAL TABLE(unnest_udtf(table1.c)) t AS h";
//		String sql = "SELECT a,b,h FROM table1 , LATERAL TABLE(unnest_udtf(table1.c)) AS t (h)";
		String sql = "SELECT a,b,h FROM table1 , LATERAL TABLE(unnest_udtf(table2.g)) AS t (h) WHERE table1.b = table2.f";
		procTimePrint(sql);
	}

	public static void testMethod4(){
//		String sql = "SELECT a,b,h FROM table1 , LATERAL TABLE(unnest_udtf(table1.c)) AS t (h)";
		String sql = "SELECT a,b,h FROM table1 LEFT JOIN LATERAL TABLE(unnest_udtf(table1.g)) AS t (h) ON TRUE";
		procTimePrint(sql);
	}

	public static void testMethod8(){
		String sql = "SELECT * FROM table1 LEFT JOIN table2 ON table1.b = table2.f";
		procTimePrint(sql);
	}






	public static void procTimePrint(String sql){
		StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
		StreamTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(sEnv);
		sEnv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

		tableEnv.registerFunction("unnest_udtf",new CustomTableFunction());

		DataStreamSource<Row> dataStreamSource_1 = sEnv.fromCollection(table1);
		DataStreamSource<Row> dataStreamSource_2 = sEnv.fromCollection(table2);

		tableEnv.registerDataStream("table2",dataStreamSource_2,"e,f,g");
		tableEnv.registerDataStream("table1",dataStreamSource_1,"a,b,c");


		Table table = tableEnv.sqlQuery(sql);
		DataStream<Tuple2<Boolean,Row>> dataStream = tableEnv.toRetractStream(table,Row.class);
		dataStream.addSink(new SqlOverviewITCase.RetractingSink());
		try {
			sEnv.execute("test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static class CustomTableFunction extends TableFunction<Row> {
		public void eval(String[] strings){
			collector.collect(Row.of(strings[0],strings[1]));
		}
	}

}
