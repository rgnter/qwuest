package xyz.rgnt.qwuest.quests.rewards;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.quests.Quest;
import xyz.rgnt.qwuest.quests.shared.SharedCommon;

/**
 * AReward is abstract class representing single-reward that can be redeemed by player.
 */
public abstract class QuestReward extends SharedCommon {


    public QuestReward(@NotNull String identifier) {
        super(identifier);
    }

    /**
     * Redeems reward to player
     */
    public abstract void redeem();

    /**
     * Creates new reward with same parameters
     */
    public abstract void makeNew();

    /**
     * Factory
     * @param <T> Goal type
     */
    public static interface Factory<T extends QuestReward> extends SharedCommon.Factory<T> {
    }

}
