package com.test.learnTableapi;

import com.test.udaf.CountUdaf;
import com.test.udf.StringLengthUdf;

public class UDAFTCase extends UDCommonTable{
	public static void main(String[] args) {
		testMethod1();
	}
	public static void testMethod1(){
		env.setParallelism(1);
		tableEnvironment.registerFunction("count1",new CountUdaf());
		String sql = "select count1(Company) from table1";
		procTimePrint(sql);
	}

}
