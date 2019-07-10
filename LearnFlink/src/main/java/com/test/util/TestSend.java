package com.test.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;

public class TestSend {
	private static FlinkJobManager flinkJobManager = FlinkJobManagerImp.getInstance();
	private static JsonParser jsonParser = new JsonParser();
    public static void main(String[] args) {
    	String jobid = "5eee8dc25976674d5a4adaa370b6dad7";

		/**
		 * 提交job
		 */
//		testMethod1();
        testMethod2();
		/**
		 * 触发检查点
		 */
//		testMethod3(jobid);

//		testMethod4();
		/**
		 * 获取检查点状态
		 */
//		testMethod5(jobid,"");

//		testMethod6(jobid);
		/**
		 * 获取测量值
		 */
//		testMethod7();
//		testMethod8();
    }




	public static void testMethod1(){
        ReadResult readResultA = flinkJobManager.uploadJob("/Users/apple/Documents/AgentJava/flink-master/LearnFlink/target/test.jar","test.jar");
        System.out.println(readResultA.getResponseBody());
    }

    public static void testMethod2(){
        String jobname = "aa507724-f700-4a8e-838b-9a5a3e4d7730_test.jar";
        String main_class = "com.test.learnState.TestMain1";
        try {
            ReadResult readResult = flinkJobManager.runJob(jobname,main_class,"",10);
            System.out.println(readResult.getResponseBody());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

	/**
	 * 触发检查点
	 */
	public static void testMethod3(String jobid){
		ReadResult readResult = flinkJobManager.triggerSavepoints(jobid);
		System.out.println(readResult.getResponseBody());
	}

	/**
	 * 得到检查点执行的状态
	 */
	public static void testMethod5(String jobId,String triggerId) {
		ReadResult readResult =flinkJobManager.getSavepointStatus("45bf3d6455efa81862b5eb97aa46b6c3","96111c27babd2a911888cff9bd74b825");
		System.out.println(readResult.getResponseBody());

	}

	/**
	 * 测试安全点并查看状态
	 */
	public static void testMethod6(String jobid) {
		ReadResult readResult = flinkJobManager.triggerSavepoints(jobid);
		if (readResult.getResponseCode() == 202){
			JsonElement jsonElement = jsonParser.parse(readResult.getResponseBody());
			String triggerId = jsonElement.getAsJsonObject().get("request-id").getAsString();
			if (triggerId != null){
				ReadResult readResult2 =flinkJobManager.getSavepointStatus(jobid,triggerId);
				System.out.println(readResult2.getResponseBody());
			}
		}
	}

	/**
	 * 得到job详情
	 */
	public static void testMethod4(){
		ReadResult readResult =flinkJobManager.getJobsDetail("d706815e316b7e34104a95421e3bb351");
	}

	/**
	 * 获取job的所有测量值
	 */
	public static void testMethod7(){
		ReadResult readResult =flinkJobManager.getJobMetrics();
	}

	/**
	 * 得到overview
	 */
	public static void testMethod8(){
		ReadResult readResult =flinkJobManager.getJobsOverview();
		System.out.println(readResult.toJsonString());
	}


}
