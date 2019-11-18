package com.test.io.netty;

/**
 * flink 使用流量控制和不使用流量控制
 */
public class TestCreditBased {


	/**
	 * 使用流量控制
	 * 例子： a 任务2个并行度分为a1 a2 b任务3个并行度分为b1 b2 b3 a和b的两节方式为all to all
	 * 那么b1 会同时和a1 a2 进行连接 b1针对a1和a2的连接会有一个SingleInputGate类进行服务，SingleInputGate
	 * 会拥有两个InputChannel分别和a1和a2连接
	 * 这时a1和a2共享有可能贡献一个通道也可能不共享这一点再确认
	 *
	 * 但是b1连接a1和a2的InputChannel会共享同一个缓存池
	 *
	 * 如果使用流量控制策略，还会为各个InputChannel分配独有缓冲区和浮动缓冲区
	 * 代码重点为：NetworkEnvironment的方法registerTask被调用也就是任务被注册到网络环境中时，
	 * 会遍历任务所拥有的入口，并调用setupInputGate方法设置入口信息
	 * 详情看类{@link org.apache.flink.runtime.io.network.NetworkEnvironment}的setupInputGate方法
	 *
	 * 使用流控模式 当有数据到来时执行逻辑是：{@link org.apache.flink.runtime.io.network.netty.CreditBasedPartitionRequestClientHandler}
	 * 会调用CreditBasedPartitionRequestClientHandler的channelRead的方法，channelRead方法会调用反序列化方法
	 * decodeMsg
	 *
	 *
	 *
	 */
	public static void testMethod1(){

	}


}
