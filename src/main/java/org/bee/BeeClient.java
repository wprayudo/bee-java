package org.bee;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface BeeClient {
    BeeClientOps<Integer, Object, Object, List> syncOps();

    BeeClientOps<Integer, Object, Object, Future<List>> asyncOps();

    BeeClientOps<Integer, Object, Object, Long> fireAndForgetOps();

    void close();

    boolean isAlive();

    void waitAlive() throws InterruptedException;

    void waitAlive(long timeout, TimeUnit unit) throws InterruptedException;

}
