package com.weyland.synthetichumancorestarter;

import com.weyland.synthetichumancorestarter.command.CommandPriority;
import com.weyland.synthetichumancorestarter.command.CommandRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public final class CommandRequestValidationTest {
    private Validator validator;

    @BeforeEach
    public void setUpValidator() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void shouldThrowBecauseIncorrectCommandRequest() {
        final CommandRequest incorrectRequest = new CommandRequest(
                "", // blank
                null,
                "Romandjasoidjasodogjoijoighjfgohjrioehjitejhiojfiohjoijiohgjidjfgadsgfusdhgflusdhlgoidjsgidsjfgoisdfjgio", // 104 characters
                "20.07.2025 17:40:37"  // not ISO 8601
        );

        final Set<ConstraintViolation<CommandRequest>> violations = this.validator.validate(incorrectRequest);

        assertFalse(violations.isEmpty());

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("priority")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("author")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("time")));
    }

    @Test
    public void shouldThrowBecauseIncorrectDescription() {
        final CommandRequest incorrectDescriptionRequest = new CommandRequest(
                "", // blank cuz over 1000 characters is madness
                CommandPriority.COMMON,
                "Roman",
                "2025-07-20T16:30:05"
        );

        final Set<ConstraintViolation<CommandRequest>> violations = this.validator.validate(incorrectDescriptionRequest);

        assertFalse(violations.isEmpty());

        assertTrue(violations.stream().allMatch(v -> v.getPropertyPath().toString().equals("description")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("priority")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("author")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("time")));
    }

    @Test
    public void shouldThrowBecauseIncorrectPriority() {
        final CommandRequest incorrectPriorityRequest = new CommandRequest(
                "Random command request",
                null,
                "Roman",
                "2025-07-20T16:30:05"
        );

        final Set<ConstraintViolation<CommandRequest>> violations = this.validator.validate(incorrectPriorityRequest);

        assertFalse(violations.isEmpty());

        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("description")));
        assertTrue(violations.stream().allMatch(v -> v.getPropertyPath().toString().equals("priority")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("author")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("time")));
    }

    @Test
    public void shouldThrowBecauseIncorrectAuthor() {
        final CommandRequest incorrectAuthorRequest = new CommandRequest(
                "Random command request",
                CommandPriority.COMMON,
                "Romandjasoidjasodogjoijoighjfgohjrioehjitejhiojfiohjoijiohgjidjfgadsgfusdhgflusdhlgoidjsgidsjfgoisdfjgio", // 104 characters
                "2025-07-20T16:30:05"
        );

        final Set<ConstraintViolation<CommandRequest>> violations = this.validator.validate(incorrectAuthorRequest);

        assertFalse(violations.isEmpty());

        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("description")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("priority")));
        assertTrue(violations.stream().allMatch(v -> v.getPropertyPath().toString().equals("author")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("time")));
    }

    @Test
    public void shouldThrowBecauseIncorrectTime() {
        final CommandRequest incorrectTimeRequest = new CommandRequest(
                "Random command request",
                CommandPriority.COMMON,
                "Roman",
                "20.07.2025 17:40:37"  // not ISO 8601
        );

        final Set<ConstraintViolation<CommandRequest>> violations = this.validator.validate(incorrectTimeRequest);

        assertFalse(violations.isEmpty());

        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("description")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("priority")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("author")));
        assertTrue(violations.stream().allMatch(v -> v.getPropertyPath().toString().equals("time")));
    }
}
