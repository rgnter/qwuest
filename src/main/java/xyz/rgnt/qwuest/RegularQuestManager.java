package xyz.rgnt.qwuest;

import lombok.Getter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.quests.Quest;
import xyz.rgnt.qwuest.quests.goals.QuestGoal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Dailyquest
 */
public class RegularQuestManager implements Listener {

    @Getter
    private final QuestManager questManager;

    private final Map<UUID, Quest> boundQuests = new HashMap<>();
    private final Map<Class<? extends QuestGoal>, QuestGoal.Factory<? extends QuestGoal>> generatedGoals = new HashMap<>();

    /**
     * Default constructor
     * @param questManager Quest manager
     */
    public RegularQuestManager(@NotNull QuestManager questManager) {
        this.questManager = questManager;
    }


    public void initialize() {

    }

    public void terminate() {

    }

    /**
     * @return Imm
     */
    public @NotNull Map<UUID, Quest> getBoundQuests() {
        return Collections.unmodifiableMap(this.boundQuests);
    }
}
