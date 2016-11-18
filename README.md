The latest stable version is [1.6.7](https://github.com/bee/bee-java/tree/connector-1.6.7)

# Java Connector for Bee 1.7.0-SNAPSHOT

[![Join the chat at https://gitter.im/bee/bee-java](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/bee/bee-java?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Problems & Questions
http://stackoverflow.com/questions/ask/advice with tags `bee` and `java`.

## Note
Bee client is not supports name resolving for fields, indexes, space etc. I highly recommend to use server side lua to
operate with named items. For example you could create dao object with simple CRUD functions.
If you still need client name resolving for some reasons you could create function which will return required maps with name to id mappings.

## How to start

First you should add dependency to your pom file
```xml
<dependency>
  <groupId>org.bee</groupId>
  <artifactId>connector</artifactId>
  <version>1.7.0-SNAPSHOT</version>
</dependency>
```
First you should configure BeeClientConfig.

```java
     BeeClientConfig config = new BeeClientConfig();
     config.username = "test";
     config.password = "test";
```

Then implements your SocketChannelProvider. SocketChannelProvider should return connected SocketChannel.
Here you also could implement some reconnect or fallback policy. Remember that BeeClient uses [fail fast
policy](https://en.wikipedia.org/wiki/Fail-fast) when client is not connected.


```java
     SocketChannelProvider socketChannelProvider = new SocketChannelProvider() {
                @Override
                public SocketChannel get(int retryNumber, Throwable lastError) {
                    if (lastError != null) {
                        lastError.printStackTrace(System.out);
                    }
                    try {
                        return SocketChannel.open(new InetSocketAddress("localhost", 3301));
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
            };
```

Now you are ready to create client
```java
BeeClient client = new BeeClientImpl(socketChannelProvider, config);
```

BeeClient is thread safe and async so you should use one client inside whole application. 

BeeClient provides 3 interfaces to execute queries

* SyncOps returns operation result
* AsyncOps returns operation result Future
* FireAndForgetOps returns query ID


Feel free to override any method of BeeClientImpl. For example you
could override 
```java
protected void complete(long code, FutureImpl<List> q);
```
to hook all results.




