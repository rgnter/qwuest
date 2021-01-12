package xyz.rgnt.qwuest.quests.goals.impl;

import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.providers.statics.ProgressStatics;
import xyz.rgnt.qwuest.providers.storage.flatfile.data.FriendlyData;
import xyz.rgnt.qwuest.quests.excp.ProducerBadDataProvided;
import xyz.rgnt.qwuest.quests.goals.QuestGoal;
import xyz.rgnt.qwuest.quests.shared.CommonInfo;

import java.util.Set;

@CommonInfo(specifier = "killmobs", friendlyName = "Kill Mobs")
public class GoalKillMobs extends QuestGoal {

    @Getter
    private EntityType targetedEntity;
    @Getter
    private int currentAmount;
    @Getter
    private int targetedAmount;

    public GoalKillMobs(@NotNull String identifier, EntityType targetedEntity, int currentAmount, int targetedAmount) {
        super(identifier);
        this.targetedEntity = targetedEntity;
        this.currentAmount = currentAmount;
        this.targetedAmount = targetedAmount;
    }

    public GoalKillMobs(@NotNull String identifier) {
        super(identifier);
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
    protected boolean onBind() {
        this.currentAmount = 0;
        return true;
    }

    @Override
    protected boolean onUnbind() {
        return true;
    }

    @Override
    public @NotNull QuestGoal makeNew() {
        return new GoalKillMobs(getIdentifier(), getTargetedEntity(),  0, getTargetedAmount());
    }

    @Override
    public @NotNull String getProgressBar() {
        return ProgressStatics.getProgressBar(currentAmount, targetedAmount);
    }

    public static class Factory implements QuestGoal.Factory<GoalKillMobs> {
        @Override
        public @NotNull GoalKillMobs produce(@NotNull GoalKillMobs goal, @NotNull FriendlyData data) throws ProducerBadDataProvided {
            String entityName = data.getString("target");
            if(entityName == null)
                throw new ProducerBadDataProvided("Missing entity target");
            try {
                goal.targetedEntity = EntityType.valueOf(entityName.toUpperCase());
            } catch (IllegalArgumentException exception) {
                throw new ProducerBadDataProvided("Specified entity is invalid");
            }

            if(data.isSet("amount"))
                goal.currentAmount = data.getInt("amount", 0);
            else
                throw new ProducerBadDataProvided("Missing target amount");
            if(goal.currentAmount <= 0)
                throw new ProducerBadDataProvided("Target amount can't be 0 or lower than 0");
            return goal;
        }

        @Override
        public @NotNull GoalKillMobs produceRandom(@NotNull GoalKillMobs goal, @NotNull FriendlyData settings) throws ProducerBadDataProvided {
            Set<String> entityNames = settings.getKeys();

            return null;
        }
    }

}
