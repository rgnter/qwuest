package xyz.rgnt.qwuest.quests.goals.impl;

import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.providers.statics.ProgressStatics;
import xyz.rgnt.qwuest.providers.storage.flatfile.data.FriendlyData;
import xyz.rgnt.qwuest.quests.Quest;
import xyz.rgnt.qwuest.quests.excp.ProducerBadDataProvided;
import xyz.rgnt.qwuest.quests.goals.QuestGoal;
import xyz.rgnt.qwuest.quests.shared.CommonInfo;

import java.util.Set;

@CommonInfo(specifier = "killmob", friendlyName = "Kill Mobs")
public class GoalKillMob extends QuestGoal {

    @Getter
    private EntityType targetedEntity;
    @Getter
    private int currentAmount;
    @Getter
    private int targetedAmount;

    public GoalKillMob(@NotNull Quest quest, @NotNull String identifier) {
        super(quest, identifier);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        if(event.getEntityType() == targetedEntity) {
            Player killer = event.getEntity().getKiller();
            if(killer == null)
                return;

            if(killer.getUniqueId().equals(getQuest().getOwner()))
                currentAmount++;
        }
    }

    @Override
    public @NotNull String getProgressBar() {
        return ProgressStatics.getProgressBar(currentAmount, targetedAmount);
    }

    public static class Factory implements QuestGoal.Factory<GoalKillMob> {
        @Override
        public @NotNull GoalKillMob produce(@NotNull GoalKillMob goal, @NotNull FriendlyData data) throws ProducerBadDataProvided {
            String entityName = data.getString("target");
            if(entityName == null)
                throw new ProducerBadDataProvided("Missing entity target");
            try {
                goal.targetedEntity = EntityType.valueOf(entityName.toUpperCase());
            } catch (IllegalArgumentException exception) {
                throw new ProducerBadDataProvided("Specified entity is invalid");
            }

            if(data.isSet("amount"))
                goal.currentAmount  = data.getInt("amount", 0);
            else
                throw new ProducerBadDataProvided("Missing target amount");
            if(goal.currentAmount <= 0)
                throw new ProducerBadDataProvided("Target amount can't be 0 or lower than 0");
            return goal;
        }

        @Override
        public @NotNull GoalKillMob produceRandom(@NotNull GoalKillMob goal, @NotNull FriendlyData settings) throws ProducerBadDataProvided {
            Set<String> entityNames = settings.getKeys("");

            return null;
        }
    }

}
