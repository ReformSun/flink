package com.test.udtf;


import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.TableFunction;

public class SplitUdtf extends TableFunction<String> {
	// 可选， open方法可不编写。若编写，需要添加声明'import org.apache.flink.table.functions.FunctionContext;'。
	@Override
	public void open(FunctionContext context) {
		// ... ...
	}

	public void eval(String str) {
		String[] split = str.split("\\|");
		for (String s : split) {
			collect(s);
		}
	}

	// 可选，close方法可不编写。
	@Override
	public void close() {
		// ... ...
	}
}
