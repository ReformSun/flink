package com.test.learnMetric;

import com.test.learnMetric.executiongraph.ExecutionGraphTest;
import com.test.learnMetric.executiongraph.metric.UpTimeGauge;
import com.test.learnMetric.group.GroupUtil;
import com.test.util.StreamExecutionEnvUtil;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Meter;
import org.apache.flink.runtime.jobgraph.JobStatus;
import org.apache.flink.runtime.jobgraph.OperatorID;
import org.apache.flink.runtime.metrics.MetricRegistryConfiguration;
import org.apache.flink.runtime.metrics.MetricRegistryImpl;
import org.apache.flink.runtime.metrics.groups.JobManagerJobMetricGroup;
import org.apache.flink.runtime.metrics.groups.OperatorIOMetricGroup;

public class TestOperatorIOMetricGroup {
	private static MetricRegistryImpl metricRegistry;
	public static void main(String[] args) {
		Configuration configuration = StreamExecutionEnvUtil.getConfiguration();
		MetricRegistryConfiguration metricRegistryConfiguration = MetricRegistryConfiguration.fromConfiguration(configuration);
		metricRegistry = new MetricRegistryImpl(metricRegistryConfiguration);
//		testMethod1();
//		testMethod2();
		testMethod3();
	}

	public static void testMethod1(){
		OperatorIOMetricGroup operatorIOMetricGroup = GroupUtil.getOperatorIOMetricGroup(metricRegistry);
		Counter counter = operatorIOMetricGroup.getNumRecordsInCounter();

		for (int i = 0; i < 1000; i++) {
			counter.inc();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void testMethod2(){
		OperatorIOMetricGroup operatorIOMetricGroup = GroupUtil.getOperatorIOMetricGroup(metricRegistry);
		Meter meter = operatorIOMetricGroup.getNumRecordsInRateMeter();

		for (int i = 0; i < 1000; i++) {
			meter.markEvent();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void testMethod3(){
		JobID jobID = new JobID();
		String job_name = "job_name_test3";
		OperatorID operatorID = new OperatorID();
		String operatorName = "operator_name_test3";

		JobManagerJobMetricGroup jobManagerJobMetricGroup = GroupUtil.getJobManagerJobMetricGroup(metricRegistry,jobID,job_name);

		long[] stateTimestamps = new long[JobStatus.values().length];
		stateTimestamps[JobStatus.RUNNING.ordinal()] = System.currentTimeMillis();
		ExecutionGraphTest executionGraphTest = new ExecutionGraphTest(JobStatus.RUNNING,stateTimestamps);
		jobManagerJobMetricGroup.gauge(UpTimeGauge.METRIC_NAME,new UpTimeGauge(executionGraphTest));


		OperatorIOMetricGroup operatorIOMetricGroup = GroupUtil.getOperatorIOMetricGroup(metricRegistry,jobID,job_name,operatorID,operatorName);
		Meter meter = operatorIOMetricGroup.getNumRecordsInRateMeter();

		for (int i = 0; i < 1000; i++) {
			meter.markEvent();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
