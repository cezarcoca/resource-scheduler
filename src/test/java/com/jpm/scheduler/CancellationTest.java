package com.jpm.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class CancellationTest {

    private MessagesQueue sut;

    @Before
    public void setUp() {
        sut = new MessagesQueue();
    }

    @Test
    public void shouldReturnNullIfAllMessagesAreCancelled() {

        String cancelledGroup = "cancelledGroupId";
        ConcreteMessage first = new ConcreteMessage(cancelledGroup, "first");
        ConcreteMessage second = new ConcreteMessage(cancelledGroup, "second");

        sut.addCancelledGroup(cancelledGroup);

        sut.enqueue(first);
        sut.enqueue(second);

        assertNull("All messages are cancelled", sut.dequeue(null));
        assertEquals("All messages are removed from queue", 0, sut.size());
    }

    @Test
    public void shouldSkipCancelledMessagesEvenIfCancelledGroupIsFilter() {

        String cancelledGroup = "cancelledGroupId";
        ConcreteMessage first = new ConcreteMessage(cancelledGroup, "first");
        ConcreteMessage second = new ConcreteMessage("uncancelledGroup",
                "second");

        sut.addCancelledGroup(cancelledGroup);

        sut.enqueue(first);
        sut.enqueue(second);

        assertEquals("All messages are cancelled", second,
                sut.dequeue(cancelledGroup));
    }

    @Test
    public void shouldNotBreakFiltering() {

        String cancelledGroup = "cancelledGroupId";
        String filter = "uncancelledGroup";
        ConcreteMessage first = new ConcreteMessage(cancelledGroup, "first");
        ConcreteMessage second = new ConcreteMessage(filter, "second");

        sut.addCancelledGroup(cancelledGroup);

        sut.enqueue(first);
        sut.enqueue(second);

        assertEquals("All messages are cancelled", second, sut.dequeue(filter));
    }
}
