package com.test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 断言和注解在代码中只是起到了一种协议的条款指定的作用
 * 指定方法在参数不能为空或者可能为空，要根据断言或者注解去调用此方法
 * 这样才能保证代码的正确运行
 * 注解和断言在代码执行中不会起到影响代码运行的作用
 */
public class TestAssert {
	public static void main(String[] args) {
		int i = 11;
		assert(i != 10);
		System.out.println("aa");
		TestAssert testAssert = new TestAssert();
		testAssert.testMethod1("cc",null);
		testAssert.testMethod2(null);
	}


	@Nullable
	public void testMethod1(@Nullable String s,@Nullable String b){
	    if (s == null){
			System.out.println("为空");
		}else {
	    	assert(b != null);
			System.out.println("d不为空");
		}
	}
	@Nonnull
	public void testMethod2(@Nonnull String s){

	}
}
