package com.test.graph;

import org.apache.flink.runtime.io.network.partition.consumer.SingleInputGate;

/**
 * 我们都知道flink的任务会使用有向无环图的形式表达分为三层架构
 * 我把它理解为
 *
 * StreamGraph {@link org.apache.flink.streaming.api.graph.StreamGraph}  应用层
 *
 *
 * JobGraph {@link org.apache.flink.runtime.jobgraph.JobGraph}  中间层
 *
 * 每个任务节点的数据结构描述类 {@link org.apache.flink.runtime.jobgraph.JobVertex}
 *
 * 关键数据结构
 * results  存储所有的中间结果数据集IntermediateDataSet{@link org.apache.flink.runtime.jobgraph.IntermediateDataSet}
 * IntermediateDataSet 中间结果集 每一个中间数据集实例代表了和同一种类型的节点建立了连接
 * 比如：
 * a -> b
 * a -> c
 * a 节点会有两个中间数据集
 *
 * 每一个中间数据集描述的是和下一个节点的连接关系
 * producer  {@link org.apache.flink.runtime.jobgraph.JobVertex} 中间数据集的生产者 代表的a节点自己
 * consumers {@link org.apache.flink.runtime.jobgraph.JobEdge}  这个中间数据集的消费者，代表下一个节点的输入边
 *
 *
 * inputs 存储所有的输入边{@link org.apache.flink.runtime.jobgraph.JobEdge}
 * 一个job边代表了一个沟通渠道
 * target 代表的自己，目标节点
 * source 这个边关联的目标数据集
 * distributionPattern 这个边的分布式方式
 *
 * 注意： 1. 节点与节点之间通过中间数据集进行了隔离
 *       2. 中间数据集和边进行交流
 *
 *
 *
 *
 * ExecutionGraph {@link org.apache.flink.runtime.executiongraph.ExecutionGraph}  执行层
 * 根据jobgraph生成执行层面的节点
 *
 * JobGraph   ======》 ExecutionGraph
 * JobVertex  ======》 ExecutionJobVertex
 *  比如：
 *  a -----> b
 *  a -----> c
 * a 节点 2个并行度
 * b 节点 3个并行度
 * c 节点 1个并行度
 * ExecutionJobVertex {@link org.apache.flink.runtime.executiongraph.ExecutionJobVertex}
 * taskVertices 任务节点数和节点并行度相等，如果并行度小于零使用默认并行度
 * producedDataSets 自己是生产者的中间数据集个数也就是连接的数据节点个数 比如a的producedDataSets个数为2
 * inputs  个数等于输入的边数也就是JobVertex的inputs个数存储的是中间结果集{@link org.apache.flink.runtime.executiongraph.IntermediateResult}
 * ExecutionVertex 执行的真实节点
 *
 * a节点会生成2个ExecutionVertex实例
 * b节点会生成3个ExecutionVertex实例
 *
 * ExecutionVertex {@link org.apache.flink.runtime.executiongraph.ExecutionVertex}
 * resultPartitions 结果分区 这个分区是具体的执行节点比如a节点，会有两个producedDataSets同样的会生成两个IntermediateResultPartition
 * inputEdges 输入边 等于JobVertex的输入数 这个是2位数组为什么那
 * 每一个JobVertex的输入边是代表的相同的节点也就是a，当时a有2个并行度也就是说有两个执行节点，并且这个节点根据分布式方式
 * 也就是{@link org.apache.flink.runtime.jobgraph.DistributionPattern} 进行连接，这是一个JobVertex的JobEdge可能变为多个
 * ExecutionVertex的ExecutionEdge多个边因为连接的节点可能也有多个并行度比如b连接a但是a有多个并行度，b中的当个执行节点就可能
 * 和a中执行节点连接两次或者一次根据DistributionPattern决定
 * 同样的a的单个执行节点也可能和b一个两个或者全部节点进行连接
 *
 * 比如b连接a，b有三个并行度，a有两个并行度，b中的一个算子，和a中的两个算子可能都要建立连接，每个连接代表一个InputChannelDeploymentDescriptor
 * 这两个连接生成一个InputGateDeploymentDescriptor。因为这两个连接是和相同的两个算子进行了连接
 * 每一个InputGateDeploymentDescriptor会生成一个SingleInputGate 代表适合相同的两个算子进行连接
 * 比如b加入和a还有c进行了连接 那么b会有有两个SingleInputGate_a 和 SingleInputGate_b
 * 假如： a有2连个并行度 a ----> b ALL_TO_ALL 那么SingleInputGate_a中会有2个RemoteInputChannel或者LocalInputChannel
 * a ----> b
 * c ----> b
 *
 *
 *  b节点中的一个执行算子会通过RecordReader的next()方法读取数据，next()会调用hasnext方法
 *  再调用getNextRecord方法 这个方法会调用SingleInputGate的getNextBufferOrEvent()方法
 *  getNextBufferOrEvent()方法会执行requestPartitions方法，这个方法会通过RemoteInputChannel
 *  或者LocalInputChannel方法请求requestSubpartition和上一个节点的中间结果集建立联系进行数据交换
 *
 * 每一个InputChannelDeploymentDescriptor连接会生成一个RemoteInputChannel或者LocalInputChannel通道和上一些节点进行数据传输
 * 每个ExecutionEdge会被生成一个InputChannelDeploymentDescriptor类 描述类 每一个InputGateDeploymentDescriptor会生成一个SingleInputGate
 * {@link org.apache.flink.runtime.io.network.partition.consumer.SingleInputGate}
 *
 *
 * 节点与节点间的连接关系
 *
 * 同一种节点 多并行度
 * 不同节点   单并行度
 * 不同节点   多并行度
 *
 * 详细查看
 *
 *
 *
 *
 *
 */
public class TestJob {
}
