package com.test.graph;

import org.apache.flink.api.common.JobID;
import org.apache.flink.runtime.io.network.partition.ResultPartitionType;
import org.apache.flink.runtime.jobgraph.DistributionPattern;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobgraph.JobVertex;
import org.apache.flink.runtime.jobgraph.ScheduleMode;
import org.apache.flink.runtime.jobgraph.jsonplan.JsonPlanGenerator;
import org.apache.flink.runtime.jobmanager.scheduler.SlotSharingGroup;

public class JobGraphUtil {
	/**
	 *  jobVertex3 --> jobVertex2 ---> jobVertex1
	 */
	public static JobGraph getJobGraph(JobID jobID ,String jobName){
		JobGraph jobGraph = new JobGraph(jobID,jobName);
		jobGraph.setScheduleMode(ScheduleMode.LAZY_FROM_SOURCES);

		SlotSharingGroup slotSharingGroup = new SlotSharingGroup();

		JobVertex jobVertex1 = new JobVertex("test_1");
		jobVertex1.setParallelism(2);
		jobVertex1.setSlotSharingGroup(slotSharingGroup);
		jobVertex1.setInvokableClass(TestClass.class);

		JobVertex jobVertex2 = new JobVertex("test_2");
		jobVertex2.setParallelism(3);
		jobVertex2.setSlotSharingGroup(slotSharingGroup);
		jobVertex2.setInvokableClass(TestClass.class);

		JobVertex jobVertex3 = new JobVertex("test_3");
		jobVertex3.setParallelism(1);
		jobVertex3.setSlotSharingGroup(slotSharingGroup);
		jobVertex3.setInvokableClass(TestClass.class);

		jobVertex1.connectNewDataSetAsInput(jobVertex2, DistributionPattern.POINTWISE, ResultPartitionType.PIPELINED);
		jobVertex2.connectNewDataSetAsInput(jobVertex3,DistributionPattern.POINTWISE,ResultPartitionType.PIPELINED);
		jobGraph.addVertex(jobVertex2);
		jobGraph.addVertex(jobVertex1);
		jobGraph.addVertex(jobVertex3);
		System.out.println(JsonPlanGenerator.generatePlan(jobGraph));
		return jobGraph;
	}

	/**
	 *  jobVertex2 ---> jobVertex1
	 */
	public static JobGraph getJobGraph2(JobID jobID ,String jobName){
		JobGraph jobGraph = new JobGraph(jobID,jobName);
		jobGraph.setScheduleMode(ScheduleMode.LAZY_FROM_SOURCES);

		SlotSharingGroup slotSharingGroup = new SlotSharingGroup();

		JobVertex jobVertex1 = new JobVertex("test_1");
		jobVertex1.setParallelism(2);
		jobVertex1.setSlotSharingGroup(slotSharingGroup);
		jobVertex1.setInvokableClass(TestClass.class);

		JobVertex jobVertex2 = new JobVertex("test_2");
		jobVertex2.setParallelism(3);
		jobVertex2.setSlotSharingGroup(slotSharingGroup);
		jobVertex2.setInvokableClass(TestClass.class);
		jobVertex2.connectNewDataSetAsInput(jobVertex1, DistributionPattern.POINTWISE, ResultPartitionType.PIPELINED);
		jobGraph.addVertex(jobVertex2);
		jobGraph.addVertex(jobVertex1);
		System.out.println(JsonPlanGenerator.generatePlan(jobGraph));
		return jobGraph;
	}
}
