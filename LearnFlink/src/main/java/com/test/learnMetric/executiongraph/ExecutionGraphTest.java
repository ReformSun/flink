package com.test.learnMetric.executiongraph;

import org.apache.flink.runtime.jobgraph.JobStatus;

public class ExecutionGraphTest {
	JobStatus jobStatus;
	private final long[] stateTimestamps;

	public ExecutionGraphTest(JobStatus jobStatus, long[] stateTimestamps) {
		this.jobStatus = jobStatus;
		this.stateTimestamps = stateTimestamps;
	}

	public JobStatus getState() {
		return jobStatus;
	}

	public long getStatusTimestamp(JobStatus status) {
		return this.stateTimestamps[status.ordinal()];
	}

	public long getNumberOfFullRestarts() {
		// subtract one, because the version starts at one
		return  1;
	}

}
