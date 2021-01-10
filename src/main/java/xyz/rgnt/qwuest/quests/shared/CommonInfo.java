package xyz.rgnt.qwuest.quests.shared;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommonInfo {
    @NotNull String specifier();
    @NotNull String friendlyName();
}
