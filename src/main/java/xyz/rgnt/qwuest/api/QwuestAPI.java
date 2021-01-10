package xyz.rgnt.qwuest.api;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.QwuestPlugin;

public class QwuestAPI {

    @Getter
    private QwuestPlugin plugin;
    private static QwuestAPI self;
    {
        self = this;
    }

    public QwuestAPI(@NotNull QwuestPlugin plugin) {
        this.plugin = plugin;
    }

    public static @NotNull QwuestAPI getInstance() {
        return self;
    }
}
