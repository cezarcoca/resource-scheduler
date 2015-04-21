## Resource Scheduler ##

### Contract with external resources  ###
After analysing the supplied interface our assumptions are the following:

- the process is **asynchronous** and the - `send(Message msg)` method immediately returns to the caller without waiting for the method execution to complete
- the `completed()` callback method is after the very expensive, external resource finish to process the message

