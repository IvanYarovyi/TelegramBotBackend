package com.eone.bot.updates;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.logging.log4j.LogManager;

public class MessageSender {
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(MessageSender.class);

    private TelegramBot telegramBot;

    public MessageSender(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage(Long chatId, String message) {
        SendMessage request = new SendMessage(chatId, message);
        request.parseMode(ParseMode.Markdown);
        SendResponse telegramResponse = telegramBot.execute(request);
        LOG.trace(telegramResponse.message());
    }

    public void sendMessage(Long chatId, Messages message) {
        sendMessage(chatId, message.getMessage());
    }
}
