package by.prus.siteinfocheck.controller;

import by.prus.siteinfocheck.service.MessageService;
import by.prus.siteinfocheck.service.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Set;


@Component
public class Bot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botUserName;
    @Value("${bot.token}")
    private String botToken;

    @Autowired
    MessageService messageService;


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String incommingMessage = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            SendMessage loadingMessage = messageService.generateSimpleMessage(Constant.loading, chatId);
            sendMessage(loadingMessage);
            SendMessage sm = messageService.generateResponse(incommingMessage, chatId);
            sendMessage(sm);
        }
    }

    @Scheduled(cron = "0 0/15 9-21 * * *") // 10-22 each 15 min , Italy time.
    public void sheduledCheck(){
        Set<SendMessage> notifications = messageService.findChangesOnSites();
        notifications.forEach(this::sendMessage);
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
