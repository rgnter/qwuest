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
import xyz.rgnt.qwuest.quests.excp.ParseFailedException;
import xyz.rgnt.qwuest.quests.goals.AGoal;

public class GoalKillMobs extends AGoal {

    @Getter
    private EntityType targetedEntity;
    @Getter
    private int currentAmount;
    @Getter
    private int targetedAmount;

    public GoalKillMobs(@NotNull Quest quest) {
        super(quest);
    }

    public GoalKillMobs(@NotNull Quest quest, EntityType targetedEntity, int currentAmount, int targetedAmount) {
        super(quest);
        this.targetedEntity = targetedEntity;
        this.currentAmount = currentAmount;
        this.targetedAmount = targetedAmount;
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

    public static class Factory implements AGoal.Factory<GoalKillMobs> {

        @Override
        public @NotNull GoalKillMobs produce(@NotNull GoalKillMobs goal, @NotNull FriendlyData data) throws ParseFailedException {
            String entityName = data.getString("target");
            if(entityName == null)
                throw new ParseFailedException("Missing entity target");
            try {
                goal.targetedEntity = EntityType.valueOf(entityName.toUpperCase());
            } catch (IllegalArgumentException exception) {
                throw new ParseFailedException("Specified entity is invalid");
            }

            if(data.isSet("amount"))
                goal.currentAmount  = data.getInt("amount", 0);
            else
                throw new ParseFailedException("Missing target amount");
            if(goal.currentAmount <= 0)
                throw new ParseFailedException("Target amount can't be 0 or smaller than 0");

            return goal;
        }

    }

}
