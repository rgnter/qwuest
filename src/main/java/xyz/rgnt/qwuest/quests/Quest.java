package xyz.rgnt.qwuest.quests;

import com.destroystokyo.paper.entity.ai.Goal;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.rgnt.qwuest.api.QwuestAPI;
import xyz.rgnt.qwuest.providers.storage.flatfile.data.FriendlyData;
import xyz.rgnt.qwuest.quests.excp.ProducerBadDataProvided;
import xyz.rgnt.qwuest.quests.excp.QuestCreatorException;
import xyz.rgnt.qwuest.quests.goals.QuestGoal;
import xyz.rgnt.qwuest.quests.rewards.QuestReward;
import xyz.rgnt.qwuest.quests.shared.SharedCommon;
import xyz.rgnt.qwuest.util.BiFunc;

import java.util.*;
import java.util.stream.Collectors;

public class Quest {

    @Getter
    private UUID owner;

    /**
     * Used for identifying quest
     */
    @Getter
    private @NotNull final String identifier;

    @Getter
    private int tickableId = -1;

    @Getter
    private boolean autoComplete = false;
    @Getter
    private boolean active = false;

    private final Set<QuestGoal>   goals   = new HashSet<>();
    private final Set<QuestReward> rewards = new HashSet<>();

    public Quest(@NotNull String identifier) {
        this.identifier = identifier;
    }

    /**
     * Activates quest
     */
    public boolean activate() {
        if(active)
            return false;
        this.active = true;
        // register tickable
        this.tickableId = QwuestAPI.getInstance().getPlugin().registerTickable(() -> {
            this.goals.forEach(QuestGoal::tick);
        });
        // register events
        this.goals.forEach(goal -> {
            Bukkit.getPluginManager().registerEvents(goal, QwuestAPI.getInstance().getPlugin());
        });
        return true;
    }

    /**
     * Deactivates quest
     */
    public boolean deactivate() {
        if(!active)
            return false;
        active = false;
        // unregister tickable
        if(tickableId != -1)
            QwuestAPI.getInstance().getPlugin().unregisterTickable(this.tickableId);

        // unregister events
        this.goals.forEach(HandlerList::unregisterAll);
        return true;
    }

    /**
     * @return Immutable map of Goals
     */
    public @NotNull Set<QuestGoal> getGoals() {
        return Collections.unmodifiableSet(this.goals);
    }

    /**
     * @return Immutable map of Rewards
     */
    public @NotNull Set<QuestReward> getRewards() {
        return Collections.unmodifiableSet(this.rewards);
    }


    @Log4j2
    public static class Creator {

        private Quest toCreate;

        private boolean bindAll = true;

        private Creator(@NotNull Quest toCreate) {
            this.toCreate = toCreate;
        }

        /**
         * Creates quest
         * @param questIdentifier Quest identifier for debug purposes
         * @return Quest.Creator
         */
        public static @NotNull Quest.Creator createQuest(@NotNull String questIdentifier) {
            return new Creator(new Quest(questIdentifier));
        }

        /**
         * Copies quest
         * @param quest Quest
         * @return Quest.Creator
         */
        public static @NotNull Quest.Creator copyQuest(@NotNull Quest quest) {
            if(quest.isActive()) {
                quest.deactivate();
                log.warn("Passed active quest to creator. This may have serious consequences.");
                for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                    log.warn(stackTraceElement);
                }
                log.warn("--");
            }
            Creator creator = new Creator(new Quest(quest.identifier));
            creator.toCreate.goals.addAll(quest.goals);
            creator.toCreate.rewards.addAll(quest.rewards);
            creator.toCreate.autoComplete = quest.autoComplete;
            creator.toCreate.owner = quest.owner;

            return creator;
        }
        /**
         * Modifies quest
         * @param quest Quest
         * @return Quest.Creator
         */
        public static @NotNull Quest.Creator modifyQuest(@NotNull Quest quest) {
            if(quest.isActive()) {
                quest.deactivate();
                log.warn("Passed active quest to creator. This may have serious consequences.");
                for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                    log.warn(stackTraceElement);
                }
                log.warn("--");
            }
            return new Creator(quest);
        }


        public @NotNull Quest.Creator forPlayer(@NotNull UUID player) {
            this.toCreate.owner = player;
            return this;
        }

        public @NotNull Quest.Creator forPlayer(@NotNull Player player) {
            return forPlayer(player.getUniqueId());
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
            var goal = makeByCommonFactory(goalIdentifier, goalClass, (factory, paramGoal) -> factory.produceRandom(paramGoal, settings));
            if(bindAll)
                goal.bind(this.toCreate);
            this.toCreate.goals.add(goal);
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
            var goal = makeByCommonFactory(goalIdentifier, goalClass, (factory, paramGoal) -> factory.produceRandom(paramGoal, data));
            if(bindAll)
                goal.bind(this.toCreate);
            this.toCreate.goals.add(goal);
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
            var reward = makeByCommonFactory(goalIdentifier, goalClass, (factory, paramGoal) -> factory.produceRandom(paramGoal, settings));
            if(bindAll)
                reward.bind(this.toCreate);
            this.toCreate.rewards.add(reward);
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
            var reward = makeByCommonFactory(goalIdentifier, goalClass, (factory, paramGoal) -> factory.produceRandom(paramGoal, data));
            if(bindAll)
                reward.bind(this.toCreate);
            this.toCreate.rewards.add(reward);
            return this;
        }

        /**
         * Whether quest can be auto-completed
         * @param autoComplete Boolean
         * @return This
         */
        public @NotNull Quest.Creator autoCompletable(boolean autoComplete) {
            this.toCreate.autoComplete = autoComplete;
            return this;
        }

        public @NotNull Quest.Creator unbound() {
            this.bindAll = false;
            return this;
        }

        public @NotNull Quest.Creator bounded() {
            this.bindAll = true;
            return this;
        }

        /**
         * @return Built quest
         */
        public @NotNull Quest build() {
            return toCreate;
        }


        // hehe docs
        private @Nullable Object makeCommonInstance(Class<?> clazz, @NotNull String identifier) {
            try {
                var constructor = clazz.getConstructor(String.class);
                return constructor.newInstance(identifier);
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

    }
}
