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
- messages should be processed in parallel (multithreading on a multi-core machine)

The resource state transition table is depicted below:

| Current state | Method        | Nest state   |
| ------------- | ------------- | -------------|
|Idle           | send          | Busy         |
|Idle           | not send      | Idle         |
|Busy           | completed     | Idle         |
|Busy           | not completed | Busy         |

To implement this behavior the following decision were made:
- Modified the **ResourceScheduler** to receive the number of resources available. Also we choose delegate the responsibility of **Gateway's** creation to a different class / classes that implements the **GatewayFactory** interface.
- Wrapped the **Gateway** in a **Resource** and added support for idle detection.