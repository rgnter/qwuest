package xyz.rgnt.qwuest.quests.rewards;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.quests.Quest;

/**
 * AReward is abstract class representing single-reward that can be redeemed by player.
 */
public abstract class AReward {


    @Getter(AccessLevel.PROTECTED)
    private Quest quest;

    /**
     * Redeems reward to player
     * @param player Player
     */
    public abstract void redeem(@NotNull Player player);

    public static interface Factory<T extends AReward> {
        @NotNull AReward produce(@NotNull Object ... data);
    }
}
