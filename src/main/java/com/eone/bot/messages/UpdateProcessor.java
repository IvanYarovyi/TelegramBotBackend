package com.eone.bot.messages;

import com.pengrad.telegrambot.model.Update;

public interface UpdateProcessor {

    boolean processUpdate(Update update);

}
