package xyz.rgnt.qwuest.quests;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.quests.goals.QuestGoal;
import xyz.rgnt.qwuest.quests.rewards.QuestReward;
import xyz.rgnt.qwuest.quests.shared.CommonInfo;
import xyz.rgnt.qwuest.quests.shared.SharedCommon;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class QuestRepo {

    private static final Map<Class<QuestGoal>, QuestGoal.Factory<QuestGoal>>       goals   = new HashMap<>();
    private static final Map<Class<QuestReward>, QuestReward.Factory<QuestReward>> rewards = new HashMap<>();


    /**
     * Registers goal factory
     *
     * @param goalClass   Goal class
     * @param goalFactory Goal factory
     */
    public static boolean registerGoal(@NotNull Class<? extends QuestGoal> goalClass, @NotNull QuestGoal.Factory<? extends QuestGoal> goalFactory) {
        var info = goalClass.getDeclaredAnnotation(CommonInfo.class);
        if(info == null) {
            log.warn("Tried to register Goal '{}' that doesn't have CommonInfo annotation", goalClass.getName());
            return false;
        }
        if(goals.containsKey(goalClass))
            log.warn("Registering goal '{}' that overrides existing goal", goalClass.getName());

        // todo perhaps check if specifier is retarded?

        goals.put((Class<QuestGoal>) goalClass, (QuestGoal.Factory<QuestGoal>) goalFactory);
        return true;
    }

    /**
     * Gets goal factory
     *
     * @param goalClass Goal class
     * @return Goal factory
     */
    public static @NotNull Optional<QuestGoal.Factory<QuestGoal>> getGoal(@NotNull Class<? extends QuestGoal> goalClass) {
        return Optional.ofNullable(goals.get(goalClass));
    }

    /**
     * Registers reward factory
     * @param rewardClass   Reward class
     * @param goalFactory Reward factory
     */
    public static boolean registerReward(@NotNull Class<? extends QuestReward> rewardClass, @NotNull QuestReward.Factory<? extends QuestReward> goalFactory) {
        var info = rewardClass.getDeclaredAnnotation(CommonInfo.class);
        if(info == null) {
            log.warn("Tried to register Reward '{}' that doesn't have CommonInfo annotation", rewardClass.getName());
            return false;
        }
        if(goals.containsKey(rewardClass))
            log.warn("Registering reward '{}' that overrides existing goal", rewardClass.getName());

        rewards.put((Class<QuestReward>)rewardClass, (QuestReward.Factory<QuestReward>)goalFactory);
        return true;
    }

    /**
     * Gets reward factory
     * @param goalClass Reward class
     * @return Reward factory
     */
    public static @NotNull Optional<QuestReward.Factory<QuestReward>> getReward(@NotNull Class<? extends QuestReward> goalClass) {
        return Optional.ofNullable(rewards.get(goalClass));
    }

    /**
     * Gets registered SharedCommon
     * @param clazz Common class
     * @return Shared common
     */
    public static <T extends SharedCommon> @NotNull Optional<SharedCommon.Factory<T>> getAny(@NotNull Class<? extends T> clazz) {
        if(goals.containsKey(clazz))
            return Optional.ofNullable((SharedCommon.Factory<T>) goals.get(clazz));
        if(rewards.containsKey(clazz))
            return Optional.ofNullable((SharedCommon.Factory<T>)rewards.get(clazz));
        return Optional.empty();
    }

    /**
     * @return Immutable map of Goals
     */
    public static @NotNull Map<Class<QuestGoal>, QuestGoal.Factory<QuestGoal>> getGoals() {
        return Collections.unmodifiableMap(goals);
    }

    /**
     * @return Immutable map of Rewards
     */
    public static @NotNull Map<Class<QuestReward>, QuestReward.Factory<QuestReward>> getRewards() {
        return Collections.unmodifiableMap(rewards);
    }




}
