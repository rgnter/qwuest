package xyz.rgnt.qwuest.quests.goals;

import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.api.QwuestAPI;
import xyz.rgnt.qwuest.quests.Quest;
import xyz.rgnt.qwuest.quests.shared.SharedQuestPart;

/**
 * AGoal is abstract class representing single-goal that player can accomplish
 */
public abstract class QuestGoal extends SharedQuestPart implements Listener {

    @Setter(AccessLevel.PROTECTED)
    private int progressMin = 0, progressMax = 100;

    @Setter(AccessLevel.PROTECTED)
    private boolean accomplished = false;

    @Setter(AccessLevel.PROTECTED)
    private long goalStartTime;
    @Setter(AccessLevel.PROTECTED)
    private long goalEndTime;

    @Setter(AccessLevel.PROTECTED)
    private double difficultyMod = 1.0d;


    public QuestGoal(@NotNull Quest quest, @NotNull String identifier) {
        super(quest, identifier);
    }

    /**
     * @return Boolean value depending on whether Goal is accomplished or not.
     */
    public boolean isAccomplished() {
        return this.accomplished;
    }


    /**
     * @return Progress min value
     */
    public int getProgressMin() {
        return this.progressMin;
    }
    /**
     * @return Progress max value
     */
    public int getProgressMax() {
        return this.progressMax;
    }

    /**
     * @return Progress bar
     */
    public abstract @NotNull String getProgressBar();

    /**
     * Sexier way of registering listeners
     */
    public void registerAsListener() {
        Bukkit.getPluginManager().registerEvents( this, QwuestAPI.getInstance().getPlugin());
    }

    /**
     * Sexier way of unregistering listeners
     */
    public void unregisterAsListener() {
        HandlerList.unregisterAll(this);
    }


    /**
     * @return Difficulty modifier of this goal. Usually calculated with complicated formulae.
     */
    public double getDifficultyModifier() {
        return this.difficultyMod;
    }


    /**
     * Factory
     * @param <T> Goal type
     */
    public static interface Factory<T extends QuestGoal> extends SharedQuestPart.Factory<T> {
    }


}
