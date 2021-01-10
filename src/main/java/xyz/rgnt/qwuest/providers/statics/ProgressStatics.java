package xyz.rgnt.qwuest.providers.statics;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ProgressStatics {

    public static final int PROGRESS_STEP_MIN = 0;
    public static final int PROGRESS_STEP_MAX = 10;

    public static final String PROGRESS_VISUAL_BORDER             = "&f┃";
    public static final String PROGRESS_VISUAL_COMPLETED_STEP     = "&7░";
    public static final String PROGRESS_VISUAL_UNCOMPLETED_STEP   = "&a░";
    /**
     *
     * @param current Current progress
     * @param target  Targeted progress
     * @return Coloured progress bar
     */
    public static @NotNull String getProgressBar(double current, double target) {
        int currentStep = 0;

        // skip unnecessary calculations with 0
        if(current != 0 || target != 0) {
            final double percentage = current / target;
            currentStep = (int) (PROGRESS_STEP_MAX * percentage);
        }

        StringBuilder bar = new StringBuilder(PROGRESS_VISUAL_BORDER);

        for(int step = PROGRESS_STEP_MIN; step < PROGRESS_STEP_MAX; step++) {
            // step is completed
            if(step <= currentStep) {
                bar.append(PROGRESS_VISUAL_COMPLETED_STEP);
            } else
                bar.append(PROGRESS_VISUAL_UNCOMPLETED_STEP);
        }
        bar.append(PROGRESS_VISUAL_BORDER);
        return ChatColor.translateAlternateColorCodes('&', bar.toString());
    }

}
