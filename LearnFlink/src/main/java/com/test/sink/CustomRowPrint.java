package com.test.sink;

import com.test.util.TimeUtil;
import com.test.util.URLUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.types.Row;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;

public class CustomRowPrint extends RichSinkFunction<Row> {
	private String fileName;
	private Counter sum = null;
	private int time_index = Integer.MAX_VALUE;
	public CustomRowPrint(String fileName) {
		this.fileName = fileName;
	}

	public CustomRowPrint(String fileName,int time_index) {
		this.time_index = time_index;
	}

	@Override
	public void open(Configuration parameters) throws Exception {
//		MetricGroup metricGroup = getRuntimeContext().getMetricGroup().addGroup("customSum");
//		sum = metricGroup.counter("sum");
		super.open(parameters);
	}

	@Override
	public void invoke(Row value) throws Exception {
		String date = null;
		if (time_index != Integer.MAX_VALUE && time_index < value.getArity()){
			Timestamp timestamp = (Timestamp)value.getField(time_index);
			date = TimeUtil.toDate(timestamp.getTime());
		}
		writerFile(date +":"+ value.toString(),fileName);
	}
	public static synchronized void writerFile(String s,String fileName) throws IOException {
		if (fileName == null){
			fileName = "test.txt";
		}
		Path logFile = Paths.get( URLUtil.baseUrl + fileName);
		try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8, StandardOpenOption.APPEND)){
			writer.newLine();
			writer.write(s);
		}
	}
}
