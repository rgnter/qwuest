package xyz.rgnt.qwuest;

import org.jetbrains.annotations.NotNull;

public class QwuestAPI {

    private static QwuestAPI self;
    {
        self = this;
    }


    public static @NotNull QwuestAPI getInstance() {
        return self;
    }
}
