package com.test.conf;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.CoreOptions;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.core.fs.FileSystem;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * {@link org.apache.flink.configuration.JobManagerOptions}
 * {@link org.apache.flink.configuration.CoreOptions}
 * {@link org.apache.flink.configuration.TaskManagerOptions}
 * {@link org.apache.flink.configuration.HighAvailabilityOptions}
 * {@link org.apache.flink.configuration.HistoryServerOptions}
 * {@link org.apache.flink.configuration.HeartbeatManagerOptions}
 * {@link org.apache.flink.configuration.MetricOptions}
 * {@link org.apache.flink.configuration.OptimizerOptions}
 * {@link org.apache.flink.configuration.ResourceManagerOptions}
 * {@link org.apache.flink.configuration.RestOptions}
 * {@link org.apache.flink.configuration.WebOptions}
 * {@link org.apache.flink.configuration.SecurityOptions}
 * {@link org.apache.flink.configuration.AkkaOptions}
 * {@link org.apache.flink.configuration.AlgorithmOptions}
 * {@link org.apache.flink.configuration.BlobServerOptions}
 * {@link org.apache.flink.configuration.CheckpointingOptions}
 * {@link org.apache.flink.configuration.JobManagerOptions}
 * {@link org.apache.flink.configuration.JobManagerOptions}
 *
 *
 * {@link org.apache.flink.configuration.ConfigConstants}
 */
public class TestConf {
	public static void main(String[] args) {
//		testMethod1();
		testMethod3();
	}

	/**
	 * jobmananger
	 */
	public static void testMethod1(){
		Configuration configuration  = new Configuration();
//		configuration.getString(ConfigOption)
		System.out.println(JobManagerOptions.ADDRESS.defaultValue());
	}
	/**
	 * taskeMananger
	 */
	public static void testMethod2(){
		Configuration configuration  = new Configuration();
	}


	public static void testMethod3(){
		Configuration configuration  = new Configuration();
		configuration.setString(CoreOptions.DEFAULT_FILESYSTEM_SCHEME,"hdfs://localhost:9000");
		try {
			FileSystem.initialize(configuration);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			FileSystem.get(new URI("hdfs://localhost:9000"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	public static void testMethod4(){

	}
}
