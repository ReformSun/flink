package com.test.graph;

import org.apache.flink.api.common.JobID;
import org.apache.flink.runtime.io.network.partition.ResultPartitionType;
import org.apache.flink.runtime.jobgraph.DistributionPattern;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobgraph.JobVertex;
import org.apache.flink.runtime.jobgraph.ScheduleMode;
import org.apache.flink.runtime.jobgraph.jsonplan.JsonPlanGenerator;
import org.apache.flink.runtime.jobmanager.scheduler.SlotSharingGroup;

/**
 * 对于flink中生成的有向无环图的理解
 *
 * 关键点： 分布模式  {@link org.apache.flink.runtime.jobgraph.DistributionPattern}
 *
 * 什么是分布模式：一个生产者的子任务连接到消费者的子任务的分布式模式
 *
 * ALL_TO_ALL  每一个生产者的子任务都要连接每一个消费者的子任务
 * POINTWISE   每一个生产者的子任务连接消费者的一个或者多个子任务
 *
 * 关键点： 结果分区类型 {@link org.apache.flink.runtime.io.network.partition.ResultPartitionType}
 * 1. BLOCKING    三个false  表示当分区已经产生数据不立即被消费，当没有被消费时，分区不产生背压，不设置有限制的网络缓冲区（仅在生成完整结果后向下游发送数据）
 * 2. PIPELINED   true true false  （一旦产生数据就可以持续向下游发送有限数据流或无限数据流。）
 * 3. PIPELINED_BOUNDED  三个true
 *
 * 上面连个关键点关注的是分布式节点间的连接方式和节点与节点间的数据传输方式
 *
 *
 * SlotSharingGroup {@link org.apache.flink.runtime.jobmanager.scheduler.SlotSharingGroup} 这是一种温和的约束
 * 每一个taskmanager可以开辟多个slot提供给jobmanager分配给flink任务中的子任务使用，
 * 通过设置SlotSharingGroup可以对同一个job的不同的task的子任务进行分组
 * 使他们可以共享一个slot
 *
 * 进一步的理解： 同一个job任务的不同task的sub task可以共享同一个slot 这样可以把同一个job的一个pipeline放到一个slot中
 *
 * flink任务所需要的slot数与job的最高并行度一致
 *
 *
 * CoLocationGroup {@link org.apache.flink.runtime.jobmanager.scheduler.CoLocationGroup}  这是一种强烈的约束
 * 可以保证所有并行度相同的子任务可以运行在同一个slot中主要用于迭代流(训练机器学习模型)
 *
 * 进一步的理解： 同一个job任务的同一task的sub task可以共享同一个slot
 *
 * 这里面牵扯三个概念：
 * isPipelined     当这个分区已经被生产出数据时，是否被消费
 * hasBackPressure  没有消费的时候这个分区是否产生背压
 * isBounded   这个分区是否使用一个有限制的网络缓冲区
 *
 * 问题1： 连接方式
 * 问题2： 并行度
 *
 *
 * 关键点：调度策略
 * {@link org.apache.flink.runtime.jobgraph.ScheduleMode}
 *
 * LAZY_FROM_SOURCES 上游数据准备好了，部署下游任务
 *
 * EAGER  同时部署多有任务，同时部署所有子任务
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class TestJobGraph1 {
	public static void main(String[] args) {
		testMethod1();
	}

	/**
	 * 节点一 并行度2
	 * 节点二 并行度3
	 *
	 *
	 */
	public static void testMethod1(){
		JobID jobID = new JobID();
		String jobName = "jobName";
		JobGraph jobGraph = new JobGraph(jobID,jobName);
		jobGraph.setScheduleMode(ScheduleMode.LAZY_FROM_SOURCES);

		SlotSharingGroup slotSharingGroup = new SlotSharingGroup();

		JobVertex jobVertex1 = new JobVertex("test_1");
		jobVertex1.setParallelism(2);
		jobVertex1.setSlotSharingGroup(slotSharingGroup);
		JobVertex jobVertex2 = new JobVertex("test_2");
		jobVertex2.setParallelism(3);
		jobVertex2.setSlotSharingGroup(slotSharingGroup);


		jobVertex2.connectNewDataSetAsInput(jobVertex1, DistributionPattern.POINTWISE, ResultPartitionType.PIPELINED);
		jobGraph.addVertex(jobVertex2);
		jobGraph.addVertex(jobVertex1);
		System.out.println(JsonPlanGenerator.generatePlan(jobGraph));

	}


	/**
	 * 节点一 并行度2
	 * 节点二 并行度3
	 */
	public static void testMethod2(){
		JobID jobID = new JobID();
		String jobName = "jobName";
		JobGraph jobGraph = new JobGraph(jobID,jobName);
		jobGraph.setScheduleMode(ScheduleMode.LAZY_FROM_SOURCES);

		SlotSharingGroup slotSharingGroup = new SlotSharingGroup();

		JobVertex jobVertex1 = new JobVertex("test_1");
		jobVertex1.setParallelism(1);
		jobVertex1.setSlotSharingGroup(slotSharingGroup);
		JobVertex jobVertex2 = new JobVertex("test_2");
		jobVertex2.setParallelism(1);
		jobVertex2.setSlotSharingGroup(slotSharingGroup);
		JobVertex jobVertex3 = new JobVertex("test_3");
		jobVertex3.setParallelism(1);
		jobVertex3.setSlotSharingGroup(slotSharingGroup);

		jobVertex1.connectNewDataSetAsInput(jobVertex2, DistributionPattern.POINTWISE, ResultPartitionType.PIPELINED);
		jobVertex2.connectNewDataSetAsInput(jobVertex3,DistributionPattern.POINTWISE,ResultPartitionType.PIPELINED);
		jobGraph.addVertex(jobVertex2);
		jobGraph.addVertex(jobVertex1);
		jobGraph.addVertex(jobVertex3);
		System.out.println(JsonPlanGenerator.generatePlan(jobGraph));

	}
}
