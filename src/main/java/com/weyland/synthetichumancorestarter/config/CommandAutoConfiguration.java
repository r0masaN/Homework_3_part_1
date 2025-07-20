package com.weyland.synthetichumancorestarter.config;

import com.weyland.synthetichumancorestarter.command.CommandProcessor;
import com.weyland.synthetichumancorestarter.command.CommandQueueManager;
import com.weyland.synthetichumancorestarter.metrics.CommandMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandAutoConfiguration {
    @Bean
    public CommandMetrics commandMetrics(final MeterRegistry meterRegistry) {
        return new CommandMetrics(meterRegistry);
    }

    @Bean
    public CommandQueueManager commandQueueManager(final CommandMetrics commandMetrics) {
        return new CommandQueueManager(commandMetrics);
    }

    @Bean
    public CommandProcessor commandProcessor(final CommandMetrics commandMetrics, final CommandQueueManager commandQueueManager) {
        return new CommandProcessor(commandMetrics, commandQueueManager);
    }
}
