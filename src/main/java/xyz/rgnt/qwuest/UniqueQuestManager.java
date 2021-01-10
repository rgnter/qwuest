package xyz.rgnt.qwuest;

import lombok.Getter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Questline
 */
public class UniqueQuestManager implements Listener {

    @Getter
    private final QuestManager questManager;

    /**
     * Default constructor
     * @param questManager Quest manager
     */
    public UniqueQuestManager(@NotNull QuestManager questManager) {
        this.questManager = questManager;
    }


    public void initialize() {

    }

    public void terminate() {

    }

}
