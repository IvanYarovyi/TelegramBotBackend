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

    private TelegramBot telegramBot;
    private FopNormDao fopDao;

    public FopRequestProcessor(TelegramBot telegramBot, FopNormDao fopDao) {
        this.telegramBot = telegramBot;
        this.fopDao = fopDao;
    }

    @Override
    public boolean processUpdate(Update update) {
        String userQuery = update.message().text();
        List<FopNorm> fops = null;
        try {
            String[] s = convertToFullTextSearch(userQuery);
            fops = this.fopDao.getFops(s[0], s[1], s[2]);
        } catch (SQLException e) {
            LOG.warn(e.getMessage());
            return false;
        }
        //todo make fop limit, grouping, count ...
        if (fops.size() == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("За фільтром %s в базі нічого не знайденно.", userQuery));
            sb.append("\n *Уточніть ваш запит, та спробуйте знову.*");
            SendMessage response
                    = new SendMessage(update.message().chat().id(), sb.toString());
            response.parseMode(ParseMode.Markdown);
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
        return true;
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

    private String[] convertToFullTextSearch(String string) {
        String[] split = string.split(" ");
        String[] param3 = new String[3];
        param3[0] = "%";
        param3[1] = "%";
        param3[2] = "%";
        if (split.length == 3){
            param3[0] = split[0]+"%";
            param3[1] = split[1]+"%";
            param3[2] = split[2]+"%";
        }
        return param3;
    }

}
