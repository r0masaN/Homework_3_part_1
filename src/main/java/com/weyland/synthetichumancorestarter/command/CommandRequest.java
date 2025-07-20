package com.weyland.synthetichumancorestarter.command;

import com.weyland.synthetichumancorestarter.validation.ISO8601;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommandRequest(
        @NotBlank(message = "description is required") @Size(max = 1000, message = "description must be <= 1000 characters")
        String description,
        @NotNull(message = "priority is required")
        CommandPriority priority,
        @NotBlank(message = "author is required") @Size(max = 100, message = "author must be <= 100 characters")
        String author,
        @NotBlank(message = "time is required") @ISO8601(message = "time must be in ISO 8601 format (YYYY-MM-DDThh:mm:ss)")
        String time // ISO 8601
) {}
