package edu.duke.risc.shared;

import edu.duke.risc.shared.PayloadObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author eason
 * @date 2021/3/10 11:34
 */
public class ThreadBarrier {

    private Object barrierLock;

    private AtomicInteger threadCounter;

    private BlockingQueue<PayloadObject> cache;

    public ThreadBarrier(int maxPlayer) {
        threadCounter = new AtomicInteger(maxPlayer);
        cache = new ArrayBlockingQueue<>(20);
        barrierLock = new Object();
    }

    public void waitAllCommits() throws InterruptedException {
        barrierLock.wait();
    }

    public void objectReceived(PayloadObject payloadObject) {
        cache.add(payloadObject);
        //todo delete this
        threadCounter.decrementAndGet();
        if (threadCounter.get() == 0) {
            barrierLock.notifyAll();
        }
    }

}
