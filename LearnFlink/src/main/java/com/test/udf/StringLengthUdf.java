package com.test.udf;

import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.ScalarFunction;

public class StringLengthUdf extends ScalarFunction{
	@Override
	public void open(FunctionContext context) throws Exception {
		super.open(context);
	}

	public long eval(String a) {
		return a == null ? 0 : a.length();
	}
	public long eval(String b, String c) {
		return eval(b) + eval(c);
	}
	//可选，close方法可不写。
	@Override
	public void close() {
	}


}
