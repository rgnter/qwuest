package xyz.rgnt.qwuest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.providers.storage.flatfile.data.FriendlyData;
import xyz.rgnt.qwuest.quests.goals.GoalRepo;
import xyz.rgnt.qwuest.quests.goals.impl.GoalKillMobs;

@Log4j2
public class QuestManager {

    @Getter
    private final QwuestPlugin plugin;

    @Getter(AccessLevel.PACKAGE)
    private FriendlyData dailyQuestSettings;
    @Getter(AccessLevel.PACKAGE)
    private FriendlyData trailQuestSettings;

    @Getter
    private final RegularQuestManager dailyQuests;
    @Getter
    private final UniqueQuestManager  trailQuests;


    public QuestManager(@NotNull QwuestPlugin plugin) {
        this.plugin = plugin;

        this.dailyQuests = new RegularQuestManager(this);
        this.trailQuests = new UniqueQuestManager(this);
    }

    /**
     * Initializes quest manager
     */
    public void initialize() {
        GoalRepo.registerGoal(GoalKillMobs.class, new GoalKillMobs.Factory());

        try {
            try {
                System.out.println(getPlugin().getResource("resources/configs/daily-quests.yml") != null);
                this.dailyQuestSettings = getPlugin().getStorageProvider().provideYaml("resources", "configs/daily-quests.yml", true).getData();
                log.info("Loaded settings for §aRegular Quests");
            } catch (Exception x) {
                log.error("Couldn't provide dailyquest settings!", x);
            }
             if(this.dailyQuestSettings != null) {
                 this.dailyQuests.initialize();
                 log.info("Initialized §aRegular Quests");

                 Bukkit.getPluginManager().registerEvents(getDailyQuests(), getPlugin());
                 log.info("Registered §aRegular Quests§r as listener");
             }
        } catch (Exception x) {
            log.error("Couldn't initialize §cRegularQuestManager", x);
        }
        try {
            try {
                this.trailQuestSettings = getPlugin().getStorageProvider().provideYaml("resources", "configs/quest-lines.yml", true).getData();
                log.info("Loaded settings for §aUnique Quests");
            } catch (Exception x) {
                log.error("Couldn't provide uniquequest settings!", x);
            }
            this.trailQuests.initialize();
            log.info("Initialized §aUnique Quests");
            Bukkit.getPluginManager().registerEvents(getDailyQuests(), getPlugin());
            log.info("Registered §aUnique Quests§r as listener");
        } catch (Exception x) {
            log.error("Couldn't initialize §cUniqueQuestManager", x);
        }
    }

    /**
     * Terminates quest manager
     */
    public void terminate() {
        System.out.println((GoalRepo.getGoals().size()));
        try {
            this.dailyQuests.terminate();
            log.info("Terminated §aRegular Quests");
        } catch (Exception x) {
            log.error("Couldn't initialize §cRegularQuestManager", x);
        }
        try {
            this.trailQuests.terminate();
            log.info("Terminated §aUnique Quests");
        } catch (Exception x) {
            log.error("Couldn't initialize §cUniqueQuestManager", x);
        }
    }
}
