package xyz.rgnt.qwuest.quests.excp;

import org.jetbrains.annotations.NotNull;

public class ParseFailedException extends Exception {
    public ParseFailedException(@NotNull String message) {
        super(message);
    }
}
