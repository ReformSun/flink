package com.test.learnMetric.group;

import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.Option;
import org.apache.flink.configuration.ConfigurationUtils;
import org.apache.flink.runtime.executiongraph.ExecutionAttemptID;
import org.apache.flink.runtime.jobgraph.JobVertexID;
import org.apache.flink.runtime.jobgraph.OperatorID;
import org.apache.flink.runtime.metrics.MetricRegistry;
import org.apache.flink.runtime.metrics.groups.*;
import org.apache.flink.runtime.metrics.util.MetricUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class GroupUtil {
	private static int index = 0;
	public static TaskMetricGroup getTaskMetricGroup(MetricRegistry metricRegistry){
		return getTaskMetricGroup(metricRegistry,new JobID(),"job_name","taskName");
	}

	public static TaskMetricGroup getTaskMetricGroup(MetricRegistry metricRegistry,JobID jobID,String jobName,String taskName){
		JobVertexID vertexId  = new JobVertexID();
		ExecutionAttemptID executionAttemptID = new ExecutionAttemptID();
		int subtaskIndex = 1;
		int attemptNumber = 1;

		TaskMetricGroup taskMetricGroup = new TaskMetricGroup(metricRegistry,
			getTaskManagerJobMetricGroup(metricRegistry,jobID,jobName),vertexId,
			executionAttemptID,taskName,subtaskIndex,attemptNumber);

		return taskMetricGroup;
	}

	public static TaskIOMetricGroup getTaskIOMetricGroup(MetricRegistry metricRegistry){
		return getTaskMetricGroup(metricRegistry).getIOMetricGroup();
	}

	public static TaskIOMetricGroup getTaskIOMetricGroup(MetricRegistry metricRegistry,JobID jobID,String jobName,String taskName){
		return getTaskMetricGroup(metricRegistry,jobID,jobName,taskName).getIOMetricGroup();
	}

	public static TaskManagerJobMetricGroup getTaskManagerJobMetricGroup(MetricRegistry metricRegistry,JobID jobID,String jobName){
		System.out.println("JobID: " + jobID.toString());
		TaskManagerJobMetricGroup taskManagerJobMetricGroup = new TaskManagerJobMetricGroup(metricRegistry
			,getTaskManagerMetricGroup(metricRegistry),jobID,jobName);
		return taskManagerJobMetricGroup;
	}

	public static TaskManagerMetricGroup getTaskManagerMetricGroup(MetricRegistry metricRegistry){
		String hostname = "localhost";
		String taskManagerId = "taskManagerId";
		TaskManagerMetricGroup taskManagerMetricGroup = new TaskManagerMetricGroup(metricRegistry,hostname,taskManagerId);
		return taskManagerMetricGroup;
	}

	public static OperatorMetricGroup getOperatorMetricGroup(MetricRegistry metricRegistry){
		String operatorName = "operatorName";
		OperatorID operatorID = new OperatorID();
		System.out.println("operatorID: " + operatorID.toString());
		OperatorMetricGroup operatorMetricGroup = new OperatorMetricGroup(metricRegistry,getTaskMetricGroup(metricRegistry),operatorID,operatorName);
		return operatorMetricGroup;
	}

	public static OperatorMetricGroup getOperatorMetricGroup(MetricRegistry metricRegistry,JobID jobID,String jobName,OperatorID operatorID,String operatorName){
		System.out.println("operatorID: " + operatorID.toString());
		OperatorMetricGroup operatorMetricGroup = new OperatorMetricGroup(metricRegistry,getTaskMetricGroup(metricRegistry,jobID,jobName,"task_name"),operatorID,operatorName);
		return operatorMetricGroup;
	}

	public static OperatorMetricGroup getOperatorMetricGroup(MetricRegistry metricRegistry,JobID jobID,String jobName,OperatorID operatorID,String operatorName,String taskName){
		System.out.println("operatorID: " + operatorID.toString());
		OperatorMetricGroup operatorMetricGroup = new OperatorMetricGroup(metricRegistry,getTaskMetricGroup(metricRegistry,jobID,jobName,taskName),operatorID,operatorName);
		return operatorMetricGroup;
	}

	public static OperatorIOMetricGroup getOperatorIOMetricGroup(MetricRegistry metricRegistry){
		return getOperatorMetricGroup(metricRegistry).getIOMetricGroup();
	}

	public static OperatorIOMetricGroup getOperatorIOMetricGroup(MetricRegistry metricRegistry,JobID jobID,String jobName,OperatorID operatorID,String operatorName){
		return getOperatorMetricGroup(metricRegistry,jobID,jobName,operatorID,operatorName).getIOMetricGroup();
	}


	public static JobManagerJobMetricGroup getJobManagerJobMetricGroup(MetricRegistry metricRegistry,JobID jobID,String jobName){
		JobManagerJobMetricGroup jobManagerJobMetricGroup = new JobManagerJobMetricGroup(metricRegistry,getJobManagerMetricGroup(metricRegistry),jobID,jobName);
		return jobManagerJobMetricGroup;
	}

	public static JobManagerMetricGroup getJobManagerMetricGroup(MetricRegistry metricRegistry){
		JobManagerMetricGroup jobManagerMetricGroup = null;
		jobManagerMetricGroup = MetricUtils.instantiateJobManagerMetricGroup(
			metricRegistry,
			"localhost",
			Optional.of(Time.of(1, TimeUnit.MINUTES)));
		return jobManagerMetricGroup;
	}






}
