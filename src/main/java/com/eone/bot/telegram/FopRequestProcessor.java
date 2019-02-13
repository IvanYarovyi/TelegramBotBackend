package com.eone.bot.telegram;

import com.eone.bot.db.FopNormDao;
import com.eone.bot.db.model.FopNorm;
import com.eone.bot.telegram.updates.input.ChatInputParser;
import com.eone.bot.telegram.updates.response.FopToMessageConverter;
import com.eone.bot.telegram.updates.response.MessageSender;
import com.pengrad.telegrambot.model.Update;
import org.apache.logging.log4j.LogManager;

import java.sql.SQLException;
import java.util.List;

import static com.eone.bot.telegram.updates.response.Messages.*;

public class FopRequestProcessor implements UpdateProcessor {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(FopRequestProcessor.class);

    private MessageSender messageSender;
    private FopNormDao fopDao;


    public FopRequestProcessor(MessageSender messageSender, FopNormDao fopDao) {
        this.messageSender = messageSender;
        this.fopDao = fopDao;
    }

    @Override
    public void processUpdate(Update update) {
        Long chatId = update.message().chat().id();
        String userQuery = update.message().text();

        if (ChatInputParser.isStartCommand(userQuery)) {
            messageSender.sendMessage(chatId, MESSAGE_ABOUT);
            return;
        }

        String[] words = ChatInputParser.getWordsFromText(userQuery);
        if (!ChatInputParser.valid(words)) {
            messageSender.sendMessage(chatId, MESSAGE_QUERY_EXAMPLE);
        } else if (!isOkFirstWordLength(words[0]) ){
            messageSender.sendMessage(chatId, MESSAGE_CLARIFY);
        } else if (words.length == 2) {
            getOnlyFopNames(chatId, userQuery, words);
        } else if (words.length == 3) {
            getAllInfo(chatId, userQuery, words);
        }
    }

    private void getAllInfo(Long chatId, String userQuery, String[] words) {
        String[] s = ChatInputParser.convertToFullTextSearch(words);
        try {
            Long count = fopDao.getCount(s[0], s[1], s[2]);
            if (count > 100) {
                messageSender.sendMessage(chatId, MESSAGE_CLARIFY);
                return;
            }
        } catch (SQLException e) {
            LOG.warn(e);
            return;
        }
        sendFopsAllInfoList(chatId, words, userQuery);
    }

    private boolean isOkFirstWordLength(String word) {
        return word.length() > 2;
    }


    private boolean getOnlyFopNames(Long chatId, String userQuery, String[] words) {
        List<FopNorm> fops;
        try {
            String[] s = ChatInputParser.convertToFullTextSearch(words);
            fops = this.fopDao.getFops(s[0], s[1]);
        } catch (SQLException e) {
            LOG.warn(e.getMessage());
            return true;
        }
        if (fops.size() == 0) {
            messageSender.sendMessage(chatId, MESSAGE_NO_DATA.format(userQuery));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(MESSAGE_FOUND_COUNT.format(userQuery, fops.size()));
            sb.append(MESSAGE_LIST.getMessage());

            fops.stream()
                    .map(fop -> fop.getOther_name())
                    .distinct()
                    .sorted()
                    .forEach(
                            otherName -> sb.append(otherName).append("\n")
                    );
            messageSender.sendMessage(chatId, sb.toString());
            return true;
        }
        return false;
    }

    private boolean sendFopsAllInfoList(Long chatId, String[] words, String userQuery) {
        List<FopNorm> fops;
        try {
            String[] s = ChatInputParser.convertToFullTextSearch(words);
            fops = this.fopDao.getFops(s[0], s[1], s[2]);
        } catch (SQLException e) {
            LOG.warn(e.getMessage());
            return true;
        }

        if (fops.size() == 0) {
            messageSender.sendMessage(chatId, MESSAGE_NO_DATA.format(userQuery));
        } else {
            for (FopNorm fop : fops) {
                messageSender.sendMessage(chatId, FopToMessageConverter.formatOutput(fop));
            }
        }
        return false;
    }

}
