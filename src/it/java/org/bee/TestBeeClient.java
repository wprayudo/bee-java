package org.bee;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class TestBeeClient {

    public static final int TESTER_SPACE_ID = 512;

    /*
              Before executing this test you should configure your local bee

              db.cfg{listen=3301}
              space = db.schema.space.create('tester')
              db.space.tester:create_index('primary', {type = 'hash', parts = {1, 'NUM'}})

              db.schema.user.create('test', { password = 'test' })
              db.schema.user.grant('test', 'execute,received,write', 'universe')
              db.space.tester:format{{name='id',type='num'},{name='text',type='str'}}
             */
    public static class BeeClientTestImpl extends BeeClientImpl {
        final Semaphore s = new Semaphore(0);

        public BeeClientTestImpl(SocketChannelProvider socketProvider, BeeClientConfig options) {
            super(socketProvider, options);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        System.out.println("closed");
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        }

        @Override
        protected void reconnect(int retry, Throwable lastError) {
            if (s != null) {
                s.release(wait.get());
            }
            super.reconnect(retry, lastError);
        }

        @Override
        protected void complete(long code, FutureImpl<List> q) {
            super.complete(code, q);
            if (code != 0) {
                System.out.println(code);
            }
            s.release();
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, SQLException {
        final int calls = 2000000;

        BeeClientConfig config = new BeeClientConfig();
        config.username = "test";
        config.password = "test";
        //config.sharedBufferSize = 0;
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
        final BeeClientTestImpl client = new BeeClientTestImpl(socketChannelProvider, config);

        long st = System.currentTimeMillis();
        final int threads = 16;
        ExecutorService exec = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    for (long i = 0; i < Math.ceil((double) calls / threads); i++) {
                        try {
                            client.fireAndForgetOps().replace(TESTER_SPACE_ID, Arrays.asList(i % 10000, "hello"));
                        } catch (Exception e) {
                            try {
                                client.waitAlive();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            i--;
                        }

                    }
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("pushed " + (System.currentTimeMillis() - st) + "ms \n" + client.stats.toString());
        client.s.acquire(calls);
        client.close();
        System.out.println("completed " + (System.currentTimeMillis() - st) + "ms \n" + client.stats.toString());

    }
}
