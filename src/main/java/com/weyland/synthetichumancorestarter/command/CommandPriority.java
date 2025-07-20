package com.weyland.synthetichumancorestarter.command;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CommandPriority {
    COMMON("cmd_pr_common"),
    CRITICAL("cmd_pr_critical"),
    UNKNOWN("cmd_pr_unknown"); // for tests

    private final String str;

    CommandPriority(final String str) {
        this.str = str;
    }

    @Override @JsonValue
    public String toString()  {
        return this.str;
    }
}
