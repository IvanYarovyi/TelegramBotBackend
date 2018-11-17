package com.eone;

import com.eone.bot.TelegramUtils;
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
        AppCommandLineOptions сommandLineOpt = new AppCommandLineOptions(args);
        String token = сommandLineOpt.getOption(OPTION.TOKEN);
        String portStr = сommandLineOpt.getOption(OPTION.SERVER_PORT);
        String publicIp = сommandLineOpt.getOption(OPTION.PUBLIC_IP);
        String certPath = сommandLineOpt.getOption(OPTION.CERTIFICATE);
        TelegramBot telegramBot = new TelegramBot(token);
        TelegramUtils.setupWebHook(publicIp, certPath, telegramBot);

        int port = JettyWebServerStarter.getWebServerPort(portStr);
        JettyWebServerStarter.start(port, telegramBot);
        //this line executes never
    }

}
