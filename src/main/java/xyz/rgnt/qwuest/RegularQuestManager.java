package xyz.rgnt.qwuest;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.qwuest.providers.storage.flatfile.data.FriendlyData;
import xyz.rgnt.qwuest.quests.Quest;
import xyz.rgnt.qwuest.quests.QuestRepo;
import xyz.rgnt.qwuest.quests.shared.CommonInfo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * Dailyquest
 */
@Log4j2
public class RegularQuestManager implements Listener {

    @Getter
    private final QuestManager questManager;

    private final Map<UUID, Quest> boundQuests = new HashMap<>();

    private final FriendlyData dailyQuestSettings;

    private volatile Random random = new Random();
    private volatile LocalDate dailyNextTime = LocalDate.MIN;
    private volatile LocalTime hourlyNextTime = LocalTime.MIN;

    private Set<Quest> dailyQuests = new HashSet<>();
    private Quest hourlyQuest;

    /**
     * Default constructor
     * @param questManager Quest manager
     */
    public RegularQuestManager(@NotNull QuestManager questManager) {
        this.questManager = questManager;
        this.dailyQuestSettings = questManager.getDailyQuestSettings();
    }

    private synchronized void generateDailyQuest() {
        this.dailyNextTime = LocalDate.now().plusDays(1);

        FriendlyData questSettings = this.dailyQuestSettings.getSector("quest");
        if(questSettings == null) {
            log.error("Couldn't find config sector 'quest'");
            return;
        }

        // Quests
        Integer questMaxCount = questSettings.getInt("max-count");
        Integer questMinCount = questSettings.getInt("min-count");
        if(questMaxCount == null || questMinCount == null) {
            log.error("Couldn't find min and max count for quest.");
            return;
        }
        int questDeltaCount = questMaxCount - questMinCount;
        int questCount = 0;
        if(questDeltaCount == 0)
            questCount = questMaxCount;
        else
            questCount = this.random.nextInt(questMaxCount - questMinCount) + questMinCount;

        for (int questId = 0; questId < questCount; questId++) {
            Quest.Creator quest = Quest.Creator.createQuest("dailyQuest" + questId).unbound();
            // goals
            Integer goalMaxCount = questSettings.getInt("goals.max-count");
            Integer goalMinCount = questSettings.getInt("goals.min-count");
            if(goalMaxCount == null || goalMinCount == null) {
                log.error("Couldn't find min and max count for quest goals.");
                return;
            }
            int goalDeltaCount = goalMaxCount - goalMinCount;
            int goalCount = 0;
            if(goalDeltaCount == 0)
                goalCount = goalMaxCount;
            else
                goalCount = this.random.nextInt(goalDeltaCount) + goalMinCount;

            List<String> filtered = Arrays.asList(questSettings.getString("goals.filter", "").split(","));
            var possibleGoals =
                    QuestRepo.getGoals((goalClass) -> !filtered.contains(goalClass.getDeclaredAnnotation(CommonInfo.class).specifier()));
            if(possibleGoals.size() == 0) {
                log.error("No matching goals found for filter '{}'", filtered.toString());
                continue;
            }

            for (int goalId = 0; goalId < goalCount; goalId++) {
                var selectedGoalOpt = possibleGoals.keySet().stream().skip(random.nextInt(possibleGoals.size()-1)).findAny();
                if(selectedGoalOpt.isEmpty()) {
                    log.error("What the fuck? Failed to get goal from filtered result! Filter: '{}', Possible: '{}'", filtered.toString(), possibleGoals.toString());
                    continue;
                }
                var selectedGoal = selectedGoalOpt.get();
                        //quest.withRandomGoal("goal" + goalId, selectedGoal, this.dailyQuestSettings.getSector("goal-settings." + goalId ));
            }



            // rewards
            Integer rewardMaxCount = questSettings.getInt("rewards.max-count");
            Integer rewardMinCount = questSettings.getInt("rewards.min-count");
            if(rewardMaxCount == null || rewardMinCount == null) {
                log.error("Couldn't find min and max count for quest rewards.");
                return;
            }
            int rewardDeltaCount = rewardMaxCount - rewardMinCount;
            int rewardCount = 0;
            if(rewardDeltaCount == 0)
                rewardCount = rewardMaxCount;
            else
                rewardCount = this.random.nextInt(rewardDeltaCount) + rewardMinCount;

            String[] rewardFilters = questSettings.getString("goals.filter", "").split(",");
        }
    }

    private synchronized void generateHourlyQuest() {
        this.hourlyNextTime = LocalTime.of(LocalTime.now().getHour()+1, 0);
    }

    public void initialize() {


        log.info("Submitting async generator thread...");
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                log.error("Async generator thread interrupted!");
            }
            if(LocalDate.now().isAfter(dailyNextTime)) {
                generateDailyQuest();
                log.info("§aGenerating new daily quests.");
            }
            if(LocalTime.now().isAfter(hourlyNextTime)) {
                generateDailyQuest();
                log.info("§aGenerating new hourly quests.");
            }
        });

    }

    public void terminate() {

    }

    /**
     * @return Imm
     */
    public @NotNull Map<UUID, Quest> getBoundQuests() {
        return Collections.unmodifiableMap(this.boundQuests);
    }
}
