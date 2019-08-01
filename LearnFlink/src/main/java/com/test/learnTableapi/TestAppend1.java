package com.test.learnTableapi;

import com.test.util.connector.CustomSourceUtil;
import org.apache.flink.streaming.api.TimeCharacteristic;

/**
 * flink中的
 */
public class TestAppend1 extends CommonTable{
	public static void main(String[] args) {
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		tableEnvironment.registerTableSource("filesource", CustomSourceUtil.getTableRow());

//		testMethod1();

		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testMethod(){

	}
}
