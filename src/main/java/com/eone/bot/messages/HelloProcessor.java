package com.eone.bot.messages;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class HelloProcessor implements UpdateProcessor {

    private TelegramBot telegramBot;

    public HelloProcessor(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public boolean processUpdate(Update update){
        User from = update.message().from();
        String firstName = from.firstName();
        String message = "Hello "+firstName + "!";

        SendMessage sendMessage
                = new SendMessage(update.message().chat().id(), message);
        sendMessage.parseMode(ParseMode.Markdown);

        SendResponse execute = telegramBot.execute(sendMessage);
        System.out.println(execute);
        return execute.isOk();
    }

}
