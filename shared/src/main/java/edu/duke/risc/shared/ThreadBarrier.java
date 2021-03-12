package edu.duke.risc.shared;

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

    public PayloadObject consumeRequest() {
        try {
            return cache.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void objectReceived(PayloadObject payloadObject) {
        cache.add(payloadObject);
    }

}
