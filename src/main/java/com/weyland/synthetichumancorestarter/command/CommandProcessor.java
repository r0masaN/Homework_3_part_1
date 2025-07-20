package com.weyland.synthetichumancorestarter.command;

import com.weyland.synthetichumancorestarter.exception.UnknownCommandPriorityException;
import com.weyland.synthetichumancorestarter.metrics.CommandMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CommandProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(CommandProcessor.class);

    private final CommandMetrics metrics;
    private final CommandQueueManager manager;

    public CommandProcessor(final CommandMetrics metrics, final CommandQueueManager manager) {
        this.metrics = metrics;
        this.manager = manager;
    }

    public void submit(final CommandRequest commandRequest) {
        switch (commandRequest.priority()) {
            case CRITICAL -> this.execute(commandRequest);
            case COMMON -> this.manager.pushCommandRequest(commandRequest);
            default -> throw new UnknownCommandPriorityException(commandRequest.priority().name());
        }
    }

    private void execute(final CommandRequest commandRequest) {
       LOG.info("Executing CRITICAL command: {}", commandRequest);

       this.metrics.incrementAuthor(commandRequest.author());
    }
}
