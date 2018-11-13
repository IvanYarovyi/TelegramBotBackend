package com.eone;

import com.eone.bot.Helper;
import com.eone.bot.JettyWebServer;
import com.eone.bot.args.AppCommandLineOptions;
import com.eone.bot.args.OPTION;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetWebhookInfo;
import com.pengrad.telegrambot.response.GetWebhookInfoResponse;

public class Main {

    public static void main(String[] args) throws Exception {
        AppCommandLineOptions appCommandLineOptions = new AppCommandLineOptions(args);
        String webHook = appCommandLineOptions.getOption(OPTION.WEB_HOOK);
        String certPath = appCommandLineOptions.getOption(OPTION.CERTIFICATE);
        String token = appCommandLineOptions.getOption(OPTION.TOKEN);
        String portStr = appCommandLineOptions.getOption(OPTION.SERVER_PORT);

        TelegramBot telegramBot = new TelegramBot(token);
        if (webHook != null & certPath != null) {
            JettyWebServer.setWebHook(webHook, certPath, telegramBot);
        }
        GetWebhookInfoResponse webHookInfoResponse = telegramBot.execute(new GetWebhookInfo());
        System.out.println(webHookInfoResponse);

        int port = Helper.getWebServerPort(portStr);

        JettyWebServer.start(port, telegramBot);
        //executes never
        System.out.println("NEVER!");
    }

}
