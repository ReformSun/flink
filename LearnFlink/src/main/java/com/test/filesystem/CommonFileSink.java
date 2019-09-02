package com.test.filesystem;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileConstants;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.fs.AvroKeyValueSinkWriter;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;
import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.Map;

public class CommonFileSink {
	public static SinkFunction<Row> createFileSink(){
		BucketingSink bucketingSink = new BucketingSink<Row>("./LearnFlink/src/main/resources");
		Map<String, String> properties = new HashMap<>();
		Schema longSchema = Schema.create(Schema.Type.LONG);
		String keySchema = longSchema.toString();
		String valueSchema = longSchema.toString();
		properties.put(AvroKeyValueSinkWriter.CONF_OUTPUT_KEY_SCHEMA, keySchema);
		properties.put(AvroKeyValueSinkWriter.CONF_OUTPUT_VALUE_SCHEMA, valueSchema);
		properties.put(AvroKeyValueSinkWriter.CONF_COMPRESS, Boolean.toString(true));
		properties.put(AvroKeyValueSinkWriter.CONF_COMPRESS_CODEC, DataFileConstants.SNAPPY_CODEC);
		AvroKeyValueSinkWriter avroKeyValueSinkWriter = new AvroKeyValueSinkWriter(properties);

		return bucketingSink;
	}

	/**
	 * 当同时设置设置桶的大小 桶的翻滚周期 检查桶的周期时间 非活跃桶的阈值时间时
	 * 因为桶的关闭逻辑为 根据检查桶的周期定时去判断最新的通的写入时间是否小于当前时间减去阈值时间如果小于关闭桶或者
	 * 桶创建时间是否小于当前时间减去桶的翻滚时间如果小于关闭桶
	 *
	 * 但是每次数据过来也会做一个桶是否翻滚的判断根据三个条件
	 * 1. 对于任务的写入没有文件被创建
	 * 2. 桶的写入最大尺寸已经达到
	 * 3. 当前桶的写入周期已经超过桶的翻滚周期
	 * 并且如果确定是翻滚就会重新创建一个桶。并打开这个桶
	 * 当时如果桶还没有并关闭就会出现错误报警
	 * 因为痛的关闭和创建打开是两个逻辑，所以会出现问题
	 * @return
	 */
	public static SinkFunction<Tuple2<Long, Long>> createFileSink1(){
		BucketingSink<Tuple2<Long, Long>> sink = new BucketingSink<Tuple2<Long, Long>>("file:///Users/apple/Documents/GitHub/flink-1.8/LearnFlink/src/main/resources/avro");
		sink.setBucketer(new DateTimeBucketer<Tuple2<Long, Long>>("yyyy-MM-dd/HH/mm/"));
		sink.setPendingSuffix(".avro");
		Map<String, String> properties = new HashMap<>();
		Schema longSchema = Schema.create(Schema.Type.LONG);
		String keySchema = longSchema.toString();
		String valueSchema = longSchema.toString();
		properties.put(AvroKeyValueSinkWriter.CONF_OUTPUT_KEY_SCHEMA, keySchema);
		properties.put(AvroKeyValueSinkWriter.CONF_OUTPUT_VALUE_SCHEMA, valueSchema);
		properties.put(AvroKeyValueSinkWriter.CONF_COMPRESS, Boolean.toString(true));
		properties.put(AvroKeyValueSinkWriter.CONF_COMPRESS_CODEC, DataFileConstants.SNAPPY_CODEC);

		sink.setWriter(new AvroKeyValueSinkWriter<Long, Long>(properties));

		// 设置检查非活跃桶的间隔时间
		sink.setInactiveBucketCheckInterval(10000);
		// 设置桶关闭的时间阈值 当桶1秒钟没有写入数据时。关闭桶
		sink.setInactiveBucketThreshold(1000);

		// 桶是否应该被滚动 通过设置桶的大小和桶的滚动时间间隔
		// 1. 对于任务的写入没有文件被创建
		// 2. 桶的写入最大尺寸已经达到
		// 3. 当前桶的写入周期已经超过桶的翻滚周期


		//		sink.setBatchSize(1024 * 1024 * 64); // this is 64 MB,
//		减少桶的尺寸和大小
//		sink.setBatchSize(1024 * 1024); // this is 1 MB,
		sink.setBatchSize(1024); // this is 1 KB,
		// 设置桶滚动时间间隔 默认为无限大
		sink.setBatchRolloverInterval(10000);
		return sink;
	}
}
