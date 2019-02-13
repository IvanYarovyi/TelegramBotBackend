package com.eone;

import com.eone.bot.db.DataSource;
import com.eone.bot.webapp.TelegramWebhookUtils;
import com.eone.bot.db.FopNormDao;
import com.eone.bot.telegram.FopRequestProcessor;
import com.eone.bot.telegram.updates.response.MessageSender;
import com.eone.bot.webapp.JettyWebServerStarter;
import com.eone.bot.args.AppCommandLineOptions;
import com.eone.bot.args.OPTION;
import com.pengrad.telegrambot.TelegramBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOG.info("Application was started.");
        AppCommandLineOptions commandLineOptions = new AppCommandLineOptions(args);
        String token = commandLineOptions.getOption(OPTION.TOKEN);

        String dbUrl = commandLineOptions.getOption(OPTION.DB_HOST);
        String dbUser = commandLineOptions.getOption(OPTION.DB_USER);
        String dbPassword = commandLineOptions.getOption(OPTION.DB_PASSWORD);

        String portStr = commandLineOptions.getOption(OPTION.SERVER_PORT);

        String publicIp = commandLineOptions.getOption(OPTION.PUBLIC_IP);
        String certPath = commandLineOptions.getOption(OPTION.CERTIFICATE);


        TelegramBot telegramBot = new TelegramBot(token);
        TelegramWebhookUtils.setupWebHook(publicIp, certPath, telegramBot);

        int port = JettyWebServerStarter.getWebServerPort(portStr);
        FopNormDao fopDao = new FopNormDao(new DataSource(dbUrl, dbUser, dbPassword));
        JettyWebServerStarter.start(port, telegramBot, new FopRequestProcessor(new MessageSender(telegramBot), fopDao));
        //this line never executes
    }

}
