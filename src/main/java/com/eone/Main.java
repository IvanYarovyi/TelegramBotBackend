package com.eone;

import com.eone.bot.TelegramUtils;
import com.eone.bot.db.FopDao;
import com.eone.bot.db.FopNormDao;
import com.eone.bot.model.FopNorm;
import com.eone.bot.updates.FopRequestProcessor;
import com.eone.bot.web.JettyWebServerStarter;
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
        String dbUrl = commandLineOptions.getOption(OPTION.DB_URL);
        String portStr = commandLineOptions.getOption(OPTION.SERVER_PORT);
        String publicIp = commandLineOptions.getOption(OPTION.PUBLIC_IP);
        String certPath = commandLineOptions.getOption(OPTION.CERTIFICATE);


        TelegramBot telegramBot = new TelegramBot(token);
        TelegramUtils.setupWebHook(publicIp, certPath, telegramBot);

        int port = JettyWebServerStarter.getWebServerPort(portStr);
        FopNormDao fopDao = new FopNormDao(dbUrl);
        JettyWebServerStarter.start(port, telegramBot, new FopRequestProcessor(telegramBot, fopDao));
        //this line executes never
    }

}
