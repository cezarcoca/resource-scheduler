package com.jpm.scheduler;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jpm.scheduler.prioritisation.PrioritisationByGroupNamePrefix;

public class MessagesQueueTest {

    private MessagesQueue sut;

    @Before
    public void setUp() {
        sut = new MessagesQueue();
    }

    @After
    public void tearDown() {
        System.setProperty(MessagesQueue.PRIORITISATION_STRATEGY, "");
    }

    @Test
    public void shouldReturnNullIfIsEmpty() {
        Assert.assertNull("Return null if empty", sut.dequeue(null));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfMessageIsNull() {
        sut.enqueue(null);
    }

    @Test
    public void sholdReturnFirstElementIfNotElementMatchTheFilter() {
        ConcreteMessage first = new ConcreteMessage("groupId1", "first");
        ConcreteMessage second = new ConcreteMessage("groupId2", "second");

        sut.enqueue(first);
        sut.enqueue(second);

        String filter = "groupId3";
        Assert.assertEquals(
                "Should return first element if no element match the filter",
                first, sut.dequeue(filter));
    }

    @Test
    public void shouldReturnFirstMatchingElementIfExists() {

        String filter = "groupId2";

        ConcreteMessage first = new ConcreteMessage("groupId1", "first");
        ConcreteMessage second = new ConcreteMessage(filter, "second");
        ConcreteMessage third = new ConcreteMessage("groupId3", "third");

        sut.enqueue(first);
        sut.enqueue(second);
        sut.enqueue(third);

        Assert.assertEquals("Should return first matching element", second,
                sut.dequeue(filter));
    }

    @Test
    public void shouldUsePrioritisationByGroupNamePrefixStrategyIfSet() {

        System.setProperty(MessagesQueue.PRIORITISATION_STRATEGY,
                PrioritisationByGroupNamePrefix.class.getCanonicalName());

        sut = new MessagesQueue();

        String filter = "b#1";

        ConcreteMessage first = new ConcreteMessage("a#1", "first");
        ConcreteMessage second = new ConcreteMessage("b#2", "second");

        sut.enqueue(first);
        sut.enqueue(second);

        Assert.assertEquals("Should return second element", second,
                sut.dequeue(filter));

    }
}
