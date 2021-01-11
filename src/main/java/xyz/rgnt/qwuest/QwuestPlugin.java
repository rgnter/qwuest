package xyz.rgnt.qwuest;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.api.QwuestAPI;
import xyz.rgnt.qwuest.diagnostics.timings.Timer;
import xyz.rgnt.qwuest.providers.statics.ProgressStatics;
import xyz.rgnt.qwuest.providers.storage.flatfile.StorageProvider;

@Log4j2
public class QwuestPlugin extends JavaPlugin {

    private final Timer timer;
    {
        timer = Timer.timings();
    }


    @Getter
    private @NotNull StorageProvider storageProvider;

    @Getter
    private @NotNull QwuestAPI api;
    @Getter
    private @NotNull QuestManager questManager;



    @Override
    public void onLoad() {
        super.onLoad();

        timer.start();
        log.info("Constructing...");
        {
            this.storageProvider = new StorageProvider(this);

            api = new QwuestAPI(this);
            this.questManager = new QuestManager(this);
        }
        timer.stop();
        log.printf(Level.INFO, "Constructed in §a%.4fms\n", timer.resultMilli());
    }

    @Override
    public void onEnable() {
        super.onEnable();

        timer.start();
        log.info("Initializing...");
        {
            this.questManager.initialize();
        }
        timer.stop();
        log.printf(Level.INFO, "Initialized in §a%.4fms\n", timer.resultMilli());
    }

    @Override
    public void onDisable() {
        super.onDisable();

        timer.start();
        log.info("Terminating...");
        {
            this.questManager.terminate();
        }
        timer.stop();
        log.printf(Level.INFO, "Terminated in §a%.4fms\n", timer.resultMilli());
    }

}
