package xyz.rgnt.qwuest.quests.excp;

import org.jetbrains.annotations.NotNull;

public class QuestCreatorException extends Exception {
    public QuestCreatorException(@NotNull String message) {
        super(message);
    }
}
