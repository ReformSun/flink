package com.test.learnMetric;

import com.test.learnMetric.group.GroupUtil;
import com.test.util.StreamExecutionEnvUtil;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.runtime.metrics.MetricRegistryConfiguration;
import org.apache.flink.runtime.metrics.MetricRegistryImpl;
import org.apache.flink.runtime.metrics.groups.TaskIOMetricGroup;

public class TestSink {
	private static MetricRegistryImpl metricRegistry;
	public static void main(String[] args) {
		Configuration configuration = StreamExecutionEnvUtil.getConfiguration();
		MetricRegistryConfiguration metricRegistryConfiguration = MetricRegistryConfiguration.fromConfiguration(configuration);
		metricRegistry = new MetricRegistryImpl(metricRegistryConfiguration);
		testMethod1();
//		testMethod2();
//		testMethod3();
	}

	public static void testMethod1(){
		JobID jobID = new JobID();
		String job_name = "job_name_sink";
		TaskIOMetricGroup taskIOMetricGroup = GroupUtil.getTaskIOMetricGroup(metricRegistry,jobID,job_name,"taskName");
		Counter counter =  taskIOMetricGroup.getNumRecordsInCounter();
		for (int i = 0; i < 1000 ; i++) {
			counter.inc();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
