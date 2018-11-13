package com.eone.bot.update;

import com.pengrad.telegrambot.model.Update;

public interface UpdateProcessor {
    boolean processUpdate(Update update);
}
