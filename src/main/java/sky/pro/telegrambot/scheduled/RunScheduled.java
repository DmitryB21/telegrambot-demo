package sky.pro.telegrambot.scheduled;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sky.pro.telegrambot.listener.TelegramBotUpdatesListener;
import sky.pro.telegrambot.model.NotificationTask;
import sky.pro.telegrambot.repo.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RunScheduled {

    @Autowired
    TelegramBotUpdatesListener telegramBotUpdatesListener;

    private final TelegramBot telegramBot;

    public RunScheduled(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Autowired
    private NotificationTaskRepository notificationTaskRepository;



    @Scheduled(cron = "0 0/1 * * * *")
    public void runScheduled() {
        Collection<NotificationTask> taskList =
                notificationTaskRepository.findNotificationTaskByTimeNotification(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        for (NotificationTask notificationTask : taskList) {
            String sendMessage = notificationTask.getTimeNotification() + " " + notificationTask.getTextNotification();
            telegramBotUpdatesListener.sendMessage(notificationTask.getChatId(), sendMessage);
        }

    }
}
