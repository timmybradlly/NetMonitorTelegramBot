package ru.itbaris.NetMonitorTelegramBot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.itbaris.NetMonitorTelegramBot.config.BotConfig;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config){
        this.config = config;
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            if(update.getMessage().hasText()){
                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();

                if (messageText.equals("/start")){
                    startCommandRecived(chatId, update.getMessage().getChat().getUserName());
                } else {
                    uncknownCommane(chatId);
                }
            }
        }
    }

    private void uncknownCommane(long chatId) {
        sendMessage(chatId, "Unknown command. Sorry.");
    }

    private void startCommandRecived(long chatId, String userName) {
        String answer = "Hi, "+ userName +", nice to meet you!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String answer) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(answer);
        try{
            execute(message);
        } catch (TelegramApiException e) {
            return;
            //throw new RuntimeException(e);
        }

    }

    @Override
    public String getBotUsername() {
        return this.config.getBotName();
    }
}
