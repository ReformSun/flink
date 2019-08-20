package com.test.sink;

import com.test.util.FileWriter;
import com.test.util.URLUtil;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CustomPrintTuple<T extends Tuple> extends RichSinkFunction<T> implements CheckpointedFunction {
	private String fileName;
	private String threadName;

	public CustomPrintTuple(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void open(Configuration parameters) throws Exception {
		threadName = Thread.currentThread().getName();
		super.open(parameters);
	}

	@Override
	public void invoke(T value) throws Exception {
		FileWriter.writerFile(threadName + ":" + value.toString(),fileName);
	}

	@Override
	public void snapshotState(FunctionSnapshotContext context) throws Exception {

	}

	@Override
	public void initializeState(FunctionInitializationContext context) throws Exception {
	}
}
