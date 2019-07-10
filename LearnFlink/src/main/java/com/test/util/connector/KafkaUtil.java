package com.test.util.connector;

import com.test.connector.JsonTableSourceBuilder;
import com.test.util.common.StreamModelSchema;
import org.apache.flink.streaming.connectors.kafka.*;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.sources.tsextractors.ExistingField;
import org.apache.flink.table.sources.wmstrategies.BoundedOutOfOrderTimestamps;

import java.util.Properties;

public class KafkaUtil {
	public static FlinkKafkaConsumer09 getKafkaConsumer09Source(String topic){
		Properties propertie = new Properties();
		propertie.setProperty("input-topic",topic);
		propertie.setProperty("bootstrap.servers", "172.31.35.58:9092");
//		propertie.setProperty("bootstrap.servers", "172.31.24.36:9092");
		propertie.setProperty("group.id", "serverCollector");
		FlinkKafkaConsumer09 flinkKafkaConsumer09 = new FlinkKafkaConsumer09(
			propertie.getProperty("input-topic"),
			new StreamModelSchema(),
			propertie);
		return flinkKafkaConsumer09;
	}

	public static FlinkKafkaConsumer010 getKafkaConsumer010Source(String topic){
		Properties propertie = new Properties();
		propertie.setProperty("input-topic",topic);
		propertie.setProperty("bootstrap.servers", "172.31.35.58:9092");
//		propertie.setProperty("bootstrap.servers", "172.31.24.36:9092");
		propertie.setProperty("group.id", "serverCollector");
		FlinkKafkaConsumer010 flinkKafkaConsumer010 = new FlinkKafkaConsumer010(
			propertie.getProperty("input-topic"),
			new StreamModelSchema(),
			propertie);
		flinkKafkaConsumer010.setStartFromGroupOffsets();
		return flinkKafkaConsumer010;
	}

	public static KafkaTableSourceBase getKafkaTableSource(String topic, TableSchema tableSchema, String rowTimeName){
		Properties propertie = new Properties();
		propertie.setProperty("input-topic",topic);
//		propertie.setProperty("bootstrap.servers", "172.31.24.30:9092,172.31.24.36:9092");
		propertie.setProperty("bootstrap.servers", "172.31.35.58:9092");
//		propertie.setProperty("bootstrap.servers", "172.31.24.36:9092");
		propertie.setProperty("group.id", "serverCollector");
		JsonTableSourceBuilder jsonTableSourceBuilder = JsonTableSourceBuilder.builder().forTopic(propertie.getProperty("input-topic"));
		jsonTableSourceBuilder.withKafkaProperties(propertie);
		TableSchema.Builder builder  = TableSchema.builder();
		jsonTableSourceBuilder.withSchema(tableSchema).withRowtimeAttribute(rowTimeName, new ExistingField(rowTimeName),new BoundedOutOfOrderTimestamps(30000L));
		KafkaTableSourceBase kafkaTableSource = jsonTableSourceBuilder.build();
		return kafkaTableSource;
	}

	public static KafkaTableSourceBase getKafka11TableSource(String topic,TableSchema tableSchema,String rowTimeName){
		Properties propertie = new Properties();
		propertie.setProperty("input-topic",topic);
//		propertie.setProperty("bootstrap.servers", "172.31.24.30:9092,172.31.24.36:9092");
		propertie.setProperty("bootstrap.servers", "172.31.35.58:9092");
//		propertie.setProperty("bootstrap.servers", "172.31.24.36:9092");
		propertie.setProperty("group.id", "serverCollector");
		propertie.put("enable.auto.commit", "true");
		propertie.put("auto.commit.interval.ms", "10000");
		propertie.put("auto.offset.reset", "earliest");
		propertie.put("session.timeout.ms", "30000");
		propertie.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		propertie.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		JsonTableSourceBuilder jsonTableSourceBuilder = JsonTableSourceBuilder.builder().forTopic(propertie.getProperty("input-topic"));
		jsonTableSourceBuilder.withKafkaProperties(propertie);
		TableSchema.Builder tableSchemaBuilder = TableSchema.builder();
		jsonTableSourceBuilder.withSchema(tableSchema).withRowtimeAttribute(rowTimeName, new ExistingField(rowTimeName),new BoundedOutOfOrderTimestamps(30000L));
		KafkaTableSourceBase kafkaTableSource = jsonTableSourceBuilder.build();
		return kafkaTableSource;
	}
}
