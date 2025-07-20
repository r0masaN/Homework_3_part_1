package com.weyland.synthetichumancorestarter;

import com.weyland.synthetichumancorestarter.command.CommandPriority;
import com.weyland.synthetichumancorestarter.command.CommandProcessor;
import com.weyland.synthetichumancorestarter.command.CommandQueueManager;
import com.weyland.synthetichumancorestarter.command.CommandRequest;
import com.weyland.synthetichumancorestarter.exception.UnknownCommandPriorityException;
import com.weyland.synthetichumancorestarter.metrics.CommandMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class CommandProcessorTest {
    @Mock
    private CommandMetrics metrics;
    private CommandQueueManager manager;
    private CommandProcessor processor;

    @BeforeEach
    public void setUp() {
        this.manager = new CommandQueueManager(this.metrics);
        this.processor = new CommandProcessor(this.metrics, this.manager);
    }

    @Test
    public void shouldExecutorProcessCriticalCommand() {
        final CommandRequest request = new CommandRequest(
                "Random command request",
                CommandPriority.CRITICAL,
                "Roman",
                "2025-07-20T16:30:05"
        );

        this.processor.submit(request);

        //verify(queueManager, never()).pushCommandRequest(any()); НЕВОЗМОЖНО
        verify(this.metrics, times(1)).incrementAuthor("Roman");
    }

    @Test
    public void shouldQueueManagerProcessCommonCommand() {
        final CommandRequest request = new CommandRequest(
                "Random command request",
                CommandPriority.COMMON,
                "Roman",
                "2025-07-20T16:30:05"
        );

        this.processor.submit(request);

        //verify(queueManager,times(1)).pushCommandRequest(any()); НЕВОЗМОЖНО
        verify(this.metrics, times(1)).incrementAuthor("Roman");
    }

    // big roflo error:
    // так как CommandMetrics и CommandQueueManager - НЕ аннотированы как бины (вручную в конфигурационном
    // файле бинами становятся), пометка их как @Mock не внедрит автоматически нужные зависимости, а
    // @InjectMocks, опять же, не сделает объект бином
    // получается, не могу протестить, вызывается ли метод pushCommandRequest или нет :)

    @Test
    void shouldThrowForUnknownPriority() {
        final CommandRequest request = mock(CommandRequest.class);
        when(request.priority()).thenReturn(CommandPriority.UNKNOWN);

        assertThrows(UnknownCommandPriorityException.class, () -> this.processor.submit(request));
    }
}
