package com.eone.bot.telegram;

import com.pengrad.telegrambot.model.Update;

public interface UpdateProcessor {

    void processUpdate(Update update);

}
