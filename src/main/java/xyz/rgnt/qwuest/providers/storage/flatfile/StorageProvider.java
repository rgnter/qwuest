package xyz.rgnt.qwuest.providers.storage.flatfile;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.rgnt.qwuest.QwuestPlugin;
import xyz.rgnt.qwuest.providers.storage.flatfile.store.AStore;

public class StorageProvider {

    private final @NotNull QwuestPlugin instance;

    /**
     * Default constructor
     * @param instance Instance of plugin
     */
    public StorageProvider(@NotNull QwuestPlugin instance) {
        this.instance = instance;
    }

    /**
     * Constructs YAML Store, and prepares it
     * @param path       Path to resource
     * @param hasDefault Load default if not available in data folder
     * @return YAML Store
     * @throws Exception When something goes wrong
     */
    public @NotNull AStore provideYaml(@Nullable String root, @NotNull String path, boolean hasDefault) throws Exception {
        return AStore.makeYaml(this.instance, root, path, hasDefault).prepare();
    }


    /**
     * Constructs JSON Store, and prepares it
     * @param path       Path to resource
     * @param hasDefault Load default if not available in data folder
     * @return YAML Store
     * @throws Exception When something goes wrong
     */
    public @NotNull AStore provideJson(@Nullable String root, @NotNull String path, boolean hasDefault) throws Exception {
        return AStore.makeJson(this.instance, root, path, hasDefault).prepare();
    }


}
