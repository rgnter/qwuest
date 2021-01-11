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
public abstract class SharedCommon {

    @Getter(AccessLevel.PROTECTED)
    private Quest quest;

    /**
     * Used for identifying goal in quest
     */
    @Getter
    private String identifier;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private boolean isActive;

    /**
     * Default shared constructor
     *
     * @param identifier Identifier for debug purposes
     */
    public SharedCommon(@NotNull String identifier) {
        this.identifier = identifier;
    }

    /**
     * Checks if is bound to quest
     * @return Boolean
     */
    public boolean isBound() {
        return quest != null;
    }

    /**
     * Binds quest
     * @param quest Quest to bind
     * @return Whether bind was successful or not
     */
    public boolean bind(@NotNull Quest quest) {
        if (!isBound()) {
            this.quest = quest;
            onBind();
            return true;
        } else if (onRebind(quest)) {
            this.quest = quest;
            return true;
        }
        return false;
    }

    /**
     * Unbinds quest
     * @return Whether unbind was successful or not
     */
    public boolean unbind() {
        if (isBound()) {
            this.quest = null;
            onUnbind();
            return true;
        }
        return false;
    }

    /**
     * Called when rebound to another quest
     * @param another Rebound quest
     */
    protected abstract boolean onRebind(@NotNull Quest another);

    /**
     * Called when bound to new quest
     */
    protected abstract boolean onBind();

    /**
     * Called when quest gets unbound
     */
    protected abstract boolean onUnbind();


    /**
     * @return Goal specifier
     */
    public @NotNull String getSpecifier() {
        return getClass().getDeclaredAnnotation(CommonInfo.class).specifier();
    }

    /**
     * @return Goal friendly name
     */
    public @NotNull String getFriendlyName() {
        return getClass().getDeclaredAnnotation(CommonInfo.class).friendlyName();
    }


    public static interface Factory<T extends SharedCommon> extends CommonFactory<T, FriendlyData, T, ProducerBadDataProvided> {

    }
}
