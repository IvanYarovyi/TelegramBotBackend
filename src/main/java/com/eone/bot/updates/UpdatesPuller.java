package com.eone.bot.updates;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

//This get updates method is working since we are using web hook approach
@Deprecated
public class UpdatesPuller {
    private static final Logger LOG = LogManager.getLogger(UpdatesListener.class);

    private static final int BATCH_UPDATES_SIZE = 30;
    private int chatUpdatesOffset = 0;

    private TelegramBot telegramBot;

    public UpdatesPuller(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public List<Update> pullUpdates() {
        LOG.debug("Offset is: "+ chatUpdatesOffset);

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
