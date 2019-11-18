package org.apache.flink.metrics.prometheus;


import io.prometheus.client.CollectorRegistry;
import org.apache.flink.metrics.*;
import org.apache.flink.metrics.reporter.AbstractReporter;
import org.apache.flink.metrics.reporter.Scheduled;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link org.apache.flink.runtime.metrics.MetricRegistryImpl}
 */
public class FileMetricReporter {
	/**
	 * 只展示我自定义的测量值
	 * flink_taskmanager_job_task_operator_numRecordsIn
	 * flink_taskmanager_job_task_numRecordsOut
	 * flink_taskmanager_job_task_numRecordsIn
	 * flink_taskmanager_job_task_operator_commitsSucceeded
	 * flink_taskmanager_job_task_operator_commitsFailed
	 */
	public static void report(CollectorRegistry registry, String job){

		try {
			Path logFile = Paths.get("/Users/apple/Documents/GitHub/flink-1.8/LearnFlink/src/main/resources/metric/metric.txt");
			List<String> names = new ArrayList<>();
//			names.add("flink_taskmanager_job_task_operator_KafkaConsumer_topic_partition_currentOffsets");

			String prefix = "flink_taskmanager_job_task_operator_KafkaConsumer";

			try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8, StandardOpenOption.APPEND)){
				TextFormat.write004(writer, registry.metricFamilySamples(),names,prefix);
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
