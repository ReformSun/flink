package com.test.graph;

import org.apache.flink.runtime.jobgraph.DistributionPattern;

/**
 * This class represent edges (communication channels) in a job graph.
 * The edges always go from an intermediate result partition to a job vertex.
 * An edge is parametrized with its {@link DistributionPattern}.
 */
public class TestJobEdge {
}
