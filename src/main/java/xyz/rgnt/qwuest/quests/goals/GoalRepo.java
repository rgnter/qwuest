package xyz.rgnt.qwuest.quests.goals;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GoalRepo {

    private static final Map<Class<AGoal>, AGoal.Factory<AGoal>> goals = new HashMap<>();

    /**
     * Registers goal factory
     *
     * @param goalClass   Goal class
     * @param goalFactory Goal factory
     */
    public static void registerGoal(@NotNull Class<? extends AGoal> goalClass, @NotNull AGoal.Factory<? extends AGoal> goalFactory) {
        goals.put((Class<AGoal>) goalClass, (AGoal.Factory<AGoal>) goalFactory);
    }

    /**
     * Gets goal factory
     *
     * @param goalClass Goal class
     * @return Goal factory
     */
    public static @NotNull Optional<AGoal.Factory<AGoal>> getGoal(@NotNull Class<AGoal> goalClass) {
        return Optional.ofNullable(goals.get(goalClass));
    }

    /**
     * @return Immutable map of Goals
     */
    public static @NotNull Map<Class<AGoal>, AGoal.Factory<AGoal>> getGoals() {
        return Collections.unmodifiableMap(goals);
    }
}
