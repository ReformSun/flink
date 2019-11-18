package com.test.graph;

import org.apache.flink.runtime.execution.Environment;
import org.apache.flink.runtime.jobgraph.tasks.AbstractInvokable;

public class TestClass extends AbstractInvokable {
	/**
	 * Create an Invokable task and set its environment.
	 *
	 * @param environment The environment assigned to this invokable.
	 */
	public TestClass(Environment environment) {
		super(environment);
	}

	public void testMethod1(){
		System.out.println("cc");
	}

	@Override
	public void invoke() throws Exception {
		testMethod1();
	}
}
