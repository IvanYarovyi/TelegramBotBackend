package com.eone.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import java.util.List;

//is not working since we are using web hook
@Deprecated
public class UpdatesPuller {
    private static final int BATCH_UPDATES_SIZE = 30;
    private int chatUpdatesOffset = 0;

    private TelegramBot telegramBot;

    public UpdatesPuller(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public List<Update> pullUpdates() {
        System.out.println("Offset is: "+ chatUpdatesOffset);

        GetUpdates gu = new GetUpdates().limit(BATCH_UPDATES_SIZE).offset(chatUpdatesOffset);
        GetUpdatesResponse execute = telegramBot.execute(gu);
        List<Update> updates = execute.updates();
        if (!updates.isEmpty()) {
            int lastProcessedUpdate = updates.get(updates.size() - 1).updateId();
            chatUpdatesOffset = lastProcessedUpdate + 1;
        }
        return updates;
    }
}
