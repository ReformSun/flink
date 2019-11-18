package com.test.learnMetric;

import com.test.learnMetric.executiongraph.ExecutionGraphTest;
import com.test.learnMetric.executiongraph.metric.UpTimeGauge;
import com.test.learnMetric.group.GroupUtil;
import com.test.util.StreamExecutionEnvUtil;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.runtime.jobgraph.JobStatus;
import org.apache.flink.runtime.metrics.MetricRegistryConfiguration;
import org.apache.flink.runtime.metrics.MetricRegistryImpl;
import org.apache.flink.runtime.metrics.groups.JobManagerJobMetricGroup;
import org.apache.flink.runtime.metrics.groups.TaskIOMetricGroup;

public class TestTaskMetricGroup {
	private static MetricRegistryImpl metricRegistry;
	public static void main(String[] args) {
		Configuration configuration = StreamExecutionEnvUtil.getConfiguration();
		MetricRegistryConfiguration metricRegistryConfiguration = MetricRegistryConfiguration.fromConfiguration(configuration);
		metricRegistry = new MetricRegistryImpl(metricRegistryConfiguration);
		testMethod1();
//		testMethod2();
	}

	public static void testMethod1(){
		JobID jobID = new JobID();
		String job_name = "job_name_test_task_3";

		JobManagerJobMetricGroup jobManagerJobMetricGroup = GroupUtil.getJobManagerJobMetricGroup(metricRegistry,jobID,job_name);

		long[] stateTimestamps = new long[JobStatus.values().length];
		stateTimestamps[JobStatus.RUNNING.ordinal()] = System.currentTimeMillis();
		ExecutionGraphTest executionGraphTest = new ExecutionGraphTest(JobStatus.RUNNING,stateTimestamps);
		jobManagerJobMetricGroup.gauge(UpTimeGauge.METRIC_NAME,new UpTimeGauge(executionGraphTest));

		TaskIOMetricGroup taskIOMetricGroup = GroupUtil.getTaskIOMetricGroup(metricRegistry,jobID,job_name,"Source:operatorName_Sink:operatorName");
		Counter counter = taskIOMetricGroup.getNumRecordsInCounter();
		Counter counter1 = taskIOMetricGroup.getNumRecordsOutCounter();

		for (int i = 0; i < 10000; i++) {
			counter1.inc();
			counter.inc();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
