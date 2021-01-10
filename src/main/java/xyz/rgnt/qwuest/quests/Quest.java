package xyz.rgnt.qwuest.quests;

import lombok.Getter;
import java.util.UUID;

public class Quest {

    @Getter
    private UUID owner;

    @Getter
    private boolean autoComplete = false;

}
