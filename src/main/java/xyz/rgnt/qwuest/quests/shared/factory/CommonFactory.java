package xyz.rgnt.qwuest.quests.shared.factory;

import org.jetbrains.annotations.NotNull;

/**
 * This class is common builder for classes QuestGoal and QuestReward
 * @param <X> Parameter 0
 * @param <Y> Parameter 1
 * @param <R> Return type
 * @param <E> Exception type
 */
public interface CommonFactory<X, Y, R, E extends Exception> {
    R produce(@NotNull X param0, @NotNull Y data) throws E;
    R produceRandom(@NotNull X param0, @NotNull Y data) throws  E;
}
