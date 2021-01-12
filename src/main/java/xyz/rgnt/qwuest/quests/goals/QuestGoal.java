package xyz.rgnt.qwuest.quests.goals;

import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.quests.shared.SharedCommon;

/**
 * AGoal is abstract class representing single-goal that player can accomplish
 */
public abstract class QuestGoal extends SharedCommon implements Listener {

    @Setter(AccessLevel.PROTECTED)
    private boolean accomplished = false;

    @Setter(AccessLevel.PROTECTED)
    private long goalStartTime;
    @Setter(AccessLevel.PROTECTED)
    private long goalEndTime;

    @Setter(AccessLevel.PROTECTED)
    private double difficultyMod = 1.0d;


    public QuestGoal( @NotNull String identifier) {
        super(identifier);
    }

    /**
     * @return Boolean value depending on whether Goal is accomplished or not.
     */
    public boolean isAccomplished() {
        return this.accomplished;
    }

    /**
     * Runs every tick
     */
    public void tick() {}

    /**
     * @return Progress bar
     */
    public abstract @NotNull String getProgressBar();

    /**
     * Creates copy of a Goal
     * @param rearm Rearm the goal?
     * @return Copy
     */
    public abstract @NotNull QuestGoal makeNew();

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
    public static interface Factory<T extends QuestGoal> extends SharedCommon.Factory<T> {
    }


}
