package sky.pro.telegrambot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambot.entity.NotificationTask;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {


}
