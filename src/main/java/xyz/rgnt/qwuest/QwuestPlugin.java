package xyz.rgnt.qwuest;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.rgnt.qwuest.diagnostics.timings.Timer;

@Log4j2
public class QwuestPlugin extends JavaPlugin {

    private final Timer timer;
    {
        timer = Timer.timings();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        timer.start();
        log.info("Constructing...");
        {


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


        }
        timer.stop();
        log.printf(Level.INFO, "Terminated in §a%.4fms\n", timer.resultMilli());
    }

}
