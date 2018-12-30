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

    //"За запитом: \"%s\" в базі нічого не знайденно."
    private static final String MESSAGE_NO_DATA = "\u0417\u0430 \u0437\u0430\u043f\u0438\u0442\u043e\u043c: \" %s \" \u0432 \u0431\u0430\u0437\u0456 \u043d\u0456\u0447\u043e\u0433\u043e \u043d\u0435 \u0437\u043d\u0430\u0439\u0434\u0435\u043d\u043d\u043e.";
    //"Не валідний запит. Спробуйте: _Мельник Іван Іванович_ або _Мельник Іван_ ";
    private static final String MESSAGE_QUERY_EXAMPLE = "\u041d\u0435 \u0432\u0430\u043b\u0456\u0434\u043d\u0438\u0439 \u0437\u0430\u043f\u0438\u0442. \u0421\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435: _\u041c\u0435\u043b\u044c\u043d\u0438\u043a \u0406\u0432\u0430\u043d \u0406\u0432\u0430\u043d\u043e\u0432\u0438\u0447_ \u0430\u0431\u043e _\u041c\u0435\u043b\u044c\u043d\u0438\u043a \u0406\u0432\u0430\u043d_ ";
    //"За фільтром \"%s\" в базі знайденно: %d записів."
    private static final String MESSAGE_FOUND_COUNT = "\u0417\u0430 \u0444\u0456\u043b\u044c\u0442\u0440\u043e\u043c \" %s \" \u0432 \u0431\u0430\u0437\u0456 \u0437\u043d\u0430\u0439\u0434\u0435\u043d\u043d\u043e: %d \u0437\u0430\u043f\u0438\u0441\u0456\u0432.";
    //"\n _В системі присутні такі 'по батькові':_ \n"
    private static final String MESSAGE_LIST = "\n _\u0412 \u0441\u0438\u0441\u0442\u0435\u043c\u0456 \u043f\u0440\u0438\u0441\u0443\u0442\u043d\u0456 \u0442\u0430\u043a\u0456 '\u043f\u043e \u0431\u0430\u0442\u044c\u043a\u043e\u0432\u0456':_ \n";
    //"Просто відправте повідомлення з Прізвищем Ім'ям та Побатькові."
    private static final String MESSAGE_ABOUT = "\u041f\u0440\u043e\u0441\u0442\u043e \u0432\u0456\u0434\u043f\u0440\u0430\u0432\u0442\u0435 \u043f\u043e\u0432\u0456\u0434\u043e\u043c\u043b\u0435\u043d\u043d\u044f \u0437 \u041f\u0440\u0456\u0437\u0432\u0438\u0449\u0435\u043c \u0406\u043c'\u044f\u043c \u0442\u0430 \u041f\u043e\u0431\u0430\u0442\u044c\u043a\u043e\u0432\u0456.";
    //"Уточніть ваш запит."
    private static final String MESSAGE_CLARIFY = "\u0423\u0442\u043e\u0447\u043d\u0456\u0442\u044c \u0432\u0430\u0448 \u0437\u0430\u043f\u0438\u0442.";


    private TelegramBot telegramBot;
    private FopNormDao fopDao;

    public FopRequestProcessor(TelegramBot telegramBot, FopNormDao fopDao) {
        this.telegramBot = telegramBot;
        this.fopDao = fopDao;
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage request = new SendMessage(chatId, message);
        request.parseMode(ParseMode.Markdown);
        SendResponse telegramResponse = telegramBot.execute(request);
        LOG.trace(telegramResponse);
    }

    @Override
    public boolean processUpdate(Update update) {
        Long chatId = update.message().chat().id();
        String userQuery = update.message().text();
        if("/start".equals(userQuery)){
            sendMessage(chatId, MESSAGE_ABOUT);
            return true;
        }

        String[] words = getWordsFromText(userQuery);
        if(!valid(words)){
            sendMessage(chatId, MESSAGE_QUERY_EXAMPLE);
        } else if (words.length == 2){
            getFopNames(chatId, userQuery, words);
        } else if (words.length == 3){
            if(words[0].length() < 4){

                String[] s = convertToFullTextSearch(words);
                try {
                    Long count = fopDao.getCount(s[0], s[1], s[2]);
                    if (count > 100){
                        sendMessage(chatId, MESSAGE_CLARIFY);
                        return true;
                    }
                } catch (SQLException e) {
                    LOG.warn(e);
                    return true;
                }
            }
            sendFopsAllInfoList(chatId, words, userQuery);
        }
        return true;
    }

    private boolean getFopNames(Long chatId, String userQuery, String[] words) {
        List<FopNorm> fops;
        try {
            String[] s = convertToFullTextSearch(words);
            fops = this.fopDao.getFops(s[0], s[1]);
        } catch (SQLException e) {
            LOG.warn(e.getMessage());
            return true;
        }
        if (fops.size() == 0) {
            sendMessage(chatId, String.format(MESSAGE_NO_DATA, userQuery));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format(MESSAGE_FOUND_COUNT, userQuery, fops.size()));
            sb.append(MESSAGE_LIST);

            fops.stream()
                    .map(fop -> fop.getOther_name())
                    .distinct()
                    .sorted()
                    .forEach(
                            otherName -> sb.append(otherName).append("\n")
                    );
            sendMessage(chatId, sb.toString());
            return true;
        }
        return false;
    }

    private boolean sendFopsAllInfoList(Long chatId, String[] words, String userQuery) {
        List<FopNorm> fops;
        try {
            String[] s = convertToFullTextSearch(words);
            fops = this.fopDao.getFops(s[0], s[1], s[2]);
        } catch (SQLException e) {
            LOG.warn(e.getMessage());
            return true;
        }

        if (fops.size() == 0) {
            sendMessage(chatId, String.format(MESSAGE_NO_DATA, userQuery));
        } else {
            for (FopNorm fop : fops) {
                sendMessage(chatId, formatOutput(fop));
            }
        }
        return false;
    }

    private static boolean valid(String[] words){
        if (words.length == 2 || words.length == 3){
            return true;
        }
        return false;
    }

    private static String[] getWordsFromText(String userText) {
        String[] words = userText.split("\\s+");
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
