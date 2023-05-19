package sky.pro.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {

            //Получаем апдейт и фиксируем в лог его данные.
            logger.info("Processing update: {}", update);

            //проверяем, что сообщение не пустое
            Message msg = update.message();
            if (msg != null) {

                //Достаеv из переданного сообщения ID чата и текст сообщения
                Long chatId = update.message().chat().id();
                String inputText = update.message().text();
                String memberName = update.message().chat().firstName();
                //Проверяем, не является ли этот текст командой "/start"
                //В случае успешного сравнения создает сообщение в ответ и отправляет его в чат, с полученным ранее ID
                switch (inputText) {
                    case "/start":
                        startBot(chatId, memberName);
                        break;
                    default:
                        sendMessage(chatId,"Unexpected message");
                        logger.info("Unexpected message");
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void startBot(Long chatId, String memberName) {
        String answer = "Salute, " + memberName;
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage(chatId, textToSend);
        SendResponse sendResponse = telegramBot.execute(message);
        boolean ok = sendResponse.isOk();
        Message msg = sendResponse.message();
        logger.info("SendMessage: {}, {}", ok,  msg);
    }
}
