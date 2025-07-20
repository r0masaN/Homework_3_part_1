package com.weyland.synthetichumancorestarter;

import com.weyland.synthetichumancorestarter.command.CommandPriority;
import com.weyland.synthetichumancorestarter.command.CommandQueueManager;
import com.weyland.synthetichumancorestarter.command.CommandRequest;
import com.weyland.synthetichumancorestarter.exception.CommandQueueFullException;
import com.weyland.synthetichumancorestarter.metrics.CommandMetrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public final class CommandQueueManagerTest {
    @Test
    public void shouldUpdateMetricsAndExecute() {
        final CommandMetrics metrics = mock(CommandMetrics.class);
        final CommandQueueManager manager = new CommandQueueManager(metrics);
        final CommandRequest request = new CommandRequest(
                "Random command request",
                CommandPriority.COMMON,
                "Roman",
                "2025-07-20T16:30:05"
        );

        manager.pushCommandRequest(request);

        verify(metrics, times(1)).incrementAuthor("Roman");
        verify(metrics, times(1)).setQueueSize(1);
    }

    @Test
    public void shouldThrowIfQueueIsFull() {
        final CommandMetrics metrics = mock(CommandMetrics.class);
        final CommandQueueManager manager = new CommandQueueManager(metrics);

        assertThrows(CommandQueueFullException.class, () -> {
            // принудительно заполняем очередь
            for (int i = 0; i < 100; i++) {
                manager.pushCommandRequest(new CommandRequest(
                        "Random command request",
                        CommandPriority.COMMON,
                        "Roman",
                        "2025-07-20T16:30:05"
                ));
            }
        });
    }
}
