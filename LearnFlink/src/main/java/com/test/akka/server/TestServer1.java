package com.test.akka.server;

import com.test.akka.jobmaster.JobMasterGatewayNoFencedImp;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.ConfigurationUtils;
import org.apache.flink.runtime.rpc.RpcService;
import org.apache.flink.runtime.rpc.akka.AkkaRpcServiceUtils;

import java.util.Properties;

public class TestServer1 {
	public static void main(String[] args) {
		Properties propertie = new Properties();
		propertie.setProperty("akka.actor.provider","akka.remote.RemoteActorRefProvider");
//		propertie.setProperty("akka.remote.netty.tcp.hostname", "127.0.0.1");
//		propertie.setProperty("akka.remote.netty.tcp.port", "8082");
		Configuration configuration = ConfigurationUtils.createConfiguration(propertie);
		testMethod1(configuration);

	}

	/**
	 * 创建rpc服务
	 */
	public static void testMethod1(Configuration configuration){
		try {
			RpcService rpcService = AkkaRpcServiceUtils.createRpcService("127.0.0.1",8082,configuration);
			// 创建一个rpc服务
			JobMasterGatewayNoFencedImp testRpcEndpoint = new JobMasterGatewayNoFencedImp(rpcService,"jobmaster");
			// 开始一个rpc服务
			testRpcEndpoint.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
