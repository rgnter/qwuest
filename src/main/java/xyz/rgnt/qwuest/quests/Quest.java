package xyz.rgnt.qwuest.quests;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.rgnt.qwuest.providers.storage.flatfile.data.FriendlyData;
import xyz.rgnt.qwuest.quests.excp.ProducerBadDataProvided;
import xyz.rgnt.qwuest.quests.excp.QuestCreatorException;
import xyz.rgnt.qwuest.quests.goals.QuestGoal;
import xyz.rgnt.qwuest.quests.rewards.QuestReward;
import xyz.rgnt.qwuest.quests.shared.SharedCommon;
import xyz.rgnt.qwuest.util.BiFunc;

import java.util.*;

public class Quest {

    @Getter
    private UUID owner;

    /**
     * Used for identifying quest
     */
    @Getter
    private @NotNull String identifier;

    @Getter
    private boolean autoComplete = false;

    private final Set<QuestGoal> goals = new HashSet<>();
    private final Set<QuestReward> rewards = new HashSet<>();

    public Quest(@NotNull String identifier, @NotNull UUID owner) {
        this.owner = owner;
        this.identifier = identifier;
    }

    public @NotNull Set<QuestGoal> getGoals() {
        return Collections.unmodifiableSet(this.goals);
    }

    public @NotNull Set<QuestReward> getRewards() {
        return Collections.unmodifiableSet(this.rewards);
    }

    public static class Creator {

        private Quest toCreate;

        private Creator(@NotNull Quest toCreate) {
            this.toCreate = toCreate;
        }

        /**
         * Creates quest
         * @param questIdentifier Quest identifier for debug purposes
         * @param player          Owner of quest
         * @return Quest.Creator
         */
        public static @NotNull Quest.Creator createQuest(@NotNull String questIdentifier, @NotNull UUID player) {
            return new Creator(new Quest(questIdentifier, player));
        }

        /**
         * Creates quest
         * @param questIdentifier Quest identifier for debug purposes
         * @param player          Owner of quest
         * @return Quest.Creator
         */
        public static @NotNull Quest.Creator createQuest(@NotNull String questIdentifier, @NotNull Player player) {
            return createQuest(questIdentifier, player.getUniqueId());
        }

        // hehe docs
        private @Nullable Object makeCommonInstance(Class<?> clazz, @NotNull String identifier) {
            try {
                var constructor = clazz.getConstructor(Quest.class, String.class);
                return constructor.newInstance(this.toCreate, identifier);
            } catch (Exception ignored) {
            }
            return null;
        }


        private <T extends SharedCommon>@NotNull T makeByCommonFactory(@NotNull String identifier, @NotNull Class<? extends T> clazz,
                                                                       @NotNull BiFunc<SharedCommon.Factory<T>, T,  T, ProducerBadDataProvided> func) throws QuestCreatorException {
            // get goal optFactory
            Optional<SharedCommon.Factory<T>> optFactory = QuestRepo.getAny(clazz);
            if (optFactory.isEmpty())
                throw new QuestCreatorException("Couldn't get factory for: " + clazz.getName() + " (Cause: Specified factory doesn't exist and/or is not registered)");
            var factory = optFactory.get();

            // make instance from class
            var obj = makeCommonInstance(clazz, identifier);
            if (obj == null)
                throw new QuestCreatorException("Couldn't make instance for class: " + clazz.getName() + " (Cause: Class is not properly inherited by specified goal)");

            // produce goal from optFactory
            T part = (T) obj;
            try {
                part = func.apply(factory, part);
            } catch (ProducerBadDataProvided e) {
                throw new QuestCreatorException("Couldn't produce goal '" + part.getIdentifier() + "(" + part.getSpecifier() + ") for quest '"
                        + toCreate.getIdentifier() + "! " + e.getMessage());
            }
            return (T) part;
        }


        /**
         * Adds random goal to quest
         * @param goalIdentifier Goal identifier
         * @param goalClass      Goal class
         * @param settings       Goal settings for optFactory
         * @return This
         * @throws QuestCreatorException When something goes wrong
         */
        public @NotNull Quest.Creator withRandomGoal(@NotNull String goalIdentifier, @NotNull Class<? extends QuestGoal> goalClass, @NotNull FriendlyData settings) throws QuestCreatorException {
            this.toCreate.goals.add(makeByCommonFactory(goalIdentifier, goalClass, (factory, goal) -> factory.produceRandom(goal, settings)));
            return this;
        }

        /**
         * Adds goal to quest
         * @param goalIdentifier Goal identifier
         * @param goalClass      Goal class
         * @param data           Goal data for optFactory
         * @return This
         * @throws QuestCreatorException When something goes wrong
         */
        public @NotNull Quest.Creator withGoal(@NotNull String goalIdentifier, @NotNull Class<? extends QuestGoal> goalClass, @NotNull FriendlyData data) throws QuestCreatorException {
            this.toCreate.goals.add(makeByCommonFactory(goalIdentifier, goalClass, (factory, goal) -> factory.produce(goal, data)));
            return this;
        }


        /**
         * Adds random reward to quest
         * @param goalIdentifier Reward identifier
         * @param goalClass      Reward class
         * @param settings       Reward settings for factory
         * @return This
         * @throws QuestCreatorException When something goes wrong
         */
        public @NotNull Quest.Creator withRandomReward(@NotNull String goalIdentifier, @NotNull Class<? extends QuestReward> goalClass, @NotNull FriendlyData settings) throws QuestCreatorException {
            this.toCreate.rewards.add(makeByCommonFactory(goalIdentifier, goalClass, (factory, goal) -> factory.produceRandom(goal, settings)));
            return this;
        }

        /**
         * Adds reward to quest
         * @param goalIdentifier Reward identifier
         * @param goalClass      Reward class
         * @param data           Reward data for factory
         * @return This
         * @throws QuestCreatorException When something goes wrong
         */
        public @NotNull Quest.Creator withReward(@NotNull String goalIdentifier, @NotNull Class<? extends QuestReward> goalClass, @NotNull FriendlyData data) throws QuestCreatorException {
            this.toCreate.rewards.add(makeByCommonFactory(goalIdentifier, goalClass, (factory, goal) -> factory.produce(goal, data)));
            return this;
        }

    }
}
