/**
 * Copyright (c) 2004-2016 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package org.slf4j.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Handler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerAccessingThread;
import org.slf4j.LoggerFactory;

public class JDK14MultithreadedInitializationTest {

    final static int THREAD_COUNT = 4 + Runtime.getRuntime().availableProcessors() * 2;

    final private AtomicLong eventCount = new AtomicLong(0);
    final private CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT + 1);

    int diff = new Random().nextInt(10000);

    java.util.logging.Logger julRootLogger = java.util.logging.Logger.getLogger("");
    java.util.logging.Logger julOrgLogger = java.util.logging.Logger.getLogger("org");

    @Before
    public void addRecordingHandler() {
        System.out.println("THREAD_COUNT=" + THREAD_COUNT);
        removeAllHandlers(julRootLogger);
        removeAllHandlers(julOrgLogger);
        julOrgLogger.addHandler(new CountingHandler());
    }

    private void removeAllHandlers(java.util.logging.Logger logger) {
        Handler[] handlers = logger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            logger.removeHandler(handlers[i]);
        }
    }

    @After
    public void tearDown() throws Exception {
        removeAllHandlers(julOrgLogger);
    }

    @Test
    public void multiThreadedInitialization() throws InterruptedException, BrokenBarrierException {
        @SuppressWarnings("unused")
        LoggerAccessingThread[] accessors = harness();

        Logger logger = LoggerFactory.getLogger(getClass().getName());
        logger.info("hello");
        eventCount.getAndIncrement();

        long recordedEventCount = getRecordedEventCount();
        assertTrue(eventCount.get() + " >= " + recordedEventCount, eventCount.get() >= recordedEventCount);
        assertTrue(eventCount.get() + " < " + recordedEventCount + "+10", eventCount.get() < recordedEventCount + 10);
    }

    private long getRecordedEventCount() {
        CountingHandler ra = findRecordingHandler();
        if (ra == null) {
            fail("failed to fing RecordingHandler");
        }
        return ra.eventCount.get();
    }

    private CountingHandler findRecordingHandler() {
        Handler[] handlers = julOrgLogger.getHandlers();
        for (Handler h : handlers) {
            if (h instanceof CountingHandler)
                return (CountingHandler) h;
        }
        return null;
    }

    private LoggerAccessingThread[] harness() throws InterruptedException, BrokenBarrierException {
        LoggerAccessingThread[] threads = new LoggerAccessingThread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new LoggerAccessingThread(barrier, i, eventCount);
            threads[i].start();
        }

        // trigger barrier
        barrier.await();

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }

        return threads;
    }

}
