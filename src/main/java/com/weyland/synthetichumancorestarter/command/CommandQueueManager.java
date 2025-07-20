package com.weyland.synthetichumancorestarter.command;

import com.weyland.synthetichumancorestarter.exception.CommandQueueFullException;
import com.weyland.synthetichumancorestarter.metrics.CommandMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class CommandQueueManager {
    private static final Logger LOG = LoggerFactory.getLogger(CommandProcessor.class);
    private static final int MAX_QUEUE_LENGTH = 16;

    private final CommandMetrics metrics;
    private final ThreadPoolExecutor executor;

    {
        this.executor = new ThreadPoolExecutor(
                1,
                MAX_QUEUE_LENGTH,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(MAX_QUEUE_LENGTH),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public CommandQueueManager(final CommandMetrics metrics) {
        this.metrics = metrics;
    }

    public void pushCommandRequest(final CommandRequest commandRequest) {
        try {
            this.metrics.setQueueSize(this.getQueueSize() + 1);

            this.executor.submit(() -> executeCommand(commandRequest));

            this.metrics.incrementAuthor(commandRequest.author());
        } catch (final RejectedExecutionException e) {
            throw new CommandQueueFullException("Command queue is full!");
        }
    }

    private void executeCommand(final CommandRequest commandRequest) {
        LOG.info("Executing COMMON command: {}", commandRequest);
    }

    public int getQueueSize() {
        return this.executor.getQueue().size();
    }
}
