## Resource Scheduler ##

### Contract with external resources  ###
After analysing the supplied interface our assumptions are the following:

- the process is **asynchronous** and the - `send(Message msg)` method immediately returns to the caller without waiting for the method execution to complete
- the `completed()` callback method is invoked after the very expensive, external resource finish to process the message

### Forwarding ###

a). For a single resource, when one message is received, that message is sent to the gateway

- created the **ForwardingTest** test case to test this scenario
- added the **ConcreteMessage** concrete implementation of Message interface
- created the **ResourceScheduler** functional class - the **Gateway** was added as constructor parameter because it is a mandatory resources
- to test this functionality a **SpyGateway** was created and the test verifies that the `send(Message msg)` is invoked with the right parameter

b). For two resources, when two messages are received, both messages are sent to the gateway.

Before starting to implement this behavior we should take in account the following:

- we need a way to check if a resource is idle or not 
- the **ResourceScheduler** should act as a router sending the received message to the next idle resource
- messages should be processed in parallel (multi-threading on a multi-core machine)

The resource state transition table is depicted below:

| Current state | Method          | Next state   |
| ------------- | -------------   | -------------|
|Idle           | *send *         | Busy         |
|Idle           | *not send*      | Idle         |
|Busy           | *completed*     | Idle         |
|Busy           | *not completed* | Busy         |

To implement this behavior the following decision were made:

- Modified the **ResourceScheduler** to receive the number of resources available. Also we choose to delegate the responsibility of **Gateway's** creation to a different class / classes that implements the **GatewayFactory** interface.
- Wrapped the **Gateway** in a **Resource** and added support for idle detection.
- Added a thread pool on the **ResourceScheduler** and modified the processing method accordingly.

> **Important**
> 
> After adding multi-threading support some of the tests failed. This is because the assertions were made in the JUnit thread and messages are sent on different threads. We've applied some asynchronous testing techniques to fix the tests:
> 
> 1. Added **SyncRetryPolicy** class. This is configured at construction time with two parameters: number of retries and sleep time between retries. If total time is exceed and the expectation is not fulfilled the test is marked as failed.
> 2. Added **Task** interface - this is implemented in each test and it is an abstraction of test expectation. 

### Queuing ###

For a single resource, when two messages are received, only the first message is sent to the gateway

- To implement this behavior a queue was added to the **ResourceScheduler**. The message is first put on the queue and after that the **ResourceScheduler** check if they are **Resource** available to process the first message from the queue.

### Responding ###

As messages are completed, if there are queued messages, they should be processed.

- We choose to implement this functionality int the `completed()` callback method that is invoked after the external resource finish to process the message. The **Resource** will try to get the next unprocessed message from the queue and to send it to the **Gateway**. Because the queue is now a shared resource (with **ResourceScheduler** as producer and multiple **Resources** as consumers) we've chosen to encapsulate it in its own class.
- We added the **AsyncSpyGateway** fake object to test this more complicated asynchronous scenario. This class simulate a real processing resource. It sleep for a specified amount of time (the message payload set on the test arrange phase) and after that, call the message `completed()` callback method allowing us to write component integration tests.

### Prioritising ###

If there are messages belonging to multiple groups in the queue, as resources become available, we want to prioritise messages from groups
already started.

- Modified **MessagesQueue** and added filter support for `dequeue()` method.
- Modified **Resource** to use the group id of previous processed message and to use it for obtaining the next message to process.


