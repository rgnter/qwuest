package xyz.rgnt.qwuest.quests.shared;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.providers.storage.flatfile.data.FriendlyData;
import xyz.rgnt.qwuest.quests.Quest;
import xyz.rgnt.qwuest.quests.excp.ProducerBadDataProvided;
import xyz.rgnt.qwuest.quests.shared.factory.CommonFactory;

/**
 * Class providing shared Factory for Goals and Rewards
 */
public class SharedQuestPart {

    @Getter(AccessLevel.PROTECTED)
    private Quest quest;

    /**
     * Used for identifying goal in quest
     */
    @Getter
    private String identifier;

    /**
     * Default shared constructor
     * @param quest      Owning quest
     * @param identifier Identifier for debug purposes
     */
    public SharedQuestPart(@NotNull Quest quest, @NotNull String identifier) {
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



    public static interface Factory<T extends SharedQuestPart> extends CommonFactory<T, FriendlyData, T, ProducerBadDataProvided> {

    }
}
