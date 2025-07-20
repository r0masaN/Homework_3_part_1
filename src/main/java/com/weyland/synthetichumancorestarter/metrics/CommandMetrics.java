package com.weyland.synthetichumancorestarter.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public final class CommandMetrics {
    private final MeterRegistry meterRegistry;
    private final AtomicInteger queueSize;
    private final ConcurrentHashMap<String, AtomicInteger> authorsCounter = new ConcurrentHashMap<>();

    @Autowired
    public CommandMetrics(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.queueSize = this.meterRegistry.gauge("commands.queue.size", new AtomicInteger(0));
    }

    public void setQueueSize(final int size) {
        this.queueSize.set(size);
    }

    public void incrementAuthor(final String author) {
        this.authorsCounter.computeIfAbsent(author, currAuthor ->
                this.meterRegistry.gauge("commands.executed.count", Tags.of("author", currAuthor), new AtomicInteger(0))
        ).incrementAndGet();
    }
}
