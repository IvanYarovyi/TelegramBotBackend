package com.eone;

import com.eone.bot.TelegramUtils;
import com.eone.bot.web.JettyWebServerStarter;
import com.eone.bot.args.AppCommandLineOptions;
import com.eone.bot.args.OPTION;
import com.pengrad.telegrambot.TelegramBot;

public class Main {

    public static void main(String[] args) throws Exception {
        AppCommandLineOptions appCommandLineOptions = new AppCommandLineOptions(args);
        String token = appCommandLineOptions.getOption(OPTION.TOKEN);
        String portStr = appCommandLineOptions.getOption(OPTION.SERVER_PORT);
        String webHook = appCommandLineOptions.getOption(OPTION.WEB_HOOK);
        String certPath = appCommandLineOptions.getOption(OPTION.CERTIFICATE);

        TelegramBot telegramBot = new TelegramBot(token);
        TelegramUtils.setupWebHook(webHook, certPath, telegramBot);

        int port = JettyWebServerStarter.getWebServerPort(portStr);
        JettyWebServerStarter.start(port, telegramBot);
        //this line executes never
        System.out.println("NEVER!");
    }



}
