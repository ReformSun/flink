package com.test.learnTableapi;

import com.test.udf.StringLengthUdf;
import com.test.udtf.ParseUdtf;
import com.test.udtf.SplitUdtf;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.table.api.Table;
import org.apache.flink.types.Row;

import java.util.Arrays;
import java.util.List;


/**
 * 自定义表值函数，将0个、1个或多个标量值作为输入参数
 * 与标量函数不同，表值函数可以返回任意数量的行作为输出，
 * 而不仅是1个值。返回的行可以由1个或多个列组成。
 *
 * 可以实现多行数据的返回
 * 多列返回
 *
 */
public class UDTFTCase extends CommonTable{
	public static List<Row> table1 = Arrays.asList(
		Row.of( "IBM|美国",3335)
		, Row.of( "理想|中国", 23333)
		, Row.of( "阿里|中国", 3112)
		, Row.of( "华为|中国", 4334));

	public static List<Row> table2 = Arrays.asList(
		Row.of( "IBM|美国",3335)
		, Row.of( "理想|中国", 23333)
		, Row.of( "阿里|中国", 3112)
		, Row.of( "error", 4334));
	public static void main(String[] args) {
//		testMethod1();
		testMethod2();
	}

	public static void testMethod1(){
		tableEnvironment.registerFunction("parseUdtf",new ParseUdtf());
		String sql = "select T.company,T.country,S.OrderNumber from table1 as S ,lateral table(parseUdtf(S.Company)) as T(company,country)";
		procTimePrint(sql);
	}

	/**
	 * left join：左表的每一行数据都会关联上UDTF 产出的每一行数据，如果UDTF不产出任何数据，
	 * 那么这1行的UDTF的字段会用null值填充。
	 * 语句后面必须加 on true
	 */
	public static void testMethod2(){
		tableEnvironment.registerFunction("parseUdtf",new ParseUdtf());
		String sql = "select T.company,T.country,S.OrderNumber from table2 as S left join lateral table(parseUdtf(S.Company)) as T(company,country) on true";
		procTimePrint(sql);
	}

	public static void procTimePrint(String sql){
		env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

		DataStreamSource<Row> dataStreamSource_1 = env.fromCollection(table1);
		DataStreamSource<Row> dataStreamSource_2 = env.fromCollection(table2);

		tableEnvironment.registerDataStream("table1",dataStreamSource_1,"Company,OrderNumber");
		tableEnvironment.registerDataStream("table2",dataStreamSource_2,"Company,OrderNumber");

		Table table = tableEnvironment.sqlQuery(sql);
		DataStream<Tuple2<Boolean,Row>> dataStream = tableEnvironment.toRetractStream(table,Row.class);
		dataStream.addSink(new SqlOverviewITCase.RetractingSink());
		try {
			env.execute("test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
