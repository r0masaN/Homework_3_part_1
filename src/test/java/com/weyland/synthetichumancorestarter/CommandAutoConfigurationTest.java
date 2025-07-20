package com.weyland.synthetichumancorestarter;

import com.weyland.synthetichumancorestarter.command.CommandProcessor;
import com.weyland.synthetichumancorestarter.command.CommandQueueManager;
import com.weyland.synthetichumancorestarter.config.CommandAutoConfiguration;
import com.weyland.synthetichumancorestarter.metrics.CommandMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public final class CommandAutoConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(CommandAutoConfiguration.class)
            .withBean(SimpleMeterRegistry.class);

    @Test
    public void shouldLoadConfigurationAndCreateBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(CommandProcessor.class);
            assertThat(context).hasSingleBean(CommandQueueManager.class);
            assertThat(context).hasSingleBean(CommandMetrics.class);
        });
    }
}
