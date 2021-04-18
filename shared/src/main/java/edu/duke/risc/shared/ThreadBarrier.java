package edu.duke.risc.shared;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Used to control the game flow
 *
 * @author eason
 * @date 2021/3/10 11:34
 */
public class ThreadBarrier implements Serializable {

    private Object barrierLock;

    private AtomicInteger threadCounter;

    /**
     * cache, reader will block here until there are payloads in
     */
    private BlockingQueue<PayloadObject> cache;

    public ThreadBarrier(int maxPlayer) {
        threadCounter = new AtomicInteger(maxPlayer);
        cache = new ArrayBlockingQueue<>(20);
        barrierLock = new Object();
    }

    /**
     * Consumer the request from clients
     * @return PayloadObject
     */
    public PayloadObject consumeRequest() {
        try {
            return cache.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Put the object received into the cache
     * @param payloadObject payloadObject
     */
    public void objectReceived(PayloadObject payloadObject) {
        cache.add(payloadObject);
    }

}
