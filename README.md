

# Java Connector for Bee (DB Only)

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
                public SocketChannel get(int retryNumber, Throwable lastE
