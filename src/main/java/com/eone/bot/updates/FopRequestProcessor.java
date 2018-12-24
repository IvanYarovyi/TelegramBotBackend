package com.eone.bot.updates;

import com.eone.bot.db.FopNormDao;
import com.eone.bot.model.FopNorm;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.logging.log4j.LogManager;

import java.sql.SQLException;
import java.util.List;

public class FopRequestProcessor implements UpdateProcessor {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(FopRequestProcessor.class);
    //"За запитом: '%s' в базі нічого не знайденно."
    public static final String NO_DATA_MESSAGE = "\u0417\u0430 \u0437\u0430\u043f\u0438\u0442\u043e\u043c: '%s' \u0432 \u0431\u0430\u0437\u0456 \u043d\u0456\u0447\u043e\u0433\u043e \u043d\u0435 \u0437\u043d\u0430\u0439\u0434\u0435\u043d\u043d\u043e.";

    private TelegramBot telegramBot;
    private FopNormDao fopDao;

    public FopRequestProcessor(TelegramBot telegramBot, FopNormDao fopDao) {
        this.telegramBot = telegramBot;
        this.fopDao = fopDao;
    }

    @Override
    public boolean processUpdate(Update update) {
        String userQuery = update.message().text();

        String[] words = getQueryParams(userQuery);
        if(!valid(words)){
            SendMessage response = new SendMessage(update.message().chat().id(), "Not valid query, try Іван Іванов or Ivan Ivanov Petrovych");
            response.parseMode(ParseMode.HTML);
            SendResponse execute = telegramBot.execute(response);
            LOG.debug(execute.message());
        }
        if (words.length == 2){
            return getFopNames(update, userQuery, words);
        } else if (words.length == 3){
            return sendFopsAllInfoList(update, words);
        }
        return true;
    }

    private boolean getFopNames(Update update, String userQuery, String[] words) {
        Long id = update.message().chat().id();
        List<FopNorm> fops = null;
        try {
            String[] s = convertToFullTextSearch(words);
            fops = this.fopDao.getFops(s[0], s[1]);
        } catch (SQLException e) {
            LOG.warn(e.getMessage());
            return true;
        }
        //todo make fop limit, grouping, count ...
        if (fops.size() == 0) {
            SendMessage response = new SendMessage(update.message().chat().id(), String.format(NO_DATA_MESSAGE, userQuery));
            response.parseMode(ParseMode.HTML);
            SendResponse execute = telegramBot.execute(response);
            LOG.trace(execute);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("За фільтром %s в базі знайденно: %d записів.", userQuery, fops.size()));
            sb.append("\n *Уточніть ваш запит, та спробуйте знову.*");
            sb.append("\n _Список прізвищ що співпали з фільтром:_");
            fops.stream()
                    .forEach(fop -> sb.append(fop.getOther_name())
                    .append("\n"));
            SendMessage response
                    = new SendMessage(update.message().chat().id(), sb.toString());
            response.parseMode(ParseMode.Markdown);
            SendResponse execute = telegramBot.execute(response);
            LOG.trace(execute);
            return true;
        }
        return false;
    }

    private boolean sendFopsAllInfoList(Update update, String[] words) {
        List<FopNorm> fops = null;
        try {
            String[] s = convertToFullTextSearch(words);
            fops = this.fopDao.getFops(s[0], s[1], s[2]);
        } catch (SQLException e) {
            LOG.warn(e.getMessage());
            return true;
        }
        //todo make fop limit, grouping, count ...
        if (fops.size() == 0) {
            SendMessage response = new SendMessage(update.message().chat().id(), NO_DATA_MESSAGE);
            response.parseMode(ParseMode.HTML);
            SendResponse execute = telegramBot.execute(response);
            LOG.trace(execute);
        } else {
            for (FopNorm fop : fops) {
                SendMessage response = new SendMessage(update.message().chat().id(), formatOutput(fop));
                response.parseMode(ParseMode.Markdown);
                SendResponse execute = telegramBot.execute(response);
                LOG.trace(execute);
            }
        }
        return false;
    }

    public static boolean valid(String[] words){
        if (words.length == 2 || words.length == 3){
            return true;
        }
        return false;
    }

    private static String[] getQueryParams(String query) {
        String[] words = query.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("^\\w", "");
        }
        return words;
    }

    private String getLFO(FopNorm fopNorm) {
        return fopNorm.getLast_name() + " " + fopNorm.getFirst_name() + " " + fopNorm.getOther_name();
    }

    private String formatOutput(FopNorm fop) {

        return "`" + getLFO(fop) + "`\n"
                + "_" + fop.getAddress() + "_\n"
                + fop.getActivity() + "\n "
                + "*" + fop.getStatus() + "*";

    }

    private String[] convertToFullTextSearch( String[] queryParamStrings) {
        String[] param3 = new String[3];
        param3[0] = "%";
        param3[1] = "%";
        param3[2] = "%";
        for (int i = 0; i < queryParamStrings.length; i++) {
            param3[i] = queryParamStrings[i]+"%";
        }
        return param3;
    }

}
