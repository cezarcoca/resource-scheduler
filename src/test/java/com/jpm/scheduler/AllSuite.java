package com.jpm.scheduler;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CancellationTest.class, ForwardingTest.class,
        GroupingTest.class, MessagesQueueTest.class, PrioritiesTest.class,
        QueuingTest.class, RespondingTest.class })
public class AllSuite {

}
