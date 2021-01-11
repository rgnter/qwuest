package xyz.rgnt.qwuest.quests.shared;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.rgnt.qwuest.providers.storage.flatfile.data.FriendlyData;
import xyz.rgnt.qwuest.quests.Quest;
import xyz.rgnt.qwuest.quests.excp.ProducerBadDataProvided;
import xyz.rgnt.qwuest.quests.shared.factory.CommonFactory;

/**
 * Class providing shared common for Goals and Rewards
 */
public class SharedCommon {

    @Getter(AccessLevel.PROTECTED)
    private @Nullable Quest quest;

    /**
     * Used for identifying goal in quest
     */
    @Getter
    private String identifier;

    @Getter @Setter(AccessLevel.PROTECTED)
    private boolean isActive;


    /**
     * Default shared constructor
     * @param quest      Owning quest
     * @param identifier Identifier for debug purposes
     */
    public SharedCommon(@Nullable Quest quest, @NotNull String identifier) {
        this.quest = quest;
        this.identifier = identifier;
    }

    /**
     * @return Goal specifier
     */
    public  @NotNull String getSpecifier() {
        return getClass().getDeclaredAnnotation(CommonInfo.class).specifier();
    }

    /**
     * @return Goal friendly name
     */
    public  @NotNull String getFriendlyName() {
        return getClass().getDeclaredAnnotation(CommonInfo.class).friendlyName();
    }



    public static interface Factory<T extends SharedCommon> extends CommonFactory<T, FriendlyData, T, ProducerBadDataProvided> {

    }
}
