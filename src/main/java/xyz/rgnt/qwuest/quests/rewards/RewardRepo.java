package xyz.rgnt.qwuest.quests.rewards;

import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.quests.goals.AGoal;
import xyz.rgnt.qwuest.quests.rewards.AReward;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RewardRepo {

    private static final Map<Class<AReward>, AReward.Factory<AReward>> rewards = new HashMap<>();

    /**
     * Registers reward factory
     * @param goalClass   Reward class
     * @param goalFactory Reward factory
     */
    public static void registerReward(@NotNull Class<? extends AReward> goalClass, @NotNull AReward.Factory<? extends AReward> goalFactory) {
        rewards.put((Class<AReward>)goalClass, (AReward.Factory<AReward>)goalFactory);
    }

    /**
     * Gets reward factory
     * @param goalClass Reward class
     * @return Reward factory
     */
    public static @NotNull Optional<AReward.Factory<AReward>> getReward(@NotNull Class<AReward> goalClass) {
        return Optional.ofNullable(rewards.get(goalClass));
    }

    /**
     * @return Immutable map of Rewards
     */
    public static @NotNull Map<Class<AReward>, AReward.Factory<AReward>> getRewards() {
        return Collections.unmodifiableMap(rewards);
    }
}
