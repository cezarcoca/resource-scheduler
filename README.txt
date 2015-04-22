## Readme ##

The aim of this document is to provide an overview of decisions made during the implementation phase. The assumptions and decisions made during this process are presented in chronological order. A considerable amount of useful information can be derived from the GIT commits logs also. 

This README file contains as well the basic setup instructions and is saved in two formats:

- a plain text file 
- the markdown version that is rendered by GitHub viewer as valid HTML

## Installation ##

All the source and project files (Eclipse) required to build and run the solution are committed on GitHub.

**How to build and run the tests**

Clone a copy of the solution GIT repository by running:

    git clone https://github.com/cezarcoca/resource-scheduler.git

Enter the resource-scheduler directory:

    cd resource-scheduler

Run the build script:

    mvn clean package


## Resource Scheduler - implementation notes ##

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
|Idle           | *send*          | Busy         |
|Idle           | *not send*      | Idle         |
|Busy           | *completed*     | Idle         |
|Busy           | *not completed* | Busy         |

![Software Life Cycle](https://4d7f3ccfc784b13605de2780f0f4bf21ee2162f6.googledrive.com/host/0B9tMA3RbZ5P_VS11Nk0yWkpNeXM/resource.png)

To implement this behavior the following decisions were made:

- Modified the **ResourceScheduler** to receive the number of resources available. Also we choose to delegate the responsibility of **Gateway's** creation to a different class / classes that implements the **GatewayFactory** interface.
- Wrapped the **Gateway** in a **Resource** and added support for idle detection.
- Added a thread pool on the **ResourceScheduler** and modified the processing method accordingly.

> **Note**
> 
> After adding multi-threading support some of the tests failed. This is because the assertions were made in the JUnit thread and messages are sent on different threads. We've applied some asynchronous testing techniques to fix the tests:
> 
> 1. Added **SyncRetryPolicy** class. This is configured at construction time with two parameters: ***number of retries*** and ***sleep time between retries***. If total time is exceeded and the expectation is not fulfilled the test is marked as failed.
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

- Modified **MessagesQueue** and added filter support for `dequeue(String filter)` method.
- Modified **Resource** to use the group id of previous processed message and to use it for obtaining the next message to process.

## Extra credit ##

### Cancellation ###

It should be possible to tell the scheduler that a group of messages has now been cancelled. Once cancelled, no further messages from that
group should sent to the Gateway.

- We enhanced the **MessagesQueue** API and added the `addCancelledGroup(String groupId)` method. The cancelled groups are stored in a internal **Set** and when the `dequeue(String filter)` method is invoked, we check next matching message against this blacklist and skip and remove it from the queue if is the case.
- We noticed that we need to remove messages from the middle of the **MessagesQueue** for both filtering and canceling operations, so we choose to replace the internal **List** implementation from **ArrayList** to **LinkedList** to improve the performance of those operations.

> **Note**
> 
> An interesting observation here is that we need to be able to add ***cancelled groups*** at runtime without application restart and without knowing in advance the groups names. The standard **Java Management Extension** (JMX) technology could be a good solution to achieve this goal. A **Cancellation MBean** could be implemented and registered using the **JMX** technology enabling the operations team to manage at runtime the cancelled message list.

### Alternative Message Prioritisation ###

It should be possible to use different Message prioritisation algorithms to select the next Message from the queue.

The **Strategy Pattern** seems to fit well in this scenario.

- We added a the new **PrioritisationFilter** interface to define the contract.
- We moved the current implementation into the **PrioritisationByGroupName** concrete class and run the tests to ensure that the current behavior is preserved.
- We added a new strategy. This strategy prioritize the messages by group name prefix. In this hypothetical scenario, by convention, each group name is composed by concatenating the **prefix**, **#** character and the **group number**.
- We added a new private factory method on **MessagesQueue**. This method try to get the value of `com.jpm.scheduler.prioritisation.strategy` environment variable and using this value the below depicted algorithm is applied.

Algorithm

1. if ***value is not*** found the default **PrioritisationByGroupName** is used
2. if ***value is found***, the specified strategy is instantiated by reflection
3. if ***an error is thrown***, a warning message is logged and the default **PrioritisationByGroupName** is used
 

 


