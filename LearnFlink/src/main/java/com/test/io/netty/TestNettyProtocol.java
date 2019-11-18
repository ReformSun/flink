package com.test.io.netty;

/**
 * Defines the server and client channel handlers, i.e. the protocol, used by netty.
 * 定义服务端和客户端的通道处理
 * {@link org.apache.flink.runtime.io.network.netty.NettyProtocol}
 */
public class TestNettyProtocol {

	/**
	 * 服务端处理
	 * Returns the server channel handlers.
	 *
	 * <pre>
	 * +-------------------------------------------------------------------+
	 * |                        SERVER CHANNEL PIPELINE                    |
	 * {@link org.apache.flink.runtime.io.network.netty.PartitionRequestQueue}
	 * |                                                                   |
	 * |    +----------+----------+ (3) write  +----------------------+    |
	 * |    | Queue of queues     +----------->| Message encoder      |{@link org.apache.flink.runtime.io.network.netty.NettyMessage.NettyMessageEncoder}
	 * |    +----------+----------+            +-----------+----------+    |
	 * |              /|\                                 \|/              |
	 * |               | (2) enqueue                       |               |
	 * |    +----------+----------+
	 * {@link org.apache.flink.runtime.io.network.netty.PartitionRequestServerHandler}
	 * |    | Request handler     |                        |               |
	 * |    +----------+----------+                        |               |
	 * |              /|\                                  |               |
	 * |               |                                   |               |
	 * |   +-----------+-----------+
	 * |   {@link org.apache.flink.runtime.io.network.netty.NettyMessage.NettyMessageDecoder}
	 * |   | Message+Frame decoder |                       |               |
	 * |   +-----------+-----------+                       |               |
	 * |              /|\                                  |               |
	 * +---------------+-----------------------------------+---------------+
	 * |               | (1) client request               \|/
	 * +---------------+-----------------------------------+---------------+
	 * |               |                                   |               |
	 * |       [ Socket.read() ]                    [ Socket.write() ]     |
	 * |                                                                   |
	 * |  Netty Internal I/O Threads (Transport Implementation)            |
	 * +-------------------------------------------------------------------+
	 * </pre>
	 *
	 * @return channel handlers
	 *
	 *
	 *
	 *
	 */
	public static void testMethod1(){

	}

	/**
	 * 客户端处理
	 * Returns the client channel handlers.
	 *
	 * <pre>
	 *     +-----------+----------+            +----------------------+
	 *     | Remote input channel |            | request client       |
	 *     +-----------+----------+            +-----------+----------+
	 *                 |                                   | (1) write
	 * +---------------+-----------------------------------+---------------+
	 * |               |     CLIENT CHANNEL PIPELINE       |               |
	 * |               |                                  \|/              |
	 * |    +----------+----------+            +----------------------+    |
	 * |    | Request handler     +            | Message encoder      |    |
	 * |    +----------+----------+            +-----------+----------+    |
	 * |              /|\                                 \|/              |
	 * |               |                                   |               |
	 * |    +----------+------------+
	 * {@link org.apache.flink.runtime.io.network.netty.NettyMessage.NettyMessageDecoder}
	 * |    | Message+Frame decoder |                      |               |
	 * |    +----------+------------+                      |               |
	 * |              /|\                                  |               |
	 * +---------------+-----------------------------------+---------------+
	 * |               | (3) server response              \|/ (2) client request
	 * +---------------+-----------------------------------+---------------+
	 * |               |                                   |               |
	 * |       [ Socket.read() ]                    [ Socket.write() ]     |
	 * |                                                                   |
	 * |  Netty Internal I/O Threads (Transport Implementation)            |
	 * +-------------------------------------------------------------------+
	 * </pre>
	 *
	 * @return channel handlers
	 */
	public static void testMethod2(){

	}
}
