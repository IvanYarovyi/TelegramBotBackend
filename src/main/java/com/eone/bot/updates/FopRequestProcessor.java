package com.eone.bot.updates;

import com.eone.bot.db.FopNormDao;
import com.eone.bot.model.Fop;
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
    public static final int GET_FOPS_LIMIT = 40;
    public static final int PRINT_FOPS_LIMIT = 5;

    private TelegramBot telegramBot;
    private FopNormDao fopDao;

    public FopRequestProcessor(TelegramBot telegramBot, FopNormDao fopDao) {
        this.telegramBot = telegramBot;
        this.fopDao = fopDao;
    }

    @Override
    public boolean processUpdate(Update update) {
        String text = update.message().text();
        List<FopNorm> fops = null;
        int count;
        try {
            String[] s = convertToFullTextSearch(text);
            count = this.fopDao.getCount(s[0], s[1], s[2]);
            if (count > 0 && count < GET_FOPS_LIMIT) {
                fops = this.fopDao.getFops(s[0], s[1], s[2]);
            }
        } catch (SQLException e) {
            LOG.warn(e.getMessage());
            return false;
        }
        if (count == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("За фільтром %s в базі нічого не знайденно.", text));
            sb.append("\n *Уточніть ваш запит, та спробуйте знову.*");
            SendMessage response
                    = new SendMessage(update.message().chat().id(), sb.toString());
            response.parseMode(ParseMode.Markdown);
            SendResponse execute = telegramBot.execute(response);
            LOG.trace(execute);
        }
        if (count > GET_FOPS_LIMIT) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("За фільтром %s в базі знайденно *%d* записів.", text, count));
            sb.append("\n *Уточніть ваш запит, та спробуйте знову.*");
            SendMessage response
                    = new SendMessage(update.message().chat().id(), sb.toString());
            response.parseMode(ParseMode.Markdown);
            SendResponse execute = telegramBot.execute(response);
            LOG.trace(execute);
        } else if (count > PRINT_FOPS_LIMIT) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("За фільтром %s в базі знайденно: %d записів.", text, fops.size()));
            sb.append("\n *Уточніть ваш запит, та спробуйте знову.*");
            sb.append("\n _Список осіб що співпали з фільтром:_");
            fops.stream().forEach(fop -> sb.append(getLFO(fop)).append("\n"));

            SendMessage response
                    = new SendMessage(update.message().chat().id(), sb.toString());
            response.parseMode(ParseMode.Markdown);
            SendResponse execute = telegramBot.execute(response);
            LOG.trace(execute);
        } else {
            for (FopNorm fop :
                    fops) {
                SendMessage response
                        = new SendMessage(update.message().chat().id(), formatOutput(fop));
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

    private String formatOutput(List<Fop> fops) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Fop fop :
                fops) {
            stringBuilder.append("`" + fop.getFull_name() + "; " + fop.getAddress() + ", " + fop.getActivity() + ", " + fop.getStatus() + "`");
        }
        return stringBuilder.toString();
    }

    private String convertTextToSqlLikeParam(String string) {
        return "%" + string.replace(" ", "%") + "%";
    }

    private String[] convertToFullTextSearch(String string) {
        String[] split = string.split(" ");
        return split;
    }

}
