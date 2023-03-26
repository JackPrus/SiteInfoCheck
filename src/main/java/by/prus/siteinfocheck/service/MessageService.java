package by.prus.siteinfocheck.service;

import by.prus.siteinfocheck.repository.StorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

@Service
public class MessageService {

    @Value("${bot.name}")
    private String botUserName;
    @Value("${bot.token}")
    private String botToken;
    @Autowired
    private CheckJuventusInfoService checkJuventusInfoService;
    @Autowired
    private CheckMilanoInfoService checkMilanoInfoService;
    @Autowired
    private StorageRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    public SendMessage generateResponse(String incommingMessage, String chatId) {
        repository.addUser(chatId);
        if (incommingMessage.equalsIgnoreCase(Constant.juventusButton)) {
            return createResponse(checkJuventusInfoService.checkInfo(), Constant.JUVENTUS, chatId);
        } else if (incommingMessage.equalsIgnoreCase(Constant.milanoButton)) {
            return createResponse(checkMilanoInfoService.checkInfo(), Constant.MILANO, chatId);
        } else if (incommingMessage.equalsIgnoreCase(Constant.logout)){
            return generateSimpleMessage(repository.deleteSubscriber(chatId), chatId);
        }else{
            return drawButtons(chatId);
        }
    }

    public synchronized Set<SendMessage> findChangesOnSites() {
        Set<SendMessage> resultSet = new HashSet<>();

        String juveResult = checkJuventusInfoService.checkInfo();
        String milanoResult = checkMilanoInfoService.checkInfo();

        String dataJuveFromStorage = repository.getDataFromStorage(Constant.juventusButton);
        String dataMilanoFromStorage = repository.getDataFromStorage(Constant.milanoButton);

        if (!juveResult.equalsIgnoreCase(dataJuveFromStorage)) {
            repository.getUsers().forEach(uId -> {
                SendMessage sm = createResponse(juveResult, Constant.JUVENTUS, uId);
                resultSet.add(sm);
            });
            repository.addInfoToStorage(Constant.juventusButton, juveResult);
        }

        if (!milanoResult.equalsIgnoreCase(dataMilanoFromStorage)) {
            repository.getUsers().forEach(uId -> {
                SendMessage sm = createResponse(milanoResult, Constant.MILANO, uId);
                resultSet.add(sm);
            });
            repository.addInfoToStorage(Constant.milanoButton, milanoResult);
        }
        return resultSet;
    }

    public SendMessage createResponse(String textMessage, String URL, String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textMessage);
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(Constant.siteTransfer);
        button.setUrl(URL);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(Collections.singletonList(Collections.singletonList(button)));
        message.setReplyMarkup(markup);
        return message;
    }

    public SendMessage drawButtons(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setDisableNotification(true);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(Constant.juventusButton);
        keyboardFirstRow.add(Constant.milanoButton);

        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(Constant.points);
        return sendMessage;
    }

    public SendMessage generateSimpleMessage(String text, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setDisableNotification(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }
}
