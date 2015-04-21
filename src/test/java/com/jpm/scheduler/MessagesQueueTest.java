package com.jpm.scheduler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessagesQueueTest {

    private MessagesQueue sut;

    @Before
    public void setUp() {
        sut = new MessagesQueue();
    }

    @Test
    public void shouldReturnNullIfIsEmpty() {
        Assert.assertNull("Return null if empty", sut.dequeue());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfMessageIsNull() {
        sut.enqueue(null);
    }

    @Test
    public void sholdReturnFirstElementIfNotEmpty() {
        ConcreteMessage first = new ConcreteMessage("groupId1", "first");
        ConcreteMessage second = new ConcreteMessage("groupId2", "second");

        sut.enqueue(first);
        sut.enqueue(second);

        Assert.assertEquals("Should return first element if not empty", first,
                sut.dequeue());
    }
}
