package com.eone.bot.updates;

import com.pengrad.telegrambot.model.Update;

public interface UpdateProcessor {

    boolean processUpdate(Update update);

}