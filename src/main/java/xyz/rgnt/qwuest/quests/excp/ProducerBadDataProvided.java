package xyz.rgnt.qwuest.quests.excp;

import org.jetbrains.annotations.NotNull;

public class ProducerBadDataProvided extends Exception {
    public ProducerBadDataProvided(@NotNull String message) {
        super(message);
    }
}
