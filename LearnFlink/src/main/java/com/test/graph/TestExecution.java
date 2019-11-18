package com.test.graph;

import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.executiongraph.Execution;
import org.apache.flink.runtime.executiongraph.ExecutionGraph;
import org.apache.flink.runtime.executiongraph.ExecutionJobVertex;
import org.apache.flink.runtime.executiongraph.ExecutionVertex;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobgraph.JobVertexID;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

public class TestExecution {
	public static void main(String[] args) {
		testMethod1();
//		testMethod2();
	}

	public static void testMethod1(){
		JobID jobID = new JobID();
		String jobName = "jobName";
		JobGraph jobGraph = JobGraphUtil.getJobGraph2(jobID,jobName);
		ExecutionGraph executionGraph = ExecutionGraphUtil.getExecutionGraph(jobGraph,jobID,jobName);
		Set<Map.Entry<JobVertexID, ExecutionJobVertex>> set = executionGraph.getAllVertices().entrySet();
		for (Map.Entry<JobVertexID, ExecutionJobVertex> entry : set){
			ExecutionJobVertex executionJobVertex = entry.getValue();
			System.out.println("start");
			System.out.println(executionJobVertex.getName());
			ExecutionVertex[] executionVertices = executionJobVertex.getTaskVertices();
			for (int i = 0; i < executionVertices.length; i++) {
				ExecutionVertex executionVertex = executionVertices[i];
				System.out.println("任务名： "+executionVertex.getTaskName());
				System.out.println("并行子任务索引： "+executionVertex.getParallelSubtaskIndex());
				System.out.println("输入数： "+executionVertex.getNumberOfInputs());
			}
			System.out.println("end");
		}
	}

	public static void testMethod2(){
		JobID jobID = new JobID();
		String jobName = "jobName";
		JobGraph jobGraph = JobGraphUtil.getJobGraph2(jobID,jobName);
		ExecutionGraph executionGraph = ExecutionGraphUtil.getExecutionGraph(jobGraph,jobID,jobName);
		Set<Map.Entry<JobVertexID, ExecutionJobVertex>> set = executionGraph.getAllVertices().entrySet();
		for (Map.Entry<JobVertexID, ExecutionJobVertex> entry : set){
			ExecutionJobVertex executionJobVertex = entry.getValue();
			System.out.println("start");
			System.out.println(executionJobVertex.getName());
			ExecutionVertex[] executionVertices = executionJobVertex.getTaskVertices();
			for (int i = 0; i < executionVertices.length; i++) {
				ExecutionVertex executionVertex = executionVertices[i];
				Execution execution = new Execution(Executors.newSingleThreadExecutor()
					,executionVertex
					,2,11L, System.currentTimeMillis(), Time.milliseconds(1000));
				System.out.println("dd");
			}
			System.out.println("end");
		}
	}
}
